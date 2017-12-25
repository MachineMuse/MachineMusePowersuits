package net.machinemuse.numina.api.item;

import net.machinemuse.numina.api.nbt.IPropertyModifier;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

public interface IModule {
    List<ItemStack> getInstallCost();

    String getCategory();

    boolean isValidForItem(ItemStack stack);

    String getDataName();

    double applyPropertyModifiers(NBTTagCompound itemTag, String propertyName, double propertyValue);

    NBTTagCompound getNewTag();

    Map<String, List<IPropertyModifier>> getPropertyModifiers();

    boolean isAllowed();

    @SideOnly(Side.CLIENT)
    TextureAtlasSprite getIcon(ItemStack stack);

    @SideOnly(Side.CLIENT)
    String getStitchedTexture(ItemStack item);
}