package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.ModCompatability;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.electricity.ElectricityPack;

import java.util.List;

public class MultimeterModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_MULTIMETER = "Multimeter";

    public MultimeterModule(String name, List<IModularItem> validItems) {
        super(name, validItems); // Add UE multimeter module
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.wiring, 2)).addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));

    }

    public MultimeterModule(List<IModularItem> validItems) {
        super(validItems);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getName() {
        return MODULE_MULTIMETER;
    }

    @Override
    public String getDescription() {
        return "A tool addon that reads the Universal Electricity power generation in a wire.";
    }

    @Override
    public String getTextureFile() {
        return "redplate";
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World worldObj, int x, int y, int z, int side, float hitX, float hitY,
                                  float hitZ) {
        if (ModCompatability.isBasicComponentsLoaded()) {
            if (!worldObj.isRemote) {
                TileEntity tileEntity = worldObj.getBlockTileEntity(x, y, z);
                if (tileEntity instanceof IConductor) {
                    IConductor wireTile = (IConductor) tileEntity;
                    ElectricityPack getProduced = wireTile.getNetwork().getProduced();
                    player.addChatMessage("Reading: " + ElectricityDisplay.getDisplay(getProduced.amperes, ElectricUnit.AMPERE) + ", "
                            + ElectricityDisplay.getDisplay(getProduced.voltage, ElectricUnit.VOLTAGE) + ", "
                            + ElectricityDisplay.getDisplay(getProduced.getWatts() * 20, ElectricUnit.WATT));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onRightClick(EntityPlayer playerClicking, World world, ItemStack item) {
    }

    @Override
    public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
    }

}
