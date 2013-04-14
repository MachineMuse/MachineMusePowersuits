package net.machinemuse.powersuits.powermodule.misc;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;

public class HazmatModule extends PowerModuleBase {
	public static final String MODULE_HAZMAT = "Radiation Shielding";

	public HazmatModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(Config.copyAndResize(ItemComponent.basicPlating, 3)).addBaseProperty(MuseCommonStrings.WEIGHT, 0.5);
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_ENVIRONMENTAL;
	}

	@Override
	public String getName() {
		return MODULE_HAZMAT;
	}

	@Override
	public String getDescription() {
		return "Protect yourself from that pesky radiation poisoning. *Must be on every piece*";
	}

	@Override
	public String getTextureFile() {
		return "greenstar";
	}
}
