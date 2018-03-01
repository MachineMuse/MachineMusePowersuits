package net.machinemuse.numina.api.nbt;

import net.minecraft.nbt.NBTTagCompound;

@Deprecated
public class PropertyModifierFlatAdditiveByte implements IPropertyModifierByte {
    public byte valueAdded;

    public PropertyModifierFlatAdditiveByte(byte valueAdded) {
        this.valueAdded = valueAdded;
    }

    // Note that byte plus byte usually returns an integer probably because the data types are so small;
    @Override
    public Byte applyModifier(NBTTagCompound moduleTag, Byte value) {
        return (byte)(value + this.valueAdded);
    }
}