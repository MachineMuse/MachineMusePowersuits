package net.machinemuse.powersuits.common.items.modules;

import net.machinemuse.numina.api.nbt.IPropertyModifier;
import net.minecraft.nbt.NBTTagCompound;

public class PropertyModifierFlatAdditive implements IPropertyModifier<Double> {
    public double valueAdded;

    public PropertyModifierFlatAdditive(double valueAdded) {
        this.valueAdded = valueAdded;
    }

    @Override
    public Double applyModifier(NBTTagCompound moduleTag, Double value) {
        return value + this.valueAdded;
    }
}
