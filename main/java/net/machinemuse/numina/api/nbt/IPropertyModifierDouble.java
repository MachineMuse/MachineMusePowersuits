package net.machinemuse.numina.api.nbt;

import net.minecraft.nbt.NBTTagCompound;

public interface IPropertyModifierDouble extends IPropertyModifier<Double> {
    @Override
    Double applyModifier(NBTTagCompound moduleTag, Double value);
}
