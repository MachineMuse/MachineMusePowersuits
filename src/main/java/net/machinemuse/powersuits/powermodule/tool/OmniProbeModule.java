package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.misc.ModCompatibility;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IRightClickModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.modulehelpers.OmniProbeHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by User: Korynkai
 * 6:30 PM 2014-11-17
 * <p>
 * TODO: Fix ProjectRed (may require PR to ProjectRed)
 */
public class OmniProbeModule extends PowerModuleBase implements IRightClickModule, IPlayerTickModule {
    private ItemStack rcMeter = ItemStack.EMPTY;

    private ItemStack conduitProbe = ItemStack.EMPTY;

    private ItemStack teMultimeter = ItemStack.EMPTY;

//    private ItemStack rednetMeter = ItemStack.EMPTY;

//    private ItemStack euMeter = ItemStack.EMPTY;

    public OmniProbeModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 4));
        ItemStack tHighest = new ItemStack(Items.COMPARATOR);

        // Does not exist
//        if (ModCompatibility.isMFRLoaded()) {
//            rednetMeter = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("MineFactoryReloaded", "rednet.meter")), 1);
//            tHighest = rednetMeter;
//        }

        if (ModCompatibility.isRailcraftLoaded()) {
            rcMeter = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("railcraft", "tool_charge_meter")), 1);
            tHighest = rcMeter;
        }

        /* untested */
        if (ModCompatibility.isThermalExpansionLoaded()) {
            teMultimeter = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("thermalexpansion", "multimeter")), 1);
            tHighest = teMultimeter;
        }

        if (ModCompatibility.isEnderIOLoaded()) {
            conduitProbe = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("enderio", "item_conduit_probe")), 1);
            tHighest = conduitProbe;
        }
        ModuleManager.INSTANCE.addInstallCost(getDataName(), tHighest);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_OMNIPROBE__DATANAME;
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        Block block = world.getBlockState(pos).getBlock();

        if (block == null || block.isAir(world.getBlockState(pos), world, pos))
            return EnumActionResult.PASS;

        try {
            if (ModCompatibility.isEnderIOLoaded()) {
                if (conduitProbe.getItem().onItemUse(player, world, pos, EnumHand.MAIN_HAND, side, hitX, hitY, hitZ) == EnumActionResult.SUCCESS)
                    return EnumActionResult.SUCCESS;
            }

//            if (ModCompatibility.isMFRLoaded()) {
//                if (block == Block.REGISTRY.getObject(new ResourceLocation("minefactoryreloaded", "cable.redstone")))
//                    return rednetMeter.getItem().onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, EnumHand.MAIN_HAND);
//            }

            if (ModCompatibility.isRailcraftLoaded()) {
                if (rcMeter.getItem().onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS)
                    return EnumActionResult.SUCCESS;
            }

            if(ModCompatibility.isThermalExpansionLoaded()) {
                if (teMultimeter.getItem().onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS)
                    return EnumActionResult.SUCCESS;
            }
        } catch (Exception ignored) {

        }
        return EnumActionResult.PASS;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        if (!OmniProbeHelper.getEIOFacadeTransparency(item)) {
            OmniProbeHelper.setEIONoCompete(item, MPSModuleConstants.MODULE_OMNIPROBE__DATANAME);
            OmniProbeHelper.setEIOFacadeTransparency(item, true);
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        if (!(OmniProbeHelper.getEIONoCompete(item).isEmpty()) && (!OmniProbeHelper.getEIONoCompete(item).isEmpty())) {
            if (OmniProbeHelper.getEIONoCompete(item).equals(MPSModuleConstants.MODULE_OMNIPROBE__DATANAME)) {
                OmniProbeHelper.setEIONoCompete(item, "");
                if (OmniProbeHelper.getEIOFacadeTransparency(item)) {
                    OmniProbeHelper.setEIOFacadeTransparency(item, false);
                }
            }
        } else {
            if (OmniProbeHelper.getEIOFacadeTransparency(item)) {
                OmniProbeHelper.setEIOFacadeTransparency(item, false);
            }
        }
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

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.omniProbe;
    }
}
