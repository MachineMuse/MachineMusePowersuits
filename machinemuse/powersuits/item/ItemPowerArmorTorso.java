package machinemuse.powersuits.item;

import java.util.ArrayList;

import machinemuse.powersuits.common.Config;
import net.minecraft.item.EnumArmorMaterial;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemPowerArmorTorso extends ItemPowerArmor {
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
		validAugTypes = new ArrayList<String>();
		validAugTypes.add("Battery");
		validAugTypes.add("Armor Plating");
	}
}
