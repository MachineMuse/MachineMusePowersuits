package net.machinemuse.powersuits.common;

import java.lang.reflect.Field;

import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.item.ModularCommon;
import net.machinemuse.powersuits.powermodule.ModuleManager;
import net.machinemuse.powersuits.powermodule.PowerModule;
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
		boolean defaultval = isGregTechLoaded();
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

	public static void registerModSpecificModules() {
		getIC2Ratio();
		if (ModCompatability.isThaumCraftLoaded() && ModCompatability.enableThaumGogglesModule()) {
			Class tcItems;
			ItemStack gogglesStack = null;
			try {
				tcItems = Class.forName("thaumcraft.common.Config");
				Field itemGoggles = tcItems.getField("itemGoggles");
				Item goggles = (Item) itemGoggles.get(itemGoggles);
				gogglesStack = new ItemStack(goggles);
				PowerModule module = new PowerModule("Aurameter", new boolean[] { true, false, false, false, false }, new MuseIcon(
						"/thaumcraft/resources/ss_core.png", 144), ModularCommon.CATEGORY_COSMETIC)
						.setDescription(
								"Connect up some Thaumic goggles to show the nearby aura values. (Does not reveal aura nodes, only shows the HUD)")
						.addInstallCost(ItemComponent.laserHologram.copy())
						.addInstallCost(gogglesStack);
				ModuleManager.addModule(module);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
