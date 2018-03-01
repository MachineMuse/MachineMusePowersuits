package net.machinemuse.numina.api.nbt;

import net.minecraft.nbt.NBTTagCompound;

public class PropertyModifierFlatAdditiveDouble implements IPropertyModifierDouble {
    public double valueAdded;

    public PropertyModifierFlatAdditiveDouble(double valueAdded) {
        this.valueAdded = valueAdded;
    }

    @Override
    public Double applyModifier(NBTTagCompound moduleTag, Double value) {
        return value + this.valueAdded;
    }
}