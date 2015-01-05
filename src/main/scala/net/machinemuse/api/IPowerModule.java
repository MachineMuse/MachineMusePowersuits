package net.machinemuse.api;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import java.util.List;
import java.util.Map;

public interface IPowerModule {
	public abstract List<ItemStack> getInstallCost();

	public abstract IIcon getIcon(ItemStack item);

	public abstract String getStitchedTexture(ItemStack item);

	public abstract void registerIcon(IIconRegister registry);

	public abstract String getCategory();

	public abstract boolean isValidForItem(ItemStack stack);

	public abstract String getDataName();

  public abstract String getLocalizedName();

	public abstract double applyPropertyModifiers(NBTTagCompound itemTag, String propertyName, double propertyValue);

	public abstract NBTTagCompound getNewTag();

	public abstract String getDescription();

	public abstract Map<String, List<IPropertyModifier>> getPropertyModifiers();

	public abstract boolean isAllowed();

	public abstract boolean isCreativeOnly();
}
