package net.machinemuse.numina.module;

import net.machinemuse.numina.nbt.propertymodifier.IPropertyModifier;
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

    String getDataName();

    String getUnlocalizedName();

    double applyPropertyModifiers(NBTTagCompound itemTag, String propertyName, double propertyValue);

    NBTTagCompound getNewTag();

    Map<String, List<IPropertyModifier>> getPropertyModifiers();

    boolean isAllowed();
}