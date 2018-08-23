package net.machinemuse.numina.api.nbt;

import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.minecraft.nbt.NBTTagCompound;

public class PropertyModifierLinearAdditiveDouble implements IPropertyModifierDouble {
    public double multiplier;
    public final String tradeoffName;

    public PropertyModifierLinearAdditiveDouble(String tradeoffName, double multiplier) {
        this.multiplier = multiplier;
        this.tradeoffName = tradeoffName;
    }

    @Override
    public Double applyModifier(NBTTagCompound moduleTag, double value) {
        return value + multiplier * MuseItemUtils.getDoubleOrZero(moduleTag, tradeoffName);
    }

    public String getTradeoffName() {
        return tradeoffName;
    }
}