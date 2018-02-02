package net.machinemuse.powersuits.common.items.modules;

import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.minecraft.nbt.NBTTagCompound;

public class PropertyModifierIntLinearAdditive extends PropertyModifierLinearAdditive {
    protected int roundTo = 0;
    protected int offset = 0;

    public PropertyModifierIntLinearAdditive(String tradeoffName, double multiplier, int roundTo, int offset) {
        super(tradeoffName, multiplier);
        this.roundTo = roundTo;
        this.offset = offset;
    }

    @Override
    public Double applyModifier(NBTTagCompound moduleTag, Double value) {
        long result = (long) (value + multiplier * MuseItemUtils.getDoubleOrZero(moduleTag, tradeoffName));
        return Double.valueOf(roundWithOffset(result, roundTo, offset));
    }

    public long roundWithOffset(double input, int roundTo, int offset) {
        return Math.round((input + offset) / roundTo) * roundTo - offset;
    }
}
