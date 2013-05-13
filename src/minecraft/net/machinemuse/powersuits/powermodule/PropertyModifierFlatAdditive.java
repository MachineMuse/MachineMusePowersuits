package net.machinemuse.powersuits.powermodule;

import net.machinemuse.api.IPropertyModifier;
import net.minecraft.nbt.NBTTagCompound;

public class PropertyModifierFlatAdditive implements IPropertyModifier {
    public double valueAdded;

    public PropertyModifierFlatAdditive(double valueAdded) {
        this.valueAdded = valueAdded;
    }

    @Override
    public double applyModifier(NBTTagCompound moduleTag, double value) {
        return value + this.valueAdded;
    }

}
