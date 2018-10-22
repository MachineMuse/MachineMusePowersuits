package net.machinemuse.numina.api.module;

import net.machinemuse.numina.api.nbt.IPropertyModifier;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Map;

public interface IPowerModule {
    /**
     * Returns the enum corresponding to the EntityEquipment slot that the parent item (Head, Chest... ALL.. )
     *
     * @return
     */
    EnumModuleTarget getTarget();

    TextureAtlasSprite getIcon(ItemStack item);

    EnumModuleCategory getCategory();

    boolean isValidForItem(ItemStack stack);

    String getDataName();

    String getUnlocalizedName();

    double applyPropertyModifiers(NBTTagCompound itemTag, String propertyName, double propertyValue);

    NBTTagCompound getNewTag();

    Map<String, List<IPropertyModifier>> getPropertyModifiers();

    boolean isAllowed();
}