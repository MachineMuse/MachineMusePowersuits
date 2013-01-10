package net.machinemuse.powersuits.powermodule;

import java.util.HashMap;
import java.util.Map;

import net.machinemuse.powersuits.item.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ModuleManager {

	protected static final Map<String, PowerModule> allModules = new HashMap();

	public static Map<String, PowerModule> getAllModules() {
		return allModules;
	}

	public static PowerModule getModule(String key) {
		return allModules.get(key);
	}

	public static void addModule(PowerModule module) {
		allModules.put(module.getName(), module);
	}

	public static double computeModularProperty(ItemStack stack, String propertyName) {
		double propertyValue = 0;
		NBTTagCompound itemTag = ItemUtils.getMuseItemTag(stack);
		for (PowerModule module : allModules.values()) {
			propertyValue = module.applyPropertyModifiers(itemTag, propertyName, propertyValue);
		}
		return propertyValue;
	}

	public static void addSimpleTradeoff(
			PowerModule module, String tradeoffName,
			String firstPropertyName, String firstUnits, double firstPropertyBase, double firstPropertyMultiplier,
			String secondPropertyName, String secondUnits, double secondPropertyBase, double secondPropertyMultiplier) {
		module.addBaseProperty(firstPropertyName, firstPropertyBase);
		module.addTradeoffProperty(firstPropertyName, firstPropertyMultiplier, tradeoffName);
		module.addBaseProperty(secondPropertyName, secondPropertyBase);
		module.addTradeoffProperty(secondPropertyName, secondPropertyMultiplier, tradeoffName);
	}

}
