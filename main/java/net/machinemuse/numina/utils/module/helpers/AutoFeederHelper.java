package net.machinemuse.numina.utils.module.helpers;

import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class AutoFeederHelper {
    public static double getFoodLevel(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            return itemTag.getDouble("Food");
        }
        return 0.0;
    }

    public static void setFoodLevel(@Nonnull ItemStack stack, double d) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            itemTag.setDouble("Food", d);
        }
    }

    public static double getSaturationLevel(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            Double saturationLevel = itemTag.getDouble("Saturation");
            if (saturationLevel != null) {
                return saturationLevel;
            }
        }
        return 0.0F;
    }

    public static void setSaturationLevel(@Nonnull ItemStack stack, double d) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            itemTag.setDouble("Saturation", d);
        }
    }
}
