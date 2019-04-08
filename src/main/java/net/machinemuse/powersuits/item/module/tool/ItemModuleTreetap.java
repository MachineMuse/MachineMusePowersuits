package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IRightClickModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

/**
 * Created by User: Andrew2448
 * 7:45 PM 4/23/13
 * <p>
 * Updated by leon on 6/14/16.
 */

public class ItemModuleTreetap extends ItemAbstractPowerModule implements IRightClickModule {
    public static ItemStack resin;
    public static Block rubber_wood;
    public static ItemStack emulatedTool;
    public static ItemStack treetap;
    private Method attemptExtract;
    private boolean isIC2Classic;

    public ItemModuleTreetap(String regName) {
        // TODO: add support for tree taps from other mods?

        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_TOOL);
//        if (ModCompatibility.isIndustrialCraftClassicLoaded()) {
//            emulatedTool = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("ic2", "itemTreetapElectric")), 1);
//            treetap = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("ic2", "itemTreetap")), 1);
//            resin = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("ic2", "misc_resource")), 1, 4);
//            rubber_wood = Block.REGISTRY.getObject(new ResourceLocation("ic2", "blockRubWood"));
//            try {
//                attemptExtract = treetap.getItem().getClass().
//                        getDeclaredMethod("attemptExtract", ItemStack.class, EntityPlayer.class, World.class, BlockPos.class, EnumFacing.class, List.class);
//            } catch (Exception ignored) {
//
//            }
//            isIC2Classic = true;
//        } else {
//            emulatedTool = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("ic2", "electric_treetap")), 1);
//            treetap = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("ic2", "treetap")), 1);
//            resin = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("ic2", "misc_resource")), 1, 4);
//            rubber_wood = Block.REGISTRY.getObject(new ResourceLocation("ic2", "rubber_wood"));
//            try {
//                attemptExtract = treetap.getItem().getClass().
//                        getDeclaredMethod("attemptExtract", EntityPlayer.class, World.class, BlockPos.class, EnumFacing.class, IBlockState.class, List.class);
//            } catch (Exception ignored) {
//
//            }
//            isIC2Classic = false;
//        }
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), emulatedTool);
//
//        addBasePropertyDouble(MPSModuleConstants.ENERGY_CONSUMPTION, 1000, "RF");
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        IBlockState state = world.getBlockState(pos);
//        Block block = state.getBlock();
//
//        try {
//            // IC2 Classic
//            if (isIC2Classic) {
//                if (block == rubber_wood && getEnergyUsage(itemStack) < ElectricItemUtils.getPlayerEnergy(player)) {
//                    if (attemptExtract.invoke("attemptExtract", null, player, world, pos, facing, null).equals(true)) {
//                        ElectricItemUtils.drainPlayerEnergy(player, (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.ENERGY_CONSUMPTION));
//                        return EnumActionResult.SUCCESS;
//                    }
//                }
//            }
//            // IC2 Experimental
//            else {
//                if (block == rubber_wood && getEnergyUsage(itemStack) < ElectricItemUtils.getPlayerEnergy(player)) {
//                    if (attemptExtract.invoke("attemptExtract", player, world, pos, facing, state, null).equals(true)) {
//                        ElectricItemUtils.drainPlayerEnergy(player, (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.ENERGY_CONSUMPTION));
//                        return EnumActionResult.SUCCESS;
//                    }
//                }
//            }
//            return EnumActionResult.PASS;
//        } catch (Exception ignored) {
//
//        }
        return EnumActionResult.FAIL;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return 0;
//        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.ENERGY_CONSUMPTION);
    }
}