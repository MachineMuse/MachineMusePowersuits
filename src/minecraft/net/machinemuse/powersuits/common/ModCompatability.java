package net.machinemuse.powersuits.common;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Loader;

public class ModCompatability {

	public static boolean isGregTechLoaded() {
		return Loader.isModLoaded("GregTech_Addon");
	}

	public static boolean isBasicComponentsLoaded() {
		return Loader.isModLoaded("BasicComponents");
	}
	
	public static boolean isIndustrialCraftLoaded() {
		return Loader.isModLoaded("IC2");
	}
	public static boolean isThaumCraftLoaded() {
		return Loader.isModLoaded("Thaumcraft");
	}

	public static boolean enableThaumGogglesModule() {
		boolean defaultval = isThaumCraftLoaded();
		return Config.getConfig().get("Special Modules", "Thaumcraft Goggles Module", defaultval).getBoolean(defaultval);
	}
	public static boolean vanillaRecipesEnabled() {
		boolean defaultval = (!isBasicComponentsLoaded()) && (!isIndustrialCraftLoaded());
 		return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Vanilla Recipes", defaultval).getBoolean(defaultval);
	}

	public static boolean UERecipesEnabled() {
		boolean defaultval = isBasicComponentsLoaded();
		return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Universal Electricity Recipes", defaultval).getBoolean(defaultval);
	}

	public static boolean IC2RecipesEnabled() {
		boolean defaultval = isIndustrialCraftLoaded() && (!isGregTechLoaded());
		return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "IndustrialCraft Recipes", defaultval).getBoolean(defaultval);
	}
	
	public static boolean GregTechRecipesEnabled() {
		boolean defaultval = isIndustrialCraftLoaded();
		return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Gregtech Recipes", defaultval).getBoolean(defaultval);
	}
	

	public static int joulesToEU(double joules) {
		return (int) (joules / getIC2Ratio());
	}
	
	public static double joulesFromEU(int eu) {
		return getIC2Ratio() * eu;
	}
	
	public static double getIC2Ratio() {
		return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Joules per IC2 EU", 50.0).getDouble(50.0);
	}

	public static double getBCRatio() {
		return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Joules per MJ", 500.0).getDouble(500.0);
	}
	
	public static ItemStack getIC2Item(String name) {
		return ic2.api.Items.getItem(name);
	}
}
