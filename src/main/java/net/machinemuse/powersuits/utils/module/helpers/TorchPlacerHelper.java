package net.machinemuse.powersuits.utils.module.helpers;

import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.utils.nbt.NuminaNBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class TorchPlacerHelper {
    public static final String TAG_TORCH = "Torch";

    public static int getTorchLevel(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = NuminaNBTUtils.getMuseItemTag(stack);
            Integer torchLevel = itemTag.getInteger(TAG_TORCH);
            if (torchLevel != null) {
                return torchLevel;
            }
        }
        return 0;
    }

    public static void setTorchLevel(@Nonnull ItemStack stack, int i) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = NuminaNBTUtils.getMuseItemTag(stack);
            itemTag.setInteger(TAG_TORCH, i);
        }
    }
}
