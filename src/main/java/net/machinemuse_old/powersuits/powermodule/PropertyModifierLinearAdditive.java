package net.machinemuse_old.powersuits.powermodule;

import net.machinemuse_old.api.IPropertyModifier;
import net.machinemuse_old.utils.MuseItemUtils;
import net.minecraft.nbt.NBTTagCompound;

public class PropertyModifierLinearAdditive implements IPropertyModifier {
    public double multiplier;
    public final String tradeoffName;

    public PropertyModifierLinearAdditive(String tradeoffName, double multiplier) {
        this.multiplier = multiplier;
        this.tradeoffName = tradeoffName;
    }

    @Override
    public double applyModifier(NBTTagCompound moduleTag, double value) {
        return value + multiplier * MuseItemUtils.getDoubleOrZero(moduleTag, tradeoffName);
    }

    public String getTradeoffName() {
        return tradeoffName;
    }
}