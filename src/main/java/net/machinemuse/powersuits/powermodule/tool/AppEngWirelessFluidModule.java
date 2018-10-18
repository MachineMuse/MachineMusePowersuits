package net.machinemuse.powersuits.powermodule.tool;

import com.google.common.base.Optional;
import extracells.api.ECApi;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by User: Korynkai
 * 10:03pm - 2014-11-15
 */

public class AppEngWirelessFluidModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_APPENG_EC_WIRELESS_FLUID = "AppEng EC Wireless Fluid Terminal";
    private ItemStack wirelessFluidTerminal;

    public AppEngWirelessFluidModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));

        //this is for versions of ExtraCells-1.7.10-2.3.0b142 and newer
        Optional<ItemStack> wirelessFluidTerminal = ECApi.instance().items().wirelessFluidTerminal().maybeStack(1);
        addInstallCost(wirelessFluidTerminal.get());

        //this is for older versions of extra cells and should be avoided as it relies on an outdated AE2 API
//        wirelessFluidTerminal = ECApi.instance().items().wirelessFluidTerminal().stack(1);
//        addInstallCost(wirelessFluidTerminal);
    }

    @Override
    public String getTextureFile() {
        return "ItemWirelessTerminalFluid"; //?
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
    public String getDescription() {
        return "An Applied Energistics ExtraCells wireless fluid terminal integrated into your power tool.";
    }

    @Override
    public void onRightClick(EntityPlayer player, World world, ItemStack item) {
        ECApi.instance().openWirelessFluidTerminal(player, player.getHeldItem(), world);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
    }
}
