//package net.machinemuse.numina.api.nbt;
//
//import net.machinemuse.numina.utils.item.MuseItemUtils;
//import net.minecraft.nbt.NBTTagCompound;
//
//public class PropertyModifierLinearAdditiveInteger implements IPropertyModifierInteger {
//    public double multiplier;
//    public final String tradeoffName;
//    protected int roundTo = 1;
//    protected int offset = 0;
//
//    public PropertyModifierLinearAdditiveInteger(String tradeoffName, double multiplier) {
//        this.tradeoffName = tradeoffName;
//        this.multiplier = multiplier;
//    }
//
//    public PropertyModifierLinearAdditiveInteger(String tradeoffName, double multiplier, int roundTo, int offset) {
//        this.tradeoffName = tradeoffName;
//        this.multiplier = multiplier;
//        this.roundTo = roundTo;
//        this.offset = offset;
//    }
//
//    @Override
//    public Integer applyModifier(NBTTagCompound moduleTag, double value) {
//        long result = (long) (value + multiplier * MuseItemUtils.getScaledIntOrZero(moduleTag, tradeoffName));
//        return (int)Math.round(Double.valueOf(roundWithOffset(result, roundTo, offset)));
//    }
//
//    public long roundWithOffset(double input, int roundTo, int offset) {
//        return Math.round((input + offset) / roundTo) * roundTo - offset;
//    }
//
//    public String getTradeoffName() {
//        return tradeoffName;
//    }
//}