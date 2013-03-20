package net.machinemuse.powersuits.powermodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.IPropertyModifier;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;

public abstract class PowerModuleBase implements IPowerModule {
	protected List<ItemStack> installCost;
	protected List<IModularItem> validItems;
	protected Map<String, List<IPropertyModifier>> propertyModifiers;
	protected static Map<String, String> units = new HashMap();
	protected NBTTagCompound defaultTag;
	protected boolean isAllowed;
	protected Icon icon;

	public PowerModuleBase(String name, List<IModularItem> validItems) {
		this.validItems = validItems;
		this.installCost = new ArrayList();
		this.propertyModifiers = new HashMap();
		this.defaultTag = new NBTTagCompound();
		this.defaultTag.setBoolean(MuseItemUtils.ONLINE, true);
		this.isAllowed = Config.getConfig().get("Modules", name, true).getBoolean(true);
	}

	public PowerModuleBase(List<IModularItem> validItems) {
		this.validItems = validItems;
		this.installCost = new ArrayList();
		this.propertyModifiers = new HashMap();
		this.defaultTag = new NBTTagCompound();
		this.defaultTag.setBoolean(MuseItemUtils.ONLINE, true);
		this.isAllowed = Config.getConfig().get("Modules", getName(), true).getBoolean(true);
	}

	@Override
	public Icon getIcon(ItemStack item) {
		return icon;
	}

	@Override
	public void registerIcon(IconRegister register) {
		this.icon = register.registerIcon(MuseIcon.ICON_PREFIX + getTextureFile());
	}

	public abstract String getTextureFile();

	@Override
	public List<ItemStack> getInstallCost() {
		return installCost;
	}

	public PowerModuleBase addInstallCost(ItemStack stack) {
		this.installCost.add(stack);
		return this;
	}

	@Override
	public boolean isValidForItem(ItemStack stack, EntityPlayer player) {
		if (this.validItems.contains(stack.getItem())) {
			return true;
		} else {
			return false;
		}
	}

	public Map<String, List<IPropertyModifier>> getPropertyModifiers() {
		return propertyModifiers;
	}

	public double applyPropertyModifiers(NBTTagCompound itemTag, String propertyName, double propertyValue) {
		if (propertyModifiers.containsKey(propertyName) && itemTag.hasKey(this.getName())) {
			NBTTagCompound moduleTag = itemTag.getCompoundTag(this.getName());
			for (IPropertyModifier modifier : propertyModifiers.get(propertyName)) {
				propertyValue = modifier.applyModifier(moduleTag, propertyValue);
			}
		}
		return propertyValue;
	}

	@Override
	public NBTTagCompound getNewTag() {
		return (NBTTagCompound) defaultTag.copy();
	}

	@Override
	public boolean isAllowed() {
		return this.isAllowed;
	}

	public PowerModuleBase addTradeoffProperty(String tradeoffName, String propertyName, double multiplier) {
		double propFromConfig = Config.getConfig().get("Properties", getName() + "." + propertyName + "." + tradeoffName + ".multiplier", multiplier)
				.getDouble(multiplier);
		return addPropertyModifier(propertyName, new PropertyModifierLinearAdditive(tradeoffName, propFromConfig));
	}

	public PowerModuleBase addTradeoffProperty(String tradeoffName, String propertyName, double multiplier, String unit) {
		double propFromConfig = Config.getConfig().get("Properties", getName() + "." + propertyName + "." + tradeoffName + ".multiplier", multiplier)
				.getDouble(multiplier);
		units.put(propertyName, unit);
		return addPropertyModifier(propertyName, new PropertyModifierLinearAdditive(tradeoffName, propFromConfig));
	}

	public PowerModuleBase addPropertyModifier(String propertyName, IPropertyModifier modifier) {
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

	public PowerModuleBase addSimpleTradeoff(IPowerModule module, String tradeoffName, String firstPropertyName, String firstUnits,
			double firstPropertyBase, double firstPropertyMultiplier, String secondPropertyName, String secondUnits, double secondPropertyBase,
			double secondPropertyMultiplier) {
		this.addBaseProperty(firstPropertyName, firstPropertyBase, firstUnits);
		this.addTradeoffProperty(tradeoffName, firstPropertyName, firstPropertyMultiplier);
		this.addBaseProperty(secondPropertyName, secondPropertyBase, secondUnits);
		this.addTradeoffProperty(tradeoffName, secondPropertyName, secondPropertyMultiplier);
		return this;
	}

	public PowerModuleBase addBaseProperty(String propertyName, double baseVal) {
		double propFromConfig = Config.getConfig().get("Properties", getName() + "." + propertyName + ".base", baseVal).getDouble(baseVal);
		return addPropertyModifier(propertyName, new PropertyModifierFlatAdditive(propFromConfig));
	}

	public PowerModuleBase addBaseProperty(String propertyName, double baseVal, String unit) {
		double propFromConfig = Config.getConfig().get("Properties", getName() + "." + propertyName + ".base", baseVal).getDouble(baseVal);
		units.put(propertyName, unit);
		return addPropertyModifier(propertyName, new PropertyModifierFlatAdditive(propFromConfig));
	}

	public boolean equals(PowerModule other) {
		return other != null && other.getName() == this.getName();
	}
}
