package net.machinemuse.powersuits.item;

import net.machinemuse.general.gui.MuseIcon;
import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPowerArmorHelmet extends ItemPowerArmor {
	public static int assignedItemID;

	public ItemPowerArmorHelmet() {
		super(assignedItemID, // itemID
				0, // Texture index for rendering armor on the player
				0); // armor type. 0=head, 1=torso, 2=legs, 3=feet
		LanguageRegistry.addName(this, "Power Armor Helmet");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister) {
		MuseIcon.ARMOR_HEAD.register(iconRegister);
		iconIndex = MuseIcon.ARMOR_HEAD.getIconRegistration();
	}
}
