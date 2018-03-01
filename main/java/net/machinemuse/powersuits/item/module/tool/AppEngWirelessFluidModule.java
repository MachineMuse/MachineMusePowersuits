package net.machinemuse.powersuits.item.module.tool;

import extracells.api.ECApi;
import net.machinemuse.item.powersuits.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IRightClickModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * Created by User: Korynkai
 * 10:03pm - 2014-11-15
 */

// TODO: revisit if EC2 ever gets ported

public class AppEngWirelessFluidModule extends PowerModuleBase implements IRightClickModule {
    private ItemStack wirelessFluidTerminal;

    public AppEngWirelessFluidModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TOOLONLY, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));

        //this is for versions of ExtraCells-1.7.10-2.3.0b142 and newer
        Optional<ItemStack> wirelessFluidTerminal = ECApi.instance().items().wirelessFluidTerminal().maybeStack(1);
        addInstallCost(wirelessFluidTerminal.get());

        //this is for older versions of extra cells and should be avoided as it relies on an outdated AE2 API
//        wirelessFluidTerminal = ECApi.instance().items().wirelessFluidTerminal().stack(1);
//        addInstallCost(wirelessFluidTerminal);
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_TOOL;
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ECApi.instance().openWirelessFluidTerminal(playerIn, hand, worldIn);
        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }
}