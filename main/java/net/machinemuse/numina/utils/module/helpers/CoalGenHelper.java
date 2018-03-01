package net.machinemuse.numina.utils.module.helpers;

import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class CoalGenHelper {
    public static int getCoalLevel(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            Integer coalLevel = itemTag.getInteger("Coal");
            if (coalLevel != null) {
                return coalLevel;
            }
        }
        return 0;
    }
    public static void setCoalLevel(@Nonnull ItemStack stack, int i) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            itemTag.setInteger("Coal", i);
        }
    }
}
