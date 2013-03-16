/**
 * 
 */
package net.machinemuse.powersuits.item;

import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
		LanguageRegistry.addName(this, itemType.englishName);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister) {
		MuseIcon.ARMOR_FEET.register(iconRegister);
		iconIndex = MuseIcon.ARMOR_FEET.getIconRegistration();
	}

}
