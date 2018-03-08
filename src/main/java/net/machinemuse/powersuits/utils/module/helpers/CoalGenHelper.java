package net.machinemuse.powersuits.utils.module.helpers;

import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.utils.nbt.NuminaNBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class CoalGenHelper {
    public static final String TAG_COAL = "Coal";
    public static int getCoalLevel(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = NuminaNBTUtils.getMuseItemTag(stack);
            Integer coalLevel = itemTag.getInteger(TAG_COAL);
            if (coalLevel != null) {
                return coalLevel;
            }
        }
        return 0;
    }
    public static void setCoalLevel(@Nonnull ItemStack stack, int i) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = NuminaNBTUtils.getMuseItemTag(stack);
            itemTag.setInteger(TAG_COAL, i);
        }
    }
}
