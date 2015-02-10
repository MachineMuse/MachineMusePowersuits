package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.util.StatCollector;

import java.util.List;

public class FlightControlModule extends PowerModuleBase implements IToggleableModule {
    public static final String MODULE_FLIGHT_CONTROL = "Flight Control";
    public static final String FLIGHT_VERTICALITY = "Y-look ratio";

    public FlightControlModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        addTradeoffProperty("Verticality", FLIGHT_VERTICALITY, 1.0, "%");
        addPropertyLocalString("Verticality", StatCollector.translateToLocal("module.flightControl.verticality"));
        addPropertyLocalString(FLIGHT_VERTICALITY, StatCollector.translateToLocal("module.flightControl.ratio"));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_SPECIAL;
    }

    @Override
    public String getTextureFile() {
        return "FlightControlY";
    }

    @Override
    public String getDataName() {
        return MODULE_FLIGHT_CONTROL;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.flightControl.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("module.flightControl.desc");
    }

}
