package net.machinemuse.powersuits.common.items.modules;

import net.machinemuse.numina.api.nbt.IPropertyModifier;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.nbt.NBTTagCompound;

public class PropertyModifierLinearAdditive implements IPropertyModifier<Double> {
    public double multiplier;
    public final String tradeoffName;

    public PropertyModifierLinearAdditive(String tradeoffName, double multiplier) {
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