package net.machinemuse.numina.api.nbt;

import net.minecraft.nbt.NBTTagCompound;

public class PropertyModifierFlatAdditiveInteger implements IPropertyModifierInteger {
    public int valueAdded;

    public PropertyModifierFlatAdditiveInteger(int valueAdded) {
        this.valueAdded = valueAdded;
    }

    @Override
    public Integer applyModifier(NBTTagCompound moduleTag, Integer value) {
        return value + this.valueAdded;
    }
}