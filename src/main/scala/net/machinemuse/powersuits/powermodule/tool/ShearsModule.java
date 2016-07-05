package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.MusePlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import java.util.List;
import java.util.Random;

public class ShearsModule extends PowerModuleBase implements IBlockBreakingModule, IRightClickModule {
    public static final ItemStack shears = new ItemStack(Items.SHEARS);
    public static final String MODULE_SHEARS = "Shears";
    private static final String SHEARING_ENERGY_CONSUMPTION = "Shearing Energy Consumption";
    private static final String SHEARING_HARVEST_SPEED = "Shearing Harvest Speed";

    public ShearsModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(Items.IRON_INGOT, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addBaseProperty(SHEARING_ENERGY_CONSUMPTION, 50, "J");
        addBaseProperty(SHEARING_HARVEST_SPEED, 8, "x");
        addTradeoffProperty("Overclock", SHEARING_ENERGY_CONSUMPTION, 950);
        addTradeoffProperty("Overclock", SHEARING_HARVEST_SPEED, 22);
    }


    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_SHEARS;
    }

    @Override
    public String getUnlocalizedName() { return "shears";
    }

    @Override
    public String getDescription() {
        return "Cuts through leaves, wool, and creepers alike.";
    }

    @Override
    public void onRightClick(EntityPlayer playerClicking, World world, ItemStack stack) {
        if (playerClicking.worldObj.isRemote) {
            return;
        }
        RayTraceResult hitMOP = MusePlayerUtils.raytraceEntities(world, playerClicking, false, 8);

        if (hitMOP != null && hitMOP.entityHit instanceof IShearable) {
            IShearable target = (IShearable) hitMOP.entityHit;
            Entity entity = hitMOP.entityHit;
            if (target.isShearable(stack, entity.worldObj, entity.getPosition())) {
                List<ItemStack> drops = target.onSheared(stack, entity.worldObj, entity.getPosition(),
                        EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByLocation("fortune"), stack));

                Random rand = new Random();
                for (ItemStack drop : drops) {
                    EntityItem ent = entity.entityDropItem(drop, 1.0F);
                    ent.motionY += rand.nextFloat() * 0.05F;
                    ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                    ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                }
                ElectricItemUtils.drainPlayerEnergy(playerClicking, ModuleManager.computeModularProperty(stack, SHEARING_ENERGY_CONSUMPTION));
            }
        }
    }

    @Override
    public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {

    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (ForgeHooks.canToolHarvestBlock(player.worldObj, pos, shears)) {
            if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, SHEARING_ENERGY_CONSUMPTION)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemstack, World world, IBlockState state, BlockPos pos, EntityPlayer player) {
        if (player.worldObj.isRemote) {
            return false;
        }
        Block block = state.getBlock();

        if (block instanceof IShearable && ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(itemstack, SHEARING_ENERGY_CONSUMPTION)) {
            IShearable target = (IShearable) block;

            if (target.isShearable(itemstack, player.worldObj, pos)) {
                List<ItemStack> drops = target.onSheared(itemstack, player.worldObj, pos,
                        EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByLocation("fortune"), itemstack));
                Random rand = new Random();

                for (ItemStack stack : drops) {
                    float f = 0.7F;
                    double d = rand.nextFloat() * f + (1.0F - f) * 0.5D;
                    double d1 = rand.nextFloat() * f + (1.0F - f) * 0.5D;
                    double d2 = rand.nextFloat() * f + (1.0F - f) * 0.5D;
                    EntityItem entityitem = new EntityItem(player.worldObj, pos.getX() + d, pos.getY() + d1, pos.getZ() + d2, stack);
                    entityitem.setDefaultPickupDelay();
                    player.worldObj.spawnEntityInWorld(entityitem);
                }

                ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(itemstack, SHEARING_ENERGY_CONSUMPTION));
                player.addStat(StatList.getBlockStats(block), 1);
            }
        }
        return false;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        // TODO: MAKE NOT STUPID
        float defaultEffectiveness = 8;
        double ourEffectiveness = ModuleManager.computeModularProperty(event.getEntityPlayer().getHeldItemMainhand(), SHEARING_HARVEST_SPEED);
        event.setNewSpeed((float) (event.getNewSpeed() * Math.max(defaultEffectiveness, ourEffectiveness)));

    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(shears).getParticleTexture();
    }

}
