package net.machinemuse.numina.utils.module.helpers;

import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.utils.nbt.NuminaNBTUtils;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

@Deprecated // TODO: switch to fluid capabilities
public class FluidUtils {
    public static final String TAG_WATER = "Water";
    public static final String TAG_LIQUID = "Liquid";

    public static double getWaterLevel(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = NuminaNBTUtils.getMuseItemTag(stack);
            Double waterLevel = itemTag.getDouble(TAG_WATER);
            if (waterLevel != null) {
                return waterLevel;
            }
        }
        return 0;
    }

    public static void setWaterLevel(@Nonnull ItemStack stack, double d) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = NuminaNBTUtils.getMuseItemTag(stack);
            itemTag.setDouble(TAG_WATER, d);
        }
    }

    public static void setLiquid(@Nonnull ItemStack stack, String name) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = NuminaNBTUtils.getMuseItemTag(stack);
            itemTag.setString(TAG_LIQUID, name);
        }
    }

    public static String getLiquid(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = NuminaNBTUtils.getMuseItemTag(stack);
            String s = itemTag.getString(TAG_LIQUID);
            if (s != null) {
                return s;
            }
        }
        return "";
    }
}
