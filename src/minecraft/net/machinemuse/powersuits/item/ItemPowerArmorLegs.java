package net.machinemuse.powersuits.item;

import net.machinemuse.powersuits.common.Config;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemPowerArmorLegs extends ItemPowerArmor {
	public ItemPowerArmorLegs() {
		super(Config.getAssignedItemID(Config.Items.PowerArmorLegs), // itemID
				0, // Texture index for rendering armor on the player
				2); // armor type. 0=head, 1=torso, 2=legs, 3=feet
		itemType = Config.Items.PowerArmorLegs;
		setItemName(itemType.idName);
		setIconIndex(47);
		LanguageRegistry.addName(this, itemType.englishName);
	}

}
