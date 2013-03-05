package net.machinemuse.powersuits.item;

import net.machinemuse.powersuits.common.Config;
import atomicscience.api.Poison.ArmorType;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemPowerArmorTorso extends ItemPowerArmor {
	public ItemPowerArmorTorso() {
		super(Config.getAssignedItemID(Config.Items.PowerArmorTorso), // itemID
				0, // Texture index for rendering armor on the player
				1); // armor type. 0=head, 1=torso, 2=legs, 3=feet
		itemType = Config.Items.PowerArmorTorso;
		setItemName(itemType.idName);
		setIconIndex(31);
		LanguageRegistry.addName(this, itemType.englishName);
	}

	@Override
	public ArmorType getArmorType() {
		return ArmorType.BODY;
	}
}
