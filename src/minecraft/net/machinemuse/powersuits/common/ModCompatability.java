package net.machinemuse.powersuits.common;

import java.lang.reflect.Field;
import java.util.Arrays;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModule;
import net.machinemuse.powersuits.powermodule.RightClickPowerModule;
import net.machinemuse.powersuits.powermodule.ToggleablePowerModule;
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

	public static double getIC2Ratio() {
		return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Joules per IC2 EU", 0.4).getDouble(0.4);
	}

	public static double getBCRatio() {
		return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Joules per MJ", 1.0).getDouble(1.0);
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
			System.out.println("IC2 API: Call getItem failed for " + name);

			return null;
		}
	}

	public static ItemStack getGregtechItem(int aIndex, int aAmount, int aMeta) {
		try {
			return (ItemStack) Class.forName("gregtechmod.GT_Mod")
					.getMethod("getGregTechItem", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE })
					.invoke(null, new Object[] { Integer.valueOf(aIndex), Integer.valueOf(aAmount), Integer.valueOf(aMeta) });
		} catch (Exception e) {
		}
		return null;
	}

	public static void registerModSpecificModules() {
		// Make the IC2 energy ratio show up in config file
		getBCRatio();
		getIC2Ratio();

		// Add thaumgoggles module
		if (ModCompatability.isThaumCraftLoaded() && ModCompatability.enableThaumGogglesModule()) {
			Class tcItems;
			ItemStack gogglesStack = null;
			try {
				tcItems = Class.forName("thaumcraft.common.Config");
				Field itemGoggles = tcItems.getField("itemGoggles");
				Item goggles = (Item) itemGoggles.get(itemGoggles);
				gogglesStack = new ItemStack(goggles);
				IPowerModule module = new PowerModule("Aurameter", Arrays.asList((IModularItem) ModularPowersuits.powerArmorHead), new MuseIcon(
						"/thaumcraft/resources/ss_core.png", 144), MuseCommonStrings.CATEGORY_SPECIAL)
						.setDescription(
								"Connect up some Thaumic goggles to show the nearby aura values. (Does not reveal aura nodes, only shows the HUD)")
						.addInstallCost(ItemComponent.laserHologram.copy())
						.addInstallCost(gogglesStack);
				ModuleManager.addModule(module);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		// Add UE multimeter module
		if (ModCompatability.isBasicComponentsLoaded()) {
			IPowerModule module = new RightClickPowerModule(MuseCommonStrings.MODULE_MULTIMETER,
					Arrays.asList((IModularItem) ModularPowersuits.powerTool),
					MuseIcon.PLATE_2_RED, MuseCommonStrings.CATEGORY_TOOL)
					.setDescription("A tool addon that reads the Universal Electricity power generation in a wire.")
					.addInstallCost(Config.copyAndResize(ItemComponent.wiring, 2))
					.addInstallCost(Config.copyAndResize(ItemComponent.solenoid, 1));
			ModuleManager.addModule(module);
		}

		if (ModCompatability.isAtomicScienceLoaded()) {

			IPowerModule module = new ToggleablePowerModule(MuseCommonStrings.MODULE_HAZMAT,
					Arrays.asList((IModularItem) ModularPowersuits.powerArmorHead, (IModularItem) ModularPowersuits.powerArmorTorso,
							(IModularItem) ModularPowersuits.powerArmorLegs, (IModularItem) ModularPowersuits.powerArmorFeet),
					MuseIcon.FIELD_EMITTER_GREEN, MuseCommonStrings.CATEGORY_ARMOR)
					.setDescription("Protect yourself from that pesky radiation poisoning. *Must be on every piece*")
					.addInstallCost(Config.copyAndResize(ItemComponent.basicPlating, 3))
					.addBaseProperty(MuseCommonStrings.WEIGHT, 0.5);
			ModuleManager.addModule(module);
		}

	}

	public static ItemStack getThermexItem(String string, int quantity) {
		try {
			ItemStack item = thermalexpansion.api.core.ItemRegistry.getItem(string, quantity);
			if (item != null) {
				return item;
			}
		} catch (Exception e) {
		}
		thermalexpansion.api.core.ItemRegistry.printItemNames();
		MuseLogger.logError("Failed to get Thermal Expansion item " + string);
		return null;
	}
}
