package net.machinemuse.numina.api.nbt;

import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.nbt.NBTTagCompound;

@Deprecated // most likely will not work correctly due to data type size and math opperations involved
public class PropertyModifierLinearAdditiveByte implements IPropertyModifierByte {
    public int multiplier;
    public final String tradeoffName;

    public PropertyModifierLinearAdditiveByte(String tradeoffName, byte multiplier) {
        this.multiplier = multiplier;
        this.tradeoffName = tradeoffName;
    }

    public String getTradeoffName() {
        return tradeoffName;
    }

    @Override
    public Byte applyModifier(NBTTagCompound moduleTag, Byte value) {
        return (byte)(value + multiplier * MuseItemUtils.getByteOrZero(moduleTag, tradeoffName));
    }
}