package net.machinemuse.powersuits.tinker;

import net.machinemuse.powersuits.item.ItemUtils;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Simple class to inform a tinkering of a requirement.
 * 
 * @author MachineMuse
 */
public class TinkerRequirement {
	public String propertyName;
	public char operator;
	public double value;

	public TinkerRequirement(String propertyName, char operator, double value) {
		this.propertyName = propertyName;
		this.operator = operator;
		this.value = value;
	}

	public boolean test(NBTTagCompound properties) {
		double testvalue = ItemUtils.getDoubleOrZero(properties, propertyName);
		switch (operator) {
		case '<':
			return testvalue < value;
		case '>':
			return testvalue > value;
		case '=':
			return testvalue == value;
		default:
			throw new IllegalArgumentException(
					"Invalid operator; must be one of <, =, or >");
		}
	}

	@Override
	public String toString() {
		return propertyName + " " + operator + " "
				+ String.format("%.2f", value);
	}
}
