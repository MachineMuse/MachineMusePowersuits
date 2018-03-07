package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IBlockBreakingModule;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.utils.misc.RayTraceUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.machinemuse.utils.ElectricItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import java.util.List;
import java.util.Random;

public class ShearsModule extends PowerModuleBase implements IBlockBreakingModule, IRightClickModule {
    private static final ItemStack emulatedTool = new ItemStack(Items.SHEARS);
    private static final String SHEARING_ENERGY_CONSUMPTION = "Shearing Energy Consumption";
    private static final String SHEARING_HARVEST_SPEED = "Shearing Harvest Speed";

    public ShearsModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TOOLONLY, resourceDommain, UnlocalizedName);
        addInstallCost(new ItemStack(Items.IRON_INGOT, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addBasePropertyInt(SHEARING_ENERGY_CONSUMPTION, 50, "J");
        addBasePropertyDouble(SHEARING_HARVEST_SPEED, 8, "x");
        addTradeoffPropertyInt("Overclock", SHEARING_ENERGY_CONSUMPTION, 950);
        addTradeoffPropertyDouble("Overclock", SHEARING_HARVEST_SPEED, 22);
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_TOOL;
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (playerIn.world.isRemote) {
            return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
        }
        RayTraceResult rayTraceResult = RayTraceUtils.raytraceEntities(worldIn, playerIn, false, 8);

        if (rayTraceResult != null && rayTraceResult.entityHit instanceof IShearable) {
            IShearable target = (IShearable) rayTraceResult.entityHit;
            Entity entity = rayTraceResult.entityHit;
            if (target.isShearable(itemStackIn, entity.world, new BlockPos(entity))) {
                List<ItemStack> drops = target.onSheared(itemStackIn, entity.world, new BlockPos(entity),
                        EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByLocation("fortune"), itemStackIn));
                Random rand = new Random();
                for (ItemStack drop : drops) {
                    EntityItem ent = entity.entityDropItem(drop, 1.0F);
                    ent.motionY += rand.nextFloat() * 0.05F;
                    ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                    ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                }
                ElectricItemUtils.drainPlayerEnergy(playerIn, ModuleManager.getInstance().computeModularPropertyInteger(itemStackIn, SHEARING_ENERGY_CONSUMPTION));
                return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
            }
        }
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {
        if (ToolHelpers.isEffectiveTool(state, emulatedTool)) {
            if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.getInstance().computeModularPropertyDouble(stack, SHEARING_ENERGY_CONSUMPTION)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (entityLiving.world.isRemote) {
            return false;
        }
        Block block = state.getBlock();

        if (block instanceof IShearable && ElectricItemUtils.getPlayerEnergy(((EntityPlayer) entityLiving)) > ModuleManager.getInstance().computeModularPropertyDouble(itemStack, SHEARING_ENERGY_CONSUMPTION)) {
            IShearable target = (IShearable) block;
            if (target.isShearable(itemStack, entityLiving.world, pos)) {
                List<ItemStack> drops = target.onSheared(itemStack, entityLiving.world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack));
                Random rand = new Random();

                for (ItemStack stack : drops) {
                    float f = 0.7F;
                    double d = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
                    double d1 = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
                    double d2 = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
                    EntityItem entityitem = new EntityItem(entityLiving.world, (double) pos.getX() + d, (double) pos.getY() + d1, (double) pos.getZ() + d2, stack);
                    entityitem.setDefaultPickupDelay(); // this is 10
                    entityitem.world.spawnEntity(entityitem);
                }
                ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving, ModuleManager.getInstance().computeModularPropertyInteger(itemStack, SHEARING_ENERGY_CONSUMPTION));
                ((EntityPlayer) (entityLiving)).addStat(StatList.getBlockStats(block), 1);
                return true;
            }
        }
        return false;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
//        // TODO: MAKE NOT STUPID ?
        float defaultEffectiveness = 8;
        double ourEffectiveness = ModuleManager.getInstance().computeModularPropertyDouble(event.getEntityPlayer().inventory.getCurrentItem(), SHEARING_HARVEST_SPEED);
        event.setNewSpeed((float) (event.getNewSpeed() *Math.max(defaultEffectiveness, ourEffectiveness)));
    }

    @Override
    public ItemStack getEmulatedTool() {
        return emulatedTool;
    }
}
