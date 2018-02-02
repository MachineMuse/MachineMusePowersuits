package net.machinemuse.numina.api.nbt;

import net.minecraft.nbt.NBTTagCompound;

public interface IPropertyModifierByte extends IPropertyModifier<Byte> {
    @Override
    Byte applyModifier(NBTTagCompound moduleTag, Byte value);
}
