package net.machinemuse.powersuits.item;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemComponent extends Item {
	public static List<Integer> icons;
	public static List<String> names;

	public static ItemStack wiring;
	public static ItemStack solenoid;
	public static ItemStack servoMotor;
	public static ItemStack gliderWing;
	public static ItemStack ionThruster;
	public static ItemStack parachute;
	public static ItemStack capacitor;

	public ItemComponent() {
		super(Config.getAssignedItemID(Config.Items.PowerArmorComponent));
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(Config.getCreativeTab());
		icons = new ArrayList();
		names = new ArrayList();
	}

	public ItemStack addComponent(String name, MuseIcon icon) {
		names.add(name);
		icons.add(icon.getIconIndex());
		this.setTextureFile(icon.getTexturefile());
		ItemStack stack = new ItemStack(this, 1, names.size() - 1);
		LanguageRegistry.addName(stack, name);
		return stack;
	}

	public void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
		currentTipList.add(
				MuseStringUtils.wrapMultipleFormatTags(
						"Power Armor Component",
						MuseStringUtils.FormatCodes.Grey,
						MuseStringUtils.FormatCodes.Italic));
	}

	public void populate() {
		wiring = addComponent("Wiring", MuseIcon.COMPONENT_WIRING);
		solenoid = addComponent("Solenoid", MuseIcon.COMPONENT_SOLENOID);
		servoMotor = addComponent("Servo Motor", MuseIcon.COMPONENT_SERVOMOTOR);
		gliderWing = addComponent("Glider Wing", MuseIcon.COMPONENT_GLIDERWING);
		ionThruster = addComponent("Ion Thruster", MuseIcon.COMPONENT_IONTHRUSTER);
		capacitor = addComponent("Capacitor", MuseIcon.COMPONENT_CAPACITOR);
		parachute = addComponent("Parachute", MuseIcon.COMPONENT_PARACHUTE);
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	public int getIconFromDamage(int index)
	{
		return icons.get(index);
	}

	public String getItemNameIS(ItemStack par1ItemStack)
	{
		int index = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, names.size() - 1);
		return "powerArmorComponent." + names.get(index).replaceAll("\\s", "");
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye
	 * returns 16 items). For creative tab.
	 */
	public void getSubItems(int itemID, CreativeTabs tab, List listToAddTo)
	{
		for (int i = 0; i < names.size() - 1; ++i)
		{
			listToAddTo.add(new ItemStack(itemID, 1, i));
		}
	}
}
