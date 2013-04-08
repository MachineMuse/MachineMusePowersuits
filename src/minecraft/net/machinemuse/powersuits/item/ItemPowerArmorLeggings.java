package net.machinemuse.powersuits.item;

import net.machinemuse.general.gui.MuseIcon;
import net.minecraft.client.renderer.texture.IconRegister;
import atomicscience.api.poison.Poison.ArmorType;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPowerArmorLeggings extends ItemPowerArmor {
	public static int assignedItemID;

	public ItemPowerArmorLeggings() {
		super(assignedItemID, // itemID
				0, // Texture index for rendering armor on the player
				2); // armor type. 0=head, 1=torso, 2=legs, 3=feet
		LanguageRegistry.addName(this, "Power Armor Leggings");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister) {
		MuseIcon.ARMOR_LEGS.register(iconRegister);
		iconIndex = MuseIcon.ARMOR_LEGS.getIconRegistration();
	}

	@Override
	public ArmorType getArmorType() {
		return ArmorType.LEGGINGS;
	}

}
