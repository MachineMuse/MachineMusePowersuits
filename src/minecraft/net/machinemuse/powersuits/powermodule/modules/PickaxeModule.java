package net.machinemuse.powersuits.powermodule.modules;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PickaxeModule extends PowerModuleBase {

	public PickaxeModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(new ItemStack(Item.ingotIron, 3));
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
		addBaseProperty(MuseCommonStrings.PICKAXE_ENERGY_CONSUMPTION, 50, "J");
		addBaseProperty(MuseCommonStrings.PICKAXE_HARVEST_SPEED, 8, "x");
		addTradeoffProperty("Overclock", MuseCommonStrings.PICKAXE_ENERGY_CONSUMPTION, 950);
		addTradeoffProperty("Overclock", MuseCommonStrings.PICKAXE_HARVEST_SPEED, 22);
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_TOOL;
	}

	@Override
	public String getName() {
		return MuseCommonStrings.MODULE_PICKAXE;
	}

	@Override
	public String getDescription() {
		return "Picks are good for harder materials like stone and ore.";
	}

	@Override
	public String getTextureFile() {
		return "toolpick";
	}

}
