package net.machinemuse.powersuits.utils.module.helpers;

import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.utils.nbt.NuminaNBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class AutoFeederHelper {
    public static String TAG_FOOD = "Food";
    public static final String TAG_SATURATION = "Saturation";

    public static double getFoodLevel(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = NuminaNBTUtils.getMuseItemTag(stack);
            return itemTag.getDouble(TAG_FOOD);
        }
        return 0.0;
    }

    public static void setFoodLevel(@Nonnull ItemStack stack, double d) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = NuminaNBTUtils.getMuseItemTag(stack);
            itemTag.setDouble(TAG_FOOD, d);
        }
    }

    public static double getSaturationLevel(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = NuminaNBTUtils.getMuseItemTag(stack);
            Double saturationLevel = itemTag.getDouble(TAG_SATURATION);
            if (saturationLevel != null) {
                return saturationLevel;
            }
        }
        return 0.0F;
    }

    public static void setSaturationLevel(@Nonnull ItemStack stack, double d) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = NuminaNBTUtils.getMuseItemTag(stack);
            itemTag.setDouble(TAG_SATURATION, d);
        }
    }
}
