//package net.machinemuse.numina.utils.nbt;
//
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//
//import static net.machinemuse.numina.api.constants.NuminaNBTConstants.NBTPREFIX;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 6:58 PM, 8/6/13
// *
// * Ported to Java by lehjr on 10/22/16.
// */
//public class MuseItemTag {
//
//    /**
//     * Gets or creates stack.getTagCompound().getTag(NBTPREFIX)
//     *
//     * @param stack
//     * @return an NBTTagCompound, may be newly created. If stack is null,
//     * returns null.
//     */
//    @Nonnull
//    public static NBTTagCompound getMuseItemTag(@Nonnull ItemStack stack) {
//        if (stack.isEmpty()) {
//            return new NBTTagCompound();
//        }
//
//        NBTTagCompound stackTag = (stack.hasTagCompound()) ? stack.getTagCompound() : new NBTTagCompound();
//        stack.setTagCompound(stackTag);
//        NBTTagCompound properties = (stackTag.hasKey(NBTPREFIX)) ? stackTag.getCompoundTag(NBTPREFIX) : new NBTTagCompound();
//        stackTag.setTag(NBTPREFIX, properties);
//        return properties;
//    }
//}
