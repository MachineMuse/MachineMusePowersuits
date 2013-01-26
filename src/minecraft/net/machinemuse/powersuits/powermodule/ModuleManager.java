package net.machinemuse.powersuits.powermodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.machinemuse.powersuits.item.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ModuleManager {

	protected static final Map<String, PowerModule> moduleMap = new HashMap();
	protected static final List<PowerModule> moduleList = new ArrayList();

	public static List<PowerModule> getAllModules() {
		return moduleList;
	}

	public static PowerModule getModule(String key) {
		return moduleMap.get(key);
	}

	public static void addModule(PowerModule module) {
		moduleMap.put(module.getName(), module);
		moduleList.add(module);
	}

	public static double computeModularProperty(ItemStack stack, String propertyName) {
		double propertyValue = 0;
		NBTTagCompound itemTag = ItemUtils.getMuseItemTag(stack);
		for (PowerModule module : moduleList) {
			if (ItemUtils.isModuleActive(itemTag, module.getName())) {
				propertyValue = module.applyPropertyModifiers(itemTag, propertyName, propertyValue);
			}
		}
		return propertyValue;
	}

	public static void addSimpleTradeoff(
			PowerModule module, String tradeoffName,
			String firstPropertyName, String firstUnits, double firstPropertyBase, double firstPropertyMultiplier,
			String secondPropertyName, String secondUnits, double secondPropertyBase, double secondPropertyMultiplier) {
		module.addBaseProperty(firstPropertyName, firstPropertyBase, firstUnits);
		module.addTradeoffProperty(tradeoffName, firstPropertyName, firstPropertyMultiplier);
		module.addBaseProperty(secondPropertyName, secondPropertyBase, secondUnits);
		module.addTradeoffProperty(tradeoffName, secondPropertyName, secondPropertyMultiplier);
	}
}
