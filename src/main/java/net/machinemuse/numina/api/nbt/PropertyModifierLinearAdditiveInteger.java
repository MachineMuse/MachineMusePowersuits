package net.machinemuse.numina.api.nbt;

import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.nbt.NBTTagCompound;

public class PropertyModifierLinearAdditiveInteger implements IPropertyModifierInteger {
    public int multiplier;
    public final String tradeoffName;

    public PropertyModifierLinearAdditiveInteger(String tradeoffName, int multiplier) {
        this.multiplier = multiplier;
        this.tradeoffName = tradeoffName;
    }

    @Override
    public Integer applyModifier(NBTTagCompound moduleTag, Integer value) {
        return value + multiplier * MuseItemUtils.getIntOrZero(moduleTag, tradeoffName);
    }

    public String getTradeoffName() {
        return tradeoffName;
    }
}