package net.machinemuse.powersuits.powermodule.misc;

import java.util.Collection;
import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.api.electricity.ElectricItemUtils;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class HazmatModule extends PowerModuleBase {
	public static final String MODULE_HAZMAT = "Radiation Shielding";

	public HazmatModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(Config.copyAndResize(ItemComponent.basicPlating, 3)).addBaseProperty(MuseCommonStrings.WEIGHT, 0.5);
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_ARMOR;
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
