package net.machinemuse.powersuits.utils.module.helpers;

import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.utils.nbt.NuminaNBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class OmniProbeHelper {
    public static final String TAG_EIO_NO_COMPLETE = "eioNoCompete";
    public static final String TAG_EIO_FACADE_TRANSPARENCY = "eioFacadeTransparency";


    public static String getEIONoCompete(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = NuminaNBTUtils.getMuseItemTag(stack);
            if (itemTag != null) {
                return itemTag.getString(TAG_EIO_NO_COMPLETE);
            } else {
                return "";
            }
        }
        return "";
    }

    public static void setEIONoCompete(ItemStack stack, String s) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = NuminaNBTUtils.getMuseItemTag(stack);
            itemTag.setString(TAG_EIO_NO_COMPLETE, s);
        }
    }

    public static boolean getEIOFacadeTransparency(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = NuminaNBTUtils.getMuseItemTag(stack);
            if (itemTag != null) {
                return itemTag.getBoolean(TAG_EIO_FACADE_TRANSPARENCY);
            }
        }
        return false;
    }

    public static void setEIOFacadeTransparency(ItemStack stack, boolean b) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = NuminaNBTUtils.getMuseItemTag(stack);
            itemTag.setBoolean(TAG_EIO_FACADE_TRANSPARENCY, b);
        }
    }
}
