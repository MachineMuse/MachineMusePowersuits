package net.machinemuse.powersuits.common;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModule;
import net.machinemuse.powersuits.powermodule.ToggleablePowerModule;
import net.machinemuse.powersuits.powermodule.misc.AirtightSealModule;
import net.machinemuse.powersuits.powermodule.misc.HazmatModule;
import net.machinemuse.powersuits.powermodule.misc.ThaumGogglesModule;
import net.machinemuse.powersuits.powermodule.tool.MultimeterModule;
import net.minecraft.item.Item;
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

	public static boolean isThermalExpansionLoaded() {
		return Loader.isModLoaded("ThermalExpansion");
	}

	public static boolean isAndrewAddonsLoaded() {
		return Loader.isModLoaded("PowersuitAddons");
	}

	public static boolean isGalacticraftLoaded() {
		return Loader.isModLoaded("GalacticraftCore");
	}
	
	public static boolean isForestryLoaded() {
		return Loader.isModLoaded("Forestry");
	}

	public static boolean enableThaumGogglesModule() {
		boolean defaultval = isThaumCraftLoaded();
		return Config.getConfig().get("Special Modules", "Thaumcraft Goggles Module", defaultval).getBoolean(defaultval);
	}

	public static boolean vanillaRecipesEnabled() {
		boolean defaultval = (!isBasicComponentsLoaded()) && (!isIndustrialCraftLoaded());
		return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Vanilla Recipes", defaultval).getBoolean(defaultval);
	}

	private static boolean isAtomicScienceLoaded() {
		return Loader.isModLoaded("AtomicScience");
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
		boolean defaultval = isGregTechLoaded();
		return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Gregtech Recipes", defaultval).getBoolean(defaultval);
	}

	public static boolean ThermalExpansionRecipesEnabled() {
		boolean defaultval = isThermalExpansionLoaded();
		return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Thermal Expansion Recipes", defaultval).getBoolean(defaultval);
	}

	public static int joulesToEU(double joules) {
		return (int) (joules / getIC2Ratio());
	}

	public static double joulesFromEU(int eu) {
		return getIC2Ratio() * eu;
	}

	public static double getUERatio() {
		return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Energy per UEJ", 1.0).getDouble(1.0);
	}

	public static double getIC2Ratio() {
		return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Energy per IC2 EU", 0.4).getDouble(0.4);
	}

	public static double getBCRatio() {
		return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Energy per MJ", 1.0).getDouble(1.0);
	}

	// These 2 elements are basically copied from IC2 api
	private static Class Ic2Items;

	public static ItemStack getIC2Item(String name) {
		try {
			if (Ic2Items == null)
				Ic2Items = Class.forName("ic2.core.Ic2Items");

			Object ret = Ic2Items.getField(name).get(null);

			if (ret instanceof ItemStack) {
				return ((ItemStack) ret).copy();
			} else {
				return null;
			}
		} catch (Exception e) {
			MuseLogger.logError("IC2 API: Call getItem failed for " + name);

			return null;
		}
	}

	public static ItemStack getGregtechItem(int aIndex, int aAmount, int aMeta) {
		try {
			return (ItemStack) Class.forName("gregtechmod.GT_Mod")
					.getMethod("getGregTechItem", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE })
					.invoke(null, Integer.valueOf(aIndex), Integer.valueOf(aAmount), Integer.valueOf(aMeta));
		} catch (Exception e) {
		}
		return null;
	}

	public static void registerModSpecificModules() {
		// Make the IC2 energy ratio show up in config file
		getBCRatio();
		getIC2Ratio();

		// Thaumcraft
		if (ModCompatability.isThaumCraftLoaded() && ModCompatability.enableThaumGogglesModule()) {
			ModuleManager.addModule(new ThaumGogglesModule(Collections.singletonList((IModularItem) ModularPowersuits.powerArmorHead)));
		}

		IPowerModule module = new MultimeterModule(Collections.singletonList((IModularItem) ModularPowersuits.powerTool));

		// Atomic Science
		if (ModCompatability.isAtomicScienceLoaded()) {
			ModuleManager.addModule(new HazmatModule(Arrays.<IModularItem> asList(ModularPowersuits.powerArmorHead, ModularPowersuits.powerArmorTorso, ModularPowersuits.powerArmorLegs, ModularPowersuits.powerArmorFeet)));
		}
		
		// Galacticraft
		if (ModCompatability.isGalacticraftLoaded()) {
			ModuleManager.addModule(new AirtightSealModule(Collections.singletonList((IModularItem) ModularPowersuits.powerArmorHead)));
		}
	}

	public static ItemStack getThermexItem(String string, int quantity) {
		try {
			ItemStack item =
					thermalexpansion.api.item.ItemRegistry.getItem(string, quantity);
			if (item != null) {
				return item;
			}
		} catch (Exception e) {
		}
		// thermalexpansion.api.item.ItemRegistry.printItemNames();
		MuseLogger.logError("Failed to get Thermal Expansion item " + string);
		return null;
	}
}
