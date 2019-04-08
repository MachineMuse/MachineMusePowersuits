package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IRightClickModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

//import mrtjp.projectred.transmission.bundledwires.TWireCommons;

/**
 * Created by User: Korynkai
 * 6:30 PM 2014-11-17
 * <p>
 * TODO: Fix ProjectRed (may require PR to ProjectRed)
 */
public class ItemModuleOmniProbe extends ItemAbstractPowerModule implements IRightClickModule, IPlayerTickModule {
    public static final String TAG_EIO_NO_COMPLETE = "eioNoCompete";
    public static final String TAG_EIO_FACADE_TRANSPARENCY = "eioFacadeTransparency";


    private ItemStack conduitProbe;
    private ItemStack rednetMeter;
    private ItemStack cpmPSD;
    private ItemStack rcMeter;
    private ItemStack prDebugger;

//    private ItemStack teMultimeter;

    public ItemModuleOmniProbe(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_TOOL);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 4));
//        ItemStack tHighest = new ItemStack(Items.COMPARATOR);
//
//        if (ModCompatibility.isMFRLoaded()) {
//            rednetMeter = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("MineFactoryReloaded", "rednet.meter")), 1);
//            tHighest = rednetMeter;
//        }
//
//        if (ModCompatibility.isRailcraftLoaded()) {
//            rcMeter = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("Railcraft", "tool.electric.meter")), 1);
//            tHighest = rcMeter;
//        }
//
//        /* Will be added when ThermalExpansion's new conduit mod is released */
//        // if (ModCompatibility.isThermalExpansionLoaded) {
//        //     teMultimeter = GameRegistry.findItemStack("ThermalExpansion", "multimeter", 1);
//        //     tHighest = teMultimeter
//        // }
//
//        if (ModCompatibility.isEnderIOLoaded()) {
//            conduitProbe = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("EnderIO", "itemConduitProbe")), 1);
//            tHighest = conduitProbe;
//        }
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), tHighest);
    }



//    public static String getEIONoCompete(@Nonnull ItemStack stack) {
//        if (!stack.isEmpty() && stack.getItem() instanceof ItemModuleOmniProbe) {
//            NBTTagCompound itemTag = MuseNBTUtils.getMuseModuleTag(stack);
//            return itemTag != null ? itemTag.getString(TAG_EIO_NO_COMPLETE) : "";
//        }
//        return "";
//    }
//
//    public static void setEIONoCompete(@Nonnull ItemStack stack, String s) {
//        if (!stack.isEmpty() && stack.getItem() instanceof ItemModuleOmniProbe) {
//            NBTTagCompound itemTag = MuseNBTUtils.getMuseModuleTag(stack);
//            itemTag.putString(TAG_EIO_NO_COMPLETE, s);
//        }
//    }
//
//    public static boolean getEIOFacadeTransparency(@Nonnull ItemStack stack) {
//        if (!stack.isEmpty() && stack.getItem() instanceof ItemModuleOmniProbe) {
//            NBTTagCompound itemTag = MuseNBTUtils.getMuseModuleTag(stack);
//            if (itemTag != null) {
//                return itemTag.getBoolean(TAG_EIO_FACADE_TRANSPARENCY);
//            }
//        }
//        return false;
//    }
//
//    public static void setEIOFacadeTransparency(@Nonnull ItemStack stack, boolean b) {
//        if (!stack.isEmpty() && stack.getItem() instanceof ItemModuleOmniProbe) {
//            NBTTagCompound itemTag = MuseNBTUtils.getMuseModuleTag(stack);
//            itemTag.putBoolean(TAG_EIO_FACADE_TRANSPARENCY, b);
//        }
//    }

















    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    // FIXME: all of these will fail due to case sensitivity issues.
    @Override
    public EnumActionResult onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
//        int block = Block.getIdFromBlock(world.getBlockState(pos).getBlock());
//
//        if (Loader.isModLoaded("MineFactoryReloaded")) {
//            if (block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("MineFactoryReloaded", "cable.redstone"))))
//                return rednetMeter.getItem().onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, EnumHand.MAIN_HAND);
//        }
//
//        if (Loader.isModLoaded("Railcraft")) {
//            if ((block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("Railcraft", "tile.railcraft.machine.alpha")))) ||
//                    (block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("Railcraft", "tile.railcraft.track")))) ||
//                    (block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("Railcraft", "tile.railcraft.machine.epsilon")))) ||
//                    (block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("Railcraft", "tile.railcraft.machine.delta"))))) {
//                return rcMeter.getItem().onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, EnumHand.MAIN_HAND);
//            }
//        }
//
//        if (Loader.isModLoaded("EnderIO")) {
//            if (block == Block.getIdFromBlock(Block.REGISTRY.getObject(new ResourceLocation("EnderIO", "blockConduitBundle")))) {
//                return conduitProbe.getItem().onItemUse(player, world, pos, EnumHand.MAIN_HAND, side, hitX, hitY, hitZ);
//            }
//        }
        return EnumActionResult.PASS;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
//        if (!OmniProbeHelper.getEIOFacadeTransparency(item)) {
//            OmniProbeHelper.setEIONoCompete(item, MPSModuleConstants.MODULE_OMNIPROBE__DATANAME);
//            OmniProbeHelper.setEIOFacadeTransparency(item, true);
//        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
//        if (!(OmniProbeHelper.getEIONoCompete(item).isEmpty()) && (!OmniProbeHelper.getEIONoCompete(item).isEmpty())) {
//            if (OmniProbeHelper.getEIONoCompete(item).equals(MPSModuleConstants.MODULE_OMNIPROBE__DATANAME)) {
//                OmniProbeHelper.setEIONoCompete(item, "");
//                if (OmniProbeHelper.getEIOFacadeTransparency(item)) {
//                    OmniProbeHelper.setEIOFacadeTransparency(item, false);
//                }
//            }
//        } else {
//            if (OmniProbeHelper.getEIOFacadeTransparency(item)) {
//                OmniProbeHelper.setEIOFacadeTransparency(item, false);
//            }
//        }
    }

    public float minF(float a, float b) {
        return a < b ? a : b;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return 0;
    }
}