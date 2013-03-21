package net.machinemuse.powersuits.powermodule.modules;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;

public class FlightControlModule extends PowerModuleBase implements IToggleableModule {
	public static final String MODULE_FLIGHT_CONTROL = "Flight Control";

	public static final String TEXTURE_FILE = "bluephaser";

	public FlightControlModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_SPECIAL;
	}

	@Override
	public String getTextureFile() {
		return "bluephaser";
	}

	@Override
	public String getName() {
		return MODULE_FLIGHT_CONTROL;
	}

	@Override
	public String getDescription() {
		return "An integrated control circuit to help you fly better. Press Z to go down.";
	}

}
