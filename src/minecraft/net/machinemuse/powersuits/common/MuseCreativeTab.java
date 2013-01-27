package net.machinemuse.powersuits.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class MuseCreativeTab extends CreativeTabs {
	public static final String NAME = "MMMPowerSuits";
	public static MuseCreativeTab instance;
	public static ItemStack stack;

	public static MuseCreativeTab instance() {
		if (instance == null) {
			instance = new MuseCreativeTab(CreativeTabs.getNextID(), NAME);
		}
		return instance;
	}

	public MuseCreativeTab(int id, String name) {
		super(id, name);
		LanguageRegistry.instance().addStringLocalization("itemGroup." + NAME, "en_US", "MMMPowersuits");
	}

	@Override
	public ItemStack getIconItemStack() {
		if (stack == null) {
			stack = new ItemStack(ModularPowersuits.powerArmorHead);
		}
		return stack;
	}

}
