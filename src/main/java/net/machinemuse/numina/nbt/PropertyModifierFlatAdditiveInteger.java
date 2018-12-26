//package net.machinemuse.numina.api.nbt;
//
//import net.minecraft.nbt.NBTTagCompound;
//
//public class PropertyModifierFlatAdditiveInteger implements IPropertyModifierInteger {
//    protected int valueAdded;
//    protected int roundTo = 1;
//    protected int offset = 0;
//
//    public PropertyModifierFlatAdditiveInteger(int valueAdded) {
//        this.valueAdded = valueAdded;
//    }
//
//    public PropertyModifierFlatAdditiveInteger(double valueAdded, int roundTo, int offset) {
//        this.valueAdded = (int) Math.round(Double.valueOf(roundWithOffset(valueAdded, roundTo, offset)));
//    }
//
//    /**
//     *
//     * @param moduleTag unused
//     * @param value
//     * @return getValue + this.valueAdded
//     */
//    @Override
//    public Integer applyModifier(NBTTagCompound moduleTag, double value) {
//        long rounded = roundWithOffset(value, roundTo, offset);
//        return this.valueAdded + (int) Math.round(Double.valueOf(rounded));
//    }
//
//    public long roundWithOffset(double input, int roundTo, int offset) {
//        return Math.round((input + offset) / roundTo) * roundTo - offset;
//    }
//}