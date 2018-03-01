package net.machinemuse.numina.utils.module.helpers;

import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class TorchPlacerHelper {
    public static int getTorchLevel(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            Integer torchLevel = itemTag.getInteger("Torch");
            if (torchLevel != null) {
                return torchLevel;
            }
        }
        return 0;
    }

    public static void setTorchLevel(@Nonnull ItemStack stack, int i) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            itemTag.setInteger("Torch", i);
        }
    }
}
