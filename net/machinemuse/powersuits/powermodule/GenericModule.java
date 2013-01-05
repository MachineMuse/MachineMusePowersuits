package net.machinemuse.powersuits.powermodule;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.tinker.TinkerProperty;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GenericModule {
	protected String name;
	protected String description;
	protected String category;
	protected MuseIcon icon;
	protected boolean[] validSlots;
	protected List<ItemStack> installCost;
	protected List<String> tweaks;
	protected List<TinkerProperty> properties;
	protected NBTTagCompound defaultTag;

	public GenericModule(String name,
			boolean[] validSlots) {
		this.name = name;
		this.validSlots = validSlots;
		this.installCost = new ArrayList();
		this.properties = new ArrayList();
		this.tweaks = new ArrayList();
		this.defaultTag = new NBTTagCompound();
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

	public NBTTagCompound getNewTag() {
		return (NBTTagCompound) defaultTag.copy();
	}

	public boolean isValidForSlot(int i) {
		return validSlots[i];
	}

	public List<ItemStack> getInstallCost() {
		return installCost;
	}


	public List<TinkerProperty> getTradeoffs() {
		return properties;
	}

	public List<String> getTweaks() {
		return tweaks;
	}

	public GenericModule setDescription(String description) {
		this.description = description;
		return this;
	}

	public GenericModule setCategory(String category) {
		this.category = category;
		return this;
	}

	public GenericModule setIcon(MuseIcon icon) {
		this.icon = icon;
		return this;
	}

	public GenericModule setDefaultString(String key, String value) {
		this.defaultTag.setString(key, value);
		return this;
	}

	public GenericModule setDefaultDouble(String key, double value) {
		this.defaultTag.setDouble(key, value);
		return this;
	}

	public GenericModule addInstallCost(ItemStack stack) {
		this.installCost.add(stack);
		return this;
	}

	public GenericModule addTweak(String tweakName) {
		this.tweaks.add(tweakName);
		return this;
	}

	public GenericModule addRelevantProperty(TinkerProperty tinkerProperty) {
		this.properties.add(tinkerProperty);
		return this;
	}
}
