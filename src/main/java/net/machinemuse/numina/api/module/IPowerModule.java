package net.machinemuse.api;

import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public interface IPowerModule {
	EnumModuleTarget getTarget();

	List<ItemStack> getInstallCost();

	@Deprecated
	TextureAtlasSprite getIcon(ItemStack item);

	String getCategory();

	boolean isValidForItem(@Nonnull ItemStack stack);

	String getUnlocalizedName();



	NBTTagCompound getNewTag();

	boolean isAllowed();

	Map<String, List<IPropertyModifier>> getPropertyModifiers();

	@Deprecated
	double applyPropertyModifiers(NBTTagCompound itemTag, String propertyName, double propertyValue);

	int applyPropertyModifiersInt(NBTTagCompound itemTag, String propertyName, int propertyValue);

	double applyPropertyModifiersDouble(NBTTagCompound itemTag, String propertyName, double propertyValue);
}
