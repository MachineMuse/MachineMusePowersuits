package net.machinemuse.api;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import java.util.List;
import java.util.Map;

public interface IPowerModule {
    NonNullList<ItemStack> getInstallCost();

    TextureAtlasSprite getIcon(ItemStack item);

    String getCategory();

    boolean isValidForItem(ItemStack stack);

    String getDataName();

    String getUnlocalizedName();

    double applyPropertyModifiers(NBTTagCompound itemTag, String propertyName, double propertyValue);

    NBTTagCompound getNewTag();

    Map<String, List<IPropertyModifier>> getPropertyModifiers();

    boolean isAllowed();
}
