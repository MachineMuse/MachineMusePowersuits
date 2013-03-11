package net.machinemuse.api;

import java.util.List;
import java.util.Map;

import net.machinemuse.general.gui.MuseIcon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IPowerModule {
	public abstract List<ItemStack> getInstallCost();

	public abstract MuseIcon getIcon(ItemStack item);

	public abstract String getCategory();

	public abstract boolean isValidForItem(ItemStack stack, EntityPlayer player);

	public abstract String getName();

	public abstract double applyPropertyModifiers(NBTTagCompound itemTag, String propertyName, double propertyValue);

	public abstract NBTTagCompound getNewTag();

	public abstract String getDescription();

	public abstract Map<String, List<IPropertyModifier>> getPropertyModifiers();

	public abstract boolean isAllowed();
}
