package net.machinemuse.powersuits.item;

import micdoodle8.mods.galacticraft.API.EnumGearType;
import micdoodle8.mods.galacticraft.API.IBreathableArmor;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.powermodule.misc.AirtightSealModule;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPowerArmorHelmet extends ItemPowerArmor implements IBreathableArmor {
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

	@Override
	public boolean canBreathe(ItemStack helmetInSlot, EntityPlayer playerWearing, EnumGearType type) {
		return MuseItemUtils.itemHasActiveModule(helmetInSlot, AirtightSealModule.AIRTIGHT_SEAL_MODULE);
	}
}
