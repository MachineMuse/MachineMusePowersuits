package machinemuse.powersuits.common.item;

import machinemuse.powersuits.common.Config;
import machinemuse.powersuits.common.augmentation.AugmentationTypes;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Describes the modular power tool.
 * 
 * @author MachineMuse
 */
public class ItemPowerTool extends Item implements IModularItem {
	private AugmentationTypes[] validAugTypes;

	/**
	 * Constructor. Takes information from the Config.Items enum.
	 * 
	 * @param item
	 */
	public ItemPowerTool(Config.Items item) {
		super(Config.getAssignedItemID(item)); // armor type.
		setMaxStackSize(1);
		setCreativeTab(Config.getCreativeTab());
		setIconIndex(item.iconIndex);
		setItemName(item.idName);
		LanguageRegistry.addName(this, item.englishName);
	}

	/**
	 * For IModularItem's aug-list functionality.
	 * 
	 * @param types
	 */
	public void setValidAugTypes(AugmentationTypes[] types) {
		validAugTypes = types;
	}

	/**
	 * Inherited from IModularItem, returns a (potentially sparse) array of
	 * valid augmentations for this item.
	 */
	@Override
	public AugmentationTypes[] getValidAugTypes() {
		return validAugTypes;
	}

}
