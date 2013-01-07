package net.machinemuse.powersuits.powermodule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.machinemuse.general.gui.MuseIcon;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GenericModule {
	protected String name;
	protected String description;
	protected String category;
	protected MuseIcon icon;
	protected boolean[] validSlots;
	protected Set<String> tweaks;
	protected Set<IModuleProperty> propertyComputers;
	protected List<ItemStack> installCost;
	protected NBTTagCompound defaultTag;
	
	public GenericModule(String name, boolean[] validSlots, MuseIcon icon, String category) {
		this.name = name;
		this.validSlots = validSlots;
		this.icon = icon;
		this.category = category;
		this.tweaks = new HashSet();
		this.propertyComputers = new HashSet();
		this.installCost = new ArrayList();
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
	
	public Set<String> getTweaks() {
		return tweaks;
	}
	
	public List<ItemStack> getInstallCost() {
		return installCost;
	}
	
	public Set<IModuleProperty> getPropertyComputers() {
		return propertyComputers;
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
	public GenericModule addProperty(IModuleProperty prop) {
		this.propertyComputers.add(prop);
		return this;
	}
	public GenericModule addTweak(String tweak) {
		this.tweaks.add(tweak);
		return this;
	}
}
