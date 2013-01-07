package net.machinemuse.powersuits.item;

import net.machinemuse.powersuits.common.Config;
import net.minecraft.item.EnumArmorMaterial;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemPowerArmorHead extends ItemPowerArmor {
	public ItemPowerArmorHead() {
		super(Config.getAssignedItemID(Config.Items.PowerArmorHead), // itemID
				EnumArmorMaterial.IRON, // Material
				0, // Texture index for rendering armor on the player
				0); // armor type. 0=head, 1=torso, 2=legs, 3=feet
		itemType = Config.Items.PowerArmorHead;
		setItemName(itemType.idName);
		setIconIndex(15);
		LanguageRegistry.addName(this, itemType.englishName);
	}
	
}
