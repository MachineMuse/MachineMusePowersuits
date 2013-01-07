package net.machinemuse.powersuits.powermodule;

import java.util.*;
import java.util.Map.Entry;

import net.machinemuse.powersuits.item.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ModularProperty implements IModularProperty {
	protected Map<String, List<IModuleProperty>> modulesWithProperty;
	protected String name;
	protected double baseValue;
	
	public ModularProperty(String name, double baseValue) {
		this.name = name;
		this.baseValue = baseValue;
		this.modulesWithProperty = new HashMap();
	}
	
	@Override public double computeProperty(ItemStack stack) {
		double value = baseValue;
		NBTTagCompound itemTag = ItemUtils.getMuseItemTag(stack);
		for (Entry<String, List<IModuleProperty>> entry : modulesWithProperty.entrySet()) {
			if (itemTag.hasKey(entry.getKey())) {
				for (IModuleProperty property : entry.getValue()) {
					NBTTagCompound moduleTag = itemTag.getCompoundTag(entry.getKey());
					value += property.computeProperty(moduleTag);
				}
			}
		}
		return value;
	}
	
	public void addModuleWithProperty(String moduleName, IModuleProperty property) {
		if (!modulesWithProperty.containsKey(moduleName)) {
			modulesWithProperty.put(moduleName, new LinkedList());
		}
		List<IModuleProperty> moduleProperties = modulesWithProperty.get(moduleName);
		moduleProperties.add(property);
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getBaseValue() {
		return baseValue;
	}
	
	public void setBaseValue(double baseValue) {
		this.baseValue = baseValue;
	}
}
