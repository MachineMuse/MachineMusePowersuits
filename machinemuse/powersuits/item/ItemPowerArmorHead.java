package machinemuse.powersuits.item;

import java.util.ArrayList;

import machinemuse.powersuits.augmentation.Augmentation;
import machinemuse.powersuits.augmentation.AugmentationArmorPlating;
import machinemuse.powersuits.augmentation.AugmentationBattery;
import machinemuse.powersuits.common.Config;
import net.minecraft.item.EnumArmorMaterial;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemPowerArmorHead extends ItemPowerArmor {
	public ItemPowerArmorHead() {
		super(Config.getAssignedItemID(Config.Items.PowerArmorHead), // itemID
				EnumArmorMaterial.IRON, // Material
				0, // Texture index
				0); // armor type. 0=head, 1=torso, 2=legs, 3=feet
		itemType = Config.Items.PowerArmorHead;
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

}
