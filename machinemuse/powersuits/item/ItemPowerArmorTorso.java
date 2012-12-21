package machinemuse.powersuits.item;

import java.util.ArrayList;
import java.util.List;

import machinemuse.powersuits.augmentation.Augmentation;
import machinemuse.powersuits.augmentation.AugmentationArmorPlating;
import machinemuse.powersuits.augmentation.AugmentationBattery;
import machinemuse.powersuits.common.Config;
import net.minecraft.item.EnumArmorMaterial;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemPowerArmorTorso extends ItemPowerArmor {
	protected static List<Augmentation> validAugTypes;

	public ItemPowerArmorTorso() {
		super(Config.getAssignedItemID(Config.Items.PowerArmorTorso), // itemID
				EnumArmorMaterial.IRON, // Material
				0, // Texture index
				1); // armor type.
		itemType = Config.Items.PowerArmorTorso;
		setItemName(itemType.idName);
		setValidAugTypes();

		LanguageRegistry.addName(this, itemType.englishName);
	}

	/**
	 * For IModularItem's aug-list functionality.
	 * 
	 * @param types
	 */
	public void setValidAugTypes() {
		validAugTypes = new ArrayList<Augmentation>();
		validAugTypes.add(new AugmentationBattery());
		validAugTypes.add(new AugmentationArmorPlating());
	}

	/**
	 * Inherited from IModularItem, returns a (potentially sparse) array of
	 * valid augmentations for this item.
	 */
	@Override
	public List<Augmentation> getValidAugs() {
		return validAugTypes;
	}

}
