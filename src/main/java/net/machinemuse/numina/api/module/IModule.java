package net.machinemuse.numina.api.module;

import net.machinemuse.numina.api.nbt.IPropertyModifier;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Map;

public interface IModule {
//    // Item transition compat
//    PowerModuleBase setRegistryName(String resourceDomain, String resourcePath);
//
//    // Item transition compat
//    PowerModuleBase setRegistryName(String registryName);
//
//    // Item transition compat
//    ResourceLocation getRegistryName();
//
//    // Item transition compat
//    PowerModuleBase setUnlocalizedName(String unlocalizedName);












	EnumModuleTarget getTarget();

	List<ItemStack> getInstallCost();

	TextureAtlasSprite getIcon(ItemStack item);

	String getCategory();

	boolean isValidForItem(ItemStack stack);

	NBTTagCompound getNewTag();

	boolean isAllowed();

	String getUnlocalizedName();

	Map<String, List<IPropertyModifier>> getPropertyModifiers();

	int applyPropertyModifiersInt(NBTTagCompound itemTag, String propertyName, int propertyValue);

	double applyPropertyModifiersDouble(NBTTagCompound itemTag, String propertyName, double propertyValue);
}