package net.machinemuse.powersuits.item;

import net.machinemuse.powersuits.common.Config;
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
		setIconIndex(31);
		LanguageRegistry.addName(this, itemType.englishName);
	}

}
