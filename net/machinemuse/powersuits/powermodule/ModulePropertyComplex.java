package net.machinemuse.powersuits.powermodule;

import java.util.Map;

import net.machinemuse.general.MuseStringUtils;
import net.minecraft.nbt.NBTTagCompound;

public class ModulePropertyComplex implements IModuleProperty {
	public String name;
	public String units;
	public double base;
	public Map<String, Double> multipliers;
	
	public ModulePropertyComplex(String name, String units, double base) {
		this.name = name;
		this.units = units;
		this.base = base;
	}
	
	public void addTweak(String ratioName, double multiplier) {
		multipliers.put(ratioName, multiplier);
	}
	
	@Override public double computeProperty(NBTTagCompound moduleTag) {
		double property = base;
		for (Map.Entry<String, Double> multiplier : multipliers.entrySet()) {
			double ratio = 0;
			String key = multiplier.getKey();
			if (moduleTag.hasKey(key)) {
				ratio = moduleTag.getDouble(key);
			} else {
				moduleTag.setDouble(key, ratio);
			}
			property += ratio * multiplier.getValue();
		}
		return property;
	}
	
	@Override public String getString(NBTTagCompound moduleTag) {
		return name
				+ ":\t"
				+ MuseStringUtils.formatNumberShort(computeProperty(moduleTag))
				+ units;
	}
}
