package net.machinemuse.powersuits.powermodule.tool;

import extracells.api.ECApi;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

/**
 * Created by User: Korynkai
 * 10:03pm - 2014-11-15
 */

// TODO: revisit if EC2 ever gets ported

public class AppEngWirelessFluidModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_APPENG_EC_WIRELESS_FLUID = "AppEng EC Wireless Fluid Terminal";
    private ItemStack wirelessFluidTerminal;

    public AppEngWirelessFluidModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));

        //this is for versions of ExtraCells-1.7.10-2.3.0b142 and newer
        Optional<ItemStack> wirelessFluidTerminal = ECApi.instance().items().wirelessFluidTerminal().maybeStack(1);
        addInstallCost(wirelessFluidTerminal.get());
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.appengECWirelessFluid;
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_APPENG_EC_WIRELESS_FLUID;
    }

    @Override
    public String getUnlocalizedName() {
        return "appengECWirelessFluid";
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.SUCCESS, ECApi.instance().openWirelessFluidTerminal(playerIn, hand, worldIn));
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

//    @Override
//    public String getDescription() {
//        return "An Applied Energistics ExtraCells wireless fluid terminal integrated into your power tool.";
//    }
}