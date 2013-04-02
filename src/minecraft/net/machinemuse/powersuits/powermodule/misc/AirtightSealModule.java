package net.machinemuse.powersuits.powermodule.misc;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class AirtightSealModule extends PowerModuleBase {
	public static final String AIRTIGHT_SEAL_MODULE = "Airtight Seal";

	public AirtightSealModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(new ItemStack(Block.glass));
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_ENVIRONMENTAL;
	}

	@Override
	public String getName() {
		return AIRTIGHT_SEAL_MODULE;
	}

	@Override
	public String getDescription() {
		return "Seal the suit against hostile atmospheres for venturing to other planets.";
	}

	@Override
	public String getTextureFile() {
		return "glasspane";
	}

}
