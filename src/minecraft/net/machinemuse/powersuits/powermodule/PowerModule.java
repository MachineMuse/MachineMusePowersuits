package net.machinemuse.powersuits.powermodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.powermodule.property.IPropertyModifier;
import net.machinemuse.powersuits.powermodule.property.PropertyModifierFlatAdditive;
import net.machinemuse.powersuits.powermodule.property.PropertyModifierLinearAdditive;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PowerModule {
	protected String name;
	protected String description;
	protected String category;
	protected MuseIcon icon;
	protected boolean[] validSlots;
	protected Set<String> tweaks;
	protected List<ItemStack> installCost;
	protected NBTTagCompound defaultTag;
	protected Map<String, List<IPropertyModifier>> propertyModifiers;
	protected static Map<String, String> units = new HashMap();
	protected boolean toggleable = false;

	public PowerModule(String name, boolean[] validSlots, MuseIcon icon, String category) {
		this.name = name;
		this.validSlots = validSlots;
		this.icon = icon;
		this.category = category;
		this.tweaks = new HashSet();
		this.propertyModifiers = new HashMap();
		this.installCost = new ArrayList();
		this.defaultTag = new NBTTagCompound();
		this.defaultTag.setBoolean(ItemUtils.ACTIVE, true);
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getCategory() {
		return category;
	}

	public MuseIcon getIcon() {
		return icon;
	}

	public boolean isToggleable() {
		return toggleable;
	}

	public PowerModule setToggleable(boolean value) {
		this.toggleable = value;
		return this;
	}

	public PowerModule addTradeoffProperty(String tradeoffName, String propertyName, double multiplier) {
		return addPropertyModifier(propertyName, new PropertyModifierLinearAdditive(tradeoffName, multiplier));
	}

	public PowerModule addTradeoffProperty(String tradeoffName, String propertyName, double multiplier, String unit) {
		units.put(propertyName, unit);
		return addPropertyModifier(propertyName, new PropertyModifierLinearAdditive(tradeoffName, multiplier));
	}

	public static String getUnit(String propertyName) {
		if (units.containsKey(propertyName)) {
			return units.get(propertyName);
		} else {
			return "";
		}
	}

	public PowerModule addBaseProperty(String propertyName, double baseVal) {
		return addPropertyModifier(propertyName, new PropertyModifierFlatAdditive(baseVal));
	}

	public PowerModule addBaseProperty(String propertyName, double baseVal, String unit) {
		units.put(propertyName, unit);
		return addPropertyModifier(propertyName, new PropertyModifierFlatAdditive(baseVal));
	}

	public PowerModule addPropertyModifier(String propertyName, IPropertyModifier modifier) {
		List<IPropertyModifier> modifiers;
		if (propertyModifiers.containsKey(propertyName)) {
			modifiers = propertyModifiers.get(propertyName);
		} else {
			modifiers = new LinkedList();
			propertyModifiers.put(propertyName, modifiers);
		}
		modifiers.add(modifier);
		return this;
	}

	public Map<String, List<IPropertyModifier>> getPropertyModifiers() {
		return propertyModifiers;
	}

	public double applyPropertyModifiers(NBTTagCompound itemTag, String propertyName, double propertyValue) {
		if (propertyModifiers.containsKey(propertyName) && itemTag.hasKey(this.name)) {
			NBTTagCompound moduleTag = itemTag.getCompoundTag(this.name);
			for (IPropertyModifier modifier : propertyModifiers.get(propertyName)) {
				propertyValue = modifier.applyModifier(moduleTag, propertyValue);
			}
		}
		return propertyValue;
	}

	public NBTTagCompound getNewTag() {
		return (NBTTagCompound) defaultTag.copy();
	}

	public boolean isValidForSlot(int i) {
		return validSlots[i];
	}

	public Set<String> getTweaks() {
		return tweaks;
	}

	public List<ItemStack> getInstallCost() {
		return installCost;
	}

	public PowerModule setDescription(String description) {
		this.description = description;
		return this;
	}

	public PowerModule setCategory(String category) {
		this.category = category;
		return this;
	}

	public PowerModule setIcon(MuseIcon icon) {
		this.icon = icon;
		return this;
	}

	public PowerModule setDefaultString(String key, String value) {
		this.defaultTag.setString(key, value);
		return this;
	}

	public PowerModule setDefaultDouble(String key, double value) {
		this.defaultTag.setDouble(key, value);
		return this;
	}

	public PowerModule addInstallCost(ItemStack stack) {
		this.installCost.add(stack);
		return this;
	}

	public PowerModule addTweak(String tweak) {
		this.tweaks.add(tweak);
		return this;
	}

	public boolean equals(PowerModule other) {
		return other != null && other.name == this.name;
	}
}
