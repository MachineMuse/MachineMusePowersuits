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
	public double lowvalue;
	public double highvalue;

	/**
	 * Require a property to be between two values to use the TinkerAction this
	 * is on. For an unbounded property, you can use something ridiculous, or
	 * Double.NEGATIVE_INFINITY / Double.POSITIVE_INFINITY
	 * 
	 * @param propertyName
	 * @param lowvalue
	 * @param highvalue
	 */
	public TinkerRequirement(String propertyName,
			double lowvalue, double highvalue) {
		this.propertyName = propertyName;
		this.lowvalue = lowvalue;
		this.highvalue = highvalue;
	}

	public boolean test(NBTTagCompound properties) {
		double testvalue = ItemUtils.getDoubleOrZero(properties, propertyName);
		if (testvalue > lowvalue && testvalue < highvalue) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return String.format("%.2f", lowvalue) + " < " + propertyName + " < "
				+ String.format("%.2f", highvalue);
	}
}
