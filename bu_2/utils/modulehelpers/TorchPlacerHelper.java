//package net.machinemuse.powersuits.utils.modulehelpers;
//
//import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//
//import javax.annotation.Nonnull;
//
///**
// * Helper methods for the old and now unused "Torch Placer" module
// */
//public class TorchPlacerHelper {
//    public static final String TAG_TORCH = "Torch";
//
//    public static int getTorchLevel(@Nonnull ItemStack stack) {
//        if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
//            NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
//            Integer torchLevel = itemTag.getInt(TAG_TORCH);
//            if (torchLevel != null) {
//                return torchLevel;
//            }
//        }
//        return 0;
//    }
//
//    public static void setTorchLevel(@Nonnull ItemStack stack, int i) {
//        if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
//            NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
//            itemTag.putInt(TAG_TORCH, i);
//        }
//    }
//}