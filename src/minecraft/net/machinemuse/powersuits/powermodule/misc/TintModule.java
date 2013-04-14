package net.machinemuse.powersuits.powermodule.misc;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;

public class TintModule extends PowerModuleBase {
	public static final String MODULE_TINT = "Custom Colour Module";
	public static final String RED_TINT = "Red Tint";
	public static final String GREEN_TINT = "Green Tint";
	public static final String BLUE_TINT = "Blue Tint";

	public TintModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.laserHologram, 1));
		addTradeoffProperty("Red Intensity", RED_TINT, 1, "%");
		addTradeoffProperty("Green Intensity", GREEN_TINT, 1, "%");
		addTradeoffProperty("Blue Intensity", BLUE_TINT, 1, "%");
	}

	@Override
	public String getTextureFile() {
		return "netherstar";
	}

	@Override
	public String getCategory() {
		return "Blah";
		// return MuseCommonStrings.CATEGORY_COSMETIC;
	}

	@Override
	public String getName() {
		return MODULE_TINT;
	}

	@Override
	public String getDescription() {
		return "Give your armor some coloured tinting to customize your armor's appearance.";
	}
}
