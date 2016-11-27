package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.electricity.IModularItem;
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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;

import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import java.util.ArrayList;
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
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
//        if (playerIn.worldObj.isRemote) {
//            return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
//        }
//        RayTraceResult rayTraceResult = MusePlayerUtils.raytraceEntities(worldIn, playerIn, false, 8);
//
//        if (rayTraceResult != null && rayTraceResult.entityHit instanceof IShearable) {
//            IShearable target = (IShearable) rayTraceResult.entityHit;
//            Entity entity = rayTraceResult.entityHit;
//            if (target.isShearable(itemStackIn, entity.worldObj, new BlockPos(entity))) {
//                ArrayList<ItemStack> drops = target.onSheared(itemStackIn, entity.worldObj,
//                        (int) entity.posX, (int) entity.posY, (int) entity.posZ,
//                        EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemStackIn));
//
//                Random rand = new Random();
//                for (ItemStack drop : drops) {
//                    EntityItem ent = entity.entityDropItem(drop, 1.0F);
//                    ent.motionY += rand.nextFloat() * 0.05F;
//                    ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
//                    ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
//                }
//                ElectricItemUtils.drainPlayerEnergy(playerIn, ModuleManager.computeModularProperty(itemStackIn, SHEARING_ENERGY_CONSUMPTION));
//            }
//        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return null;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return null;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {
        // TODO: The params needed for ForgeHooks.canToolHarvestBlock don't take us where we need to go, but
        // we can use our own helper function to do the same thing with the parameters above.





//        if (ForgeHooks.canToolHarvestBlock(block, state, shears)) {
//            if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, SHEARING_ENERGY_CONSUMPTION)) {
//                return true;
//            }
//        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
//        if (player.worldObj.isRemote) {
//            return false;
//        }
//        if (block instanceof IShearable && ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(itemstack, SHEARING_ENERGY_CONSUMPTION)) {
//            IShearable target = (IShearable) block;
//            if (target.isShearable(itemstack, player.worldObj, x, y, z)) {
//                ArrayList<ItemStack> drops = target.onSheared(itemstack, player.worldObj, x, y, z,
//                        EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemstack));
//                Random rand = new Random();
//
//                for (ItemStack stack : drops) {
//                    float f = 0.7F;
//                    double d = rand.nextFloat() * f + (1.0F - f) * 0.5D;
//                    double d1 = rand.nextFloat() * f + (1.0F - f) * 0.5D;
//                    double d2 = rand.nextFloat() * f + (1.0F - f) * 0.5D;
//                    EntityItem entityitem = new EntityItem(player.worldObj, x + d, y + d1, z + d2, stack);
//                    entityitem.delayBeforeCanPickup = 10;
//                    player.worldObj.spawnEntityInWorld(entityitem);
//                }
//
//                ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(itemstack, SHEARING_ENERGY_CONSUMPTION));
//                player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(block)], 1);
//            }
//        }
        return false;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
//        // TODO: MAKE NOT STUPID
//        float defaultEffectiveness = 8;
//        double ourEffectiveness = ModuleManager.computeModularProperty(event.entityPlayer.inventory.getCurrentItem(), SHEARING_HARVEST_SPEED);
//        event.newSpeed *= Math.max(defaultEffectiveness, ourEffectiveness);

    }

    @Override
    public String getTextureFile() {
        return null;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(shears).getParticleTexture();
    }

    @Override
    public ItemStack getEmulatedTool() {
        return shears;
    }
}