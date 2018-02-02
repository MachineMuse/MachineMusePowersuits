package net.machinemuse.numina.api.nbt;

import net.minecraft.nbt.NBTTagCompound;

public interface IPropertyModifierInt extends IPropertyModifier<Integer> {
    @Override
    Integer applyModifier(NBTTagCompound moduleTag, Integer value);
}
