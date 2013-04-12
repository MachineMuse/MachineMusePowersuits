package net.machinemuse.powersuits.item;

import net.machinemuse.general.gui.MuseIcon;
import net.minecraft.client.renderer.texture.IconRegister;
import atomicscience.api.poison.Poison.ArmorType;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPowerArmorChestplate extends ItemPowerArmor {
	public static int assignedItemID;

	public ItemPowerArmorChestplate() {
		super(assignedItemID, // itemID
				0, // Texture index for rendering armor on the player
				1); // armor type. 0=head, 1=torso, 2=legs, 3=feet
		setUnlocalizedName("powerArmorChestplate");
		LanguageRegistry.addName(this, "Power Armor Chestplate");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		MuseIcon.ARMOR_TORSO.register(iconRegister);
		itemIcon = MuseIcon.ARMOR_TORSO.getIconRegistration();
	}

	@Override
	public ArmorType getArmorType() {
		return ArmorType.BODY;
	}

}
