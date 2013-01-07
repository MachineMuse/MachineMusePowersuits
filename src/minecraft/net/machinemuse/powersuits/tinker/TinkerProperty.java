package net.machinemuse.powersuits.tinker;

import net.machinemuse.general.MuseStringUtils;
import net.minecraft.item.ItemStack;

public class TinkerProperty {
	public String label;
	public String units;
	public ITinkerPropertyCallback callback;
	
	public TinkerProperty(String label, String units,
			ITinkerPropertyCallback callback) {
		this.label = label;
		this.units = units;
		this.callback = callback;
	}
	
	public String getString(ItemStack stack) {
		return label
				+ ":\t"
				+ MuseStringUtils.formatNumberShort(callback.getValue(stack))
				+ units;
	}
	
}
