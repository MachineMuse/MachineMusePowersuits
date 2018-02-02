package net.machinemuse.powersuits.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:58 PM, 8/6/13
 *
 * Ported to Java by lehjr on 10/22/16.
 */
public class MuseItemTag {
    public static final String NBTPREFIX = "mmmpsmod";

    /**
     * Gets or creates stack.getTagCompound().getTag(NBTPREFIX)
     *
     * @param stack
     * @return an NBTTagCompound, may be newly created. If stack is null,
     * returns null.
     */
    @Nullable
    public static NBTTagCompound getMuseItemTag(ItemStack stack) {
        if (stack == null) {
            return null;
        }

        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        NBTTagCompound stackTag = stack.getTagCompound();

        if (!stackTag.hasKey(NBTPREFIX)) {
            stackTag.setTag(NBTPREFIX, new NBTTagCompound());
            stack.setTagCompound(stackTag);
        }

        return stackTag.getCompoundTag(NBTPREFIX);
    }
}
