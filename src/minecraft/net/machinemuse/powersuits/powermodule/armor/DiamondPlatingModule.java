package net.machinemuse.powersuits.powermodule.armor;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;

public class DiamondPlatingModule extends PowerModuleBase {
	public static final String MODULE_DIAMOND_PLATING = "Diamond Plating";
	
	public DiamondPlatingModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.advancedPlating, 1));
		addTradeoffProperty("Plating Thickness", MuseCommonStrings.ARMOR_VALUE_PHYSICAL, 6, " Points");
		addTradeoffProperty("Plating Thickness", MuseCommonStrings.WEIGHT, 6000, "g");
	}
	
	@Override
	public String getTextureFile() {
		return "advancedplating2";
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_ARMOR;
	}

	@Override
	public String getName() {
		return MODULE_DIAMOND_PLATING;
	}

	@Override
	public String getDescription() {
		return "Advanced plating is lighter, harder, and more protective than Basic but much harder to make.";
	}
}
