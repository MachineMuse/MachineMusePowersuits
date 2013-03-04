package net.machinemuse.powersuits.powermodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.IPropertyModifier;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PowerModule implements IPowerModule {
	protected String name;
	protected String description;
	protected String category;
	protected MuseIcon icon;
	protected Set<String> tweaks;
	protected List<ItemStack> installCost;
	protected NBTTagCompound defaultTag;
	protected Map<String, List<IPropertyModifier>> propertyModifiers;
	protected static Map<String, String> units = new HashMap();
	protected boolean toggleable = false;
	protected List<IModularItem> validItems;
	protected boolean isActive = false;
	protected boolean isAllowed;

	public PowerModule(String name, List<IModularItem> validItems, MuseIcon icon, String category) {
		this.name = name;
		this.validItems = validItems;
		this.icon = icon;
		this.category = category;
		this.tweaks = new HashSet();
		this.propertyModifiers = new HashMap();
		this.installCost = new ArrayList();
		this.defaultTag = new NBTTagCompound();
		this.defaultTag.setBoolean(MuseItemUtils.ONLINE, true);
		boolean isModuleEnabled = Config.getConfig().get("Modules", name, true).getBoolean(true);
		this.isAllowed = isModuleEnabled;
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

	@Override
	public MuseIcon getIcon(ItemStack item) {
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
		double propFromConfig = Config.getConfig().get("Properties", getName() + "." + propertyName + "." + tradeoffName + ".multiplier", multiplier)
				.getDouble(multiplier);
		return addPropertyModifier(propertyName, new PropertyModifierLinearAdditive(tradeoffName, propFromConfig));
	}

	public PowerModule addTradeoffProperty(String tradeoffName, String propertyName, double multiplier, String unit) {
		double propFromConfig = Config.getConfig().get("Properties", getName() + "." + propertyName + "." + tradeoffName + ".multiplier", multiplier)
				.getDouble(multiplier);
		units.put(propertyName, unit);
		return addPropertyModifier(propertyName, new PropertyModifierLinearAdditive(tradeoffName, propFromConfig));
	}

	public static String getUnit(String propertyName) {
		if (units.containsKey(propertyName)) {
			return units.get(propertyName);
		} else {
			return "";
		}
	}

	public PowerModule addBaseProperty(String propertyName, double baseVal) {
		double propFromConfig = Config.getConfig().get("Properties", getName() + "." + propertyName + ".base", baseVal).getDouble(baseVal);
		return addPropertyModifier(propertyName, new PropertyModifierFlatAdditive(propFromConfig));
	}

	public PowerModule addBaseProperty(String propertyName, double baseVal, String unit) {
		double propFromConfig = Config.getConfig().get("Properties", getName() + "." + propertyName + ".base", baseVal).getDouble(baseVal);
		units.put(propertyName, unit);
		return addPropertyModifier(propertyName, new PropertyModifierFlatAdditive(propFromConfig));
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

	public boolean isValidForItem(ItemStack stack, EntityPlayer player) {
		if (this.validItems.contains(stack.getItem())) {
			return true;
		} else {
			return false;
		}
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

	public PowerModule addSimpleTradeoff(
			IPowerModule module, String tradeoffName,
			String firstPropertyName, String firstUnits, double firstPropertyBase, double firstPropertyMultiplier,
			String secondPropertyName, String secondUnits, double secondPropertyBase, double secondPropertyMultiplier) {
		this.addBaseProperty(firstPropertyName, firstPropertyBase, firstUnits);
		this.addTradeoffProperty(tradeoffName, firstPropertyName, firstPropertyMultiplier);
		this.addBaseProperty(secondPropertyName, secondPropertyBase, secondUnits);
		this.addTradeoffProperty(tradeoffName, secondPropertyName, secondPropertyMultiplier);
		return this;
	}

	public PowerModule setIsActive(boolean value) {
		this.isActive = value;
		return this;
	}

	@Override
	public boolean isActive() {
		return this.isActive;
	}

	@Override
	public boolean isAllowed() {
		return this.isAllowed;
	}

}
