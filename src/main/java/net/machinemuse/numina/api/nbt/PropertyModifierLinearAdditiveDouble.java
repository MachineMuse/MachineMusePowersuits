package net.machinemuse.numina.api.nbt;

import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.nbt.NBTTagCompound;

public class PropertyModifierLinearAdditiveDouble implements IPropertyModifierDouble {
    public double multiplier;
    public final String tradeoffName;

    public PropertyModifierLinearAdditiveDouble(String tradeoffName, double multiplier) {
        this.multiplier = multiplier;
        this.tradeoffName = tradeoffName;
    }

    @Override
    public Double applyModifier(NBTTagCompound moduleTag, Double value) {
        return value + multiplier * MuseItemUtils.getDoubleOrZero(moduleTag, tradeoffName);
    }

    public String getTradeoffName() {
        return tradeoffName;
    }
}