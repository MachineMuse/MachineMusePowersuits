package net.machinemuse.powersuits.powermodule;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.powersuits.gui.MuseIcon;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GenericModule {
	protected String name;
	protected String description;
	protected String category;
	protected MuseIcon icon;
	protected boolean[] validSlots;
	protected List<ItemStack> installCost;
	protected List<ItemStack> salvageRefund;
	protected List<TinkerTradeoff> tradeoffs;
	protected NBTTagCompound defaultTag;

	public GenericModule(String name,
			boolean[] validSlots) {
		this.name = name;
		this.validSlots = validSlots;
		this.installCost = new ArrayList();
		this.salvageRefund = new ArrayList();
		this.tradeoffs = new ArrayList();
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

	public List<ItemStack> getSalvageRefund() {
		return salvageRefund;
	}

	public List<TinkerTradeoff> getTradeoffs() {
		return tradeoffs;
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

	public GenericModule addSalvageRefund(ItemStack stack) {
		this.salvageRefund.add(stack);
		return this;
	}

	public GenericModule addTradeoff(TinkerTradeoff tradeoff) {
		this.tradeoffs.add(tradeoff);
		return this;
	}
}
