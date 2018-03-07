package net.machinemuse.numina.api.module;

import net.machinemuse.numina.api.nbt.IPropertyModifier;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Map;

public interface IModule {

	EnumModuleTarget getTarget();

	List<ItemStack> getInstallCost();

	String getCategory();

	boolean isValidForItem(ItemStack stack);

	NBTTagCompound getNewTag();

	boolean isAllowed();

	String getUnlocalizedName();

	Map<String, List<IPropertyModifier>> getPropertyModifiers();

	int applyPropertyModifiersInt(NBTTagCompound itemTag, String propertyName, int propertyValue);

	double applyPropertyModifiersDouble(NBTTagCompound itemTag, String propertyName, double propertyValue);
}