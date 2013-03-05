/**
 * 
 */
package net.machinemuse.powersuits.item;

import net.machinemuse.powersuits.common.Config;
import atomicscience.api.Poison.ArmorType;
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
				0, // Texture index for rendering armor on the player
				3); // armor type. 0=head, 1=torso, 2=legs, 3=feet
		itemType = Config.Items.PowerArmorFeet;
		setItemName(itemType.idName);
		setIconIndex(63);
		LanguageRegistry.addName(this, itemType.englishName);
	}

	@Override
	public ArmorType getArmorType() {
		return ArmorType.BOOTS;
	}
}
