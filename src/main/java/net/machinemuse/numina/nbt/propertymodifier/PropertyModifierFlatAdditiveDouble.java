package net.machinemuse.numina.nbt.propertymodifier;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Used for Base properties such as MAXIMUM_ENERGY.
 */
public class PropertyModifierFlatAdditiveDouble implements IPropertyModifierDouble {
    public double valueAdded;

    public PropertyModifierFlatAdditiveDouble(double valueAdded) {
        this.valueAdded = valueAdded;
    }

    /**
     * @param moduleTag unused
     * @param value
     * @return getValue + this.valueAdded
     */
    @Override
    public Double applyModifier(NBTTagCompound moduleTag, double value) {
        return value + this.valueAdded;
    }
}