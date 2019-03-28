package net.machinemuse.powersuits.utils.modulehelpers;

import net.machinemuse.numina.item.IModularItem;
import net.machinemuse.numina.nbt.MuseNBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class CoalGenHelper {
    public static final String TAG_COAL = "Coal";

    public static int getCoalLevel(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
            Integer coalLevel = itemTag.getInteger(TAG_COAL);
            if (coalLevel != null) {
                return coalLevel;
            }
        }
        return 0;
    }

    public static void setCoalLevel(@Nonnull ItemStack stack, int i) {
        if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
            itemTag.setInteger(TAG_COAL, i);
        }
    }
}
