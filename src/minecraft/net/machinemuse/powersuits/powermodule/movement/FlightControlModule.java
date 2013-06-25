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
    public static String MODULE_FLIGHT_CONTROL;

    public static final String FLIGHT_VERTICALITY = "Y-look ratio";

    public FlightControlModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        addTradeoffProperty("Verticality", FLIGHT_VERTICALITY, 1.0, "%");
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
    public String getName() {
        MODULE_FLIGHT_CONTROL = StatCollector.translateToLocal("module.flightControl.name");
        return MODULE_FLIGHT_CONTROL;
    }

    @Override
    public String getDescription() {
        return "An integrated control circuit to help you fly better. Press Z to go down.";
    }

}
