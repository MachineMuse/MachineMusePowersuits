package net.machinemuse.powersuits.powermodule.modules;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DiamondPickUpgradeModule extends PowerModuleBase {
	public DiamondPickUpgradeModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
		addInstallCost(new ItemStack(Item.diamond, 3));
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_SPECIAL;
	}

	@Override
	public String getName() {
		return MuseCommonStrings.MODULE_DIAMOND_PICK_UPGRADE;
	}

	@Override
	public String getDescription() {
		return "Add diamonds to allow your pickaxe module to mine Obsidian. *REQUIRES PICKAXE MODULE TO WORK*";
	}

	@Override
	public String getTextureFile() {
		return "diamondupgrade";
	}

}
