/**
 * 
 */
package machinemuse.powersuits.item;

import java.util.ArrayList;

import machinemuse.powersuits.augmentation.Augmentation;
import machinemuse.powersuits.augmentation.AugmentationArmorPlating;
import machinemuse.powersuits.augmentation.AugmentationBattery;
import machinemuse.powersuits.common.Config;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * @author MachineMuse
 * 
 */
public class ItemPowerArmorFeet extends ItemPowerArmor {
	/**
	 * @param item
	 */
	public ItemPowerArmorFeet() {
		super(Config.getAssignedItemID(Config.Items.PowerArmorFeet), // itemID
				EnumArmorMaterial.IRON, // Material
				0, // Texture index
				3); // armor type. 0=head, 1=torso, 2=legs, 3=feet
		itemType = Config.Items.PowerArmorFeet;
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
