package net.machinemuse.numina.utils.module.helpers;

import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class FluidUtils {
    public static double getWaterLevel(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            Double waterLevel = itemTag.getDouble("Water");
            if (waterLevel != null) {
                return waterLevel;
            }
        }
        return 0;
    }

    public static void setWaterLevel(@Nonnull ItemStack stack, double d) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            itemTag.setDouble("Water", d);
        }
    }

    public static void setLiquid(@Nonnull ItemStack stack, String name) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            itemTag.setString("Liquid", name);
        }
    }

    public static String getLiquid(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            String s = itemTag.getString("Liquid");
            if (s != null) {
                return s;
            }
        }
        return "";
    }
}
