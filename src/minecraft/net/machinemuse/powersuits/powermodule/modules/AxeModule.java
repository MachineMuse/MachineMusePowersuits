package net.machinemuse.powersuits.powermodule.modules;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AxeModule extends PowerModuleBase {
	public AxeModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(new ItemStack(Item.ingotIron, 3));
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
		addBaseProperty(MuseCommonStrings.AXE_ENERGY_CONSUMPTION, 50, "J");
		addBaseProperty(MuseCommonStrings.AXE_HARVEST_SPEED, 8, "x");
		addTradeoffProperty("Overclock", MuseCommonStrings.AXE_ENERGY_CONSUMPTION, 950);
		addTradeoffProperty("Overclock", MuseCommonStrings.AXE_HARVEST_SPEED, 22);
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_TOOL;
	}

	@Override
	public String getName() {
		return MuseCommonStrings.MODULE_AXE;
	}

	@Override
	public String getDescription() {
		return "Axes are mostly for chopping trees.";
	}

	@Override
	public String getTextureFile() {
		return "toolaxe";
	}

}
