package net.machinemuse.powersuits.powermodule;

import net.machinemuse.powersuits.item.ItemUtils;
import net.minecraft.nbt.NBTTagCompound;

public class PropertyModifierLinearAdditive implements IPropertyModifier {
	protected double multiplier;
	protected String tradeoffName;

	public PropertyModifierLinearAdditive(String tradeoffName, double multiplier) {
		this.multiplier = multiplier;
		this.tradeoffName = tradeoffName;
	}

	@Override
	public double applyModifier(NBTTagCompound moduleTag, double value) {
		return value + multiplier * ItemUtils.getDoubleOrZero(moduleTag, tradeoffName);
	}

	public String getTradeoffName() {
		return tradeoffName;
	}

}
