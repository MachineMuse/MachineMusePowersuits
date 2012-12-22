package machinemuse.powersuits.item;

import java.util.ArrayList;

import machinemuse.powersuits.augmentation.Augmentation;
import machinemuse.powersuits.augmentation.AugmentationArmorPlating;
import machinemuse.powersuits.augmentation.AugmentationBattery;
import machinemuse.powersuits.common.Config;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemPowerArmorLegs extends ItemPowerArmor {
	public ItemPowerArmorLegs() {
		super(Config.getAssignedItemID(Config.Items.PowerArmorLegs), // itemID
				EnumArmorMaterial.IRON, // Material
				0, // Texture index
				2); // armor type. 0=head, 1=torso, 2=legs, 3=feet
		itemType = Config.Items.PowerArmorLegs;
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
		validAugTypes.add(new AugmentationBattery(new NBTTagCompound()));
		validAugTypes.add(new AugmentationArmorPlating(new NBTTagCompound()));
	}

}
