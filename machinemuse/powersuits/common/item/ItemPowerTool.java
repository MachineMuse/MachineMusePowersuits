package machinemuse.powersuits.common.item;

import machinemuse.powersuits.common.Config;
import machinemuse.powersuits.common.augmentation.AugmentationTypes;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemPowerTool extends Item implements IModularItem {
	private AugmentationTypes[] validAugTypes;

	public ItemPowerTool(Config.Items item) {
		super(Config.getAssignedItemID(item)); // armor type.
		setMaxStackSize(1);
		setCreativeTab(Config.getCreativeTab());
		setIconIndex(item.iconIndex);
		setItemName(item.idName);
		LanguageRegistry.addName(this, item.englishName);
	}

	public void setValidAugTypes(AugmentationTypes[] types) {
		validAugTypes = types;
	}

	@Override
	public AugmentationTypes[] getValidAugTypes() {
		return validAugTypes;
	}

}
