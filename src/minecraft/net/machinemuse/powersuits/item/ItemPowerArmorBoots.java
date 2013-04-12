/**
 * 
 */
package net.machinemuse.powersuits.item;

import net.machinemuse.general.gui.MuseIcon;
import net.minecraft.client.renderer.texture.IconRegister;
import atomicscience.api.poison.Poison.ArmorType;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author MachineMuse
 * 
 */
public class ItemPowerArmorBoots extends ItemPowerArmor {
	public static int assignedItemID;

	/**
	 * @param item
	 */
	public ItemPowerArmorBoots() {
		super(assignedItemID, // itemID
				0, // Texture index for rendering armor on the player
				3); // armor type. 0=head, 1=torso, 2=legs, 3=feet
		setUnlocalizedName("powerArmorBoots");
		LanguageRegistry.addName(this, "Power Armor Boots");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		MuseIcon.ARMOR_FEET.register(iconRegister);
		itemIcon = MuseIcon.ARMOR_FEET.getIconRegistration();
	}

	@Override
	public ArmorType getArmorType() {
		return ArmorType.BOOTS;
	}

}
