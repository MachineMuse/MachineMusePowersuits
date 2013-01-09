package net.machinemuse.powersuits.powermodule;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;

public class ModuleManager {

	private static final Map<String, GenericModule> allModules = new HashMap();
	private static final Map<String, ModularProperty> allModularProperties = new HashMap();

	public static Map<String, ModularProperty> getAllModularProperties() {
		return allModularProperties;
	}

	public static IModularProperty getModularProperty(String key) {
		return allModularProperties.get(key);
	}

	public static Map<String, GenericModule> getAllModules() {
		return allModules;
	}

	public static GenericModule getModule(String key) {
		return allModules.get(key);
	}

	public static void addModule(GenericModule module) {
		allModules.put(module.getName(), module);
	}

	public static ModularProperty loadModularProperty(String name, double defaultValue) {
		if (allModularProperties.containsKey(name)) {
			return allModularProperties.get(name);
		} else {
			ModularProperty newprop = new ModularProperty(name, defaultValue);
			allModularProperties.put(name, newprop);
			return newprop;
		}
	}

	public static double computeModularProperty(ItemStack stack, String propertyName) {
		ModularProperty propertyComputer = loadModularProperty(propertyName, 0);
		return propertyComputer.computeProperty(stack);
	}

	public static void addSimpleTradeoff(
			GenericModule module, String tradeoffName,
			String firstPropertyName, String firstUnits, double firstPropertyBase, double firstPropertyMultiplier,
			String secondPropertyName, String secondUnits, double secondPropertyBase, double secondPropertyMultiplier) {
		IModuleProperty first = new ModulePropertySimple(firstPropertyName, firstUnits, firstPropertyBase, tradeoffName, firstPropertyMultiplier);
		ModularProperty firstFull = loadModularProperty(firstPropertyName, 0);
		firstFull.addModuleWithProperty(module.getName(), first);

		IModuleProperty second = new ModulePropertySimple(secondPropertyName, secondUnits, secondPropertyBase, tradeoffName, secondPropertyMultiplier);
		ModularProperty secondFull = loadModularProperty(secondPropertyName, 0);
		secondFull.addModuleWithProperty(module.getName(), second);

		module.addTweak(tradeoffName);
		module.addProperty(first);
		module.addProperty(second);
	}

}
