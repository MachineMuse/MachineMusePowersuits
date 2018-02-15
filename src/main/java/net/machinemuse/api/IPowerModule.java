package net.machinemuse.api;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Map;

public interface IPowerModule {
	List<ItemStack> getInstallCost();

	TextureAtlasSprite getIcon(ItemStack item);

	String getCategory();

	boolean isValidForItem(ItemStack stack);

	String getDataName();

	double applyPropertyModifiers(NBTTagCompound itemTag, String propertyName, double propertyValue);

	NBTTagCompound getNewTag();

	Map<String, List<IPropertyModifier>> getPropertyModifiers();

	boolean isAllowed();
}
