package net.machinemuse.powersuits.powermodule;

import net.minecraft.nbt.NBTTagCompound;

public class ModulePropertySimple implements IModuleProperty {
	public double base;
	public String ratioName;
	public double multiplier;
	public String name;
	public String units;
	
	public ModulePropertySimple(String name, String units, double base, String ratioName, double multiplier) {
		this.name = name;
		this.units = units;
		this.base = base;
		this.ratioName = ratioName;
		this.multiplier = multiplier;
	}
	
	@Override public double computeProperty(NBTTagCompound moduleTag) {
		double property = base;
		double ratio = 0;
		if (moduleTag.hasKey(ratioName)) {
			ratio = moduleTag.getDouble(ratioName);
		} else {
			moduleTag.setDouble(ratioName, ratio);
		}
		property += ratio * multiplier;
		return property;
	}
	
	@Override public String getName() {
		return name;
	}
	
	@Override public String getUnits() {
		return units;
	}
}
