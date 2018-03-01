package net.machinemuse.numina.utils.module.helpers;

import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class OmniProbeHelper {
    public static String getEIONoCompete(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            if (itemTag != null) {
                return itemTag.getString("eioNoCompete");
            } else {
                return "";
            }
        }
        return "";
    }

    public static void setEIONoCompete(ItemStack stack, String s) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            itemTag.setString("eioNoCompete", s);
        }
    }

    public static boolean getEIOFacadeTransparency(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            if (itemTag != null) {
                return itemTag.getBoolean("eioFacadeTransparency");
            }
        }
        return false;
    }

    public static void setEIOFacadeTransparency(ItemStack stack, boolean b) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            itemTag.setBoolean("eioFacadeTransparency", b);
        }
    }
}
