package net.machinemuse.numina.utils.nbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.machinemuse.numina.api.constants.NuminaNBTConstants.TAG_ITEM_PREFIX;

public class MuseNBTUtils {


    /**
     * Gets or creates stack.getTagCompound().getTag(TAG_ITEM_PREFIX)
     *
     * @param stack
     * @return an NBTTagCompound, may be newly created. If stack is null,
     * returns null.
     */
    @Nullable
    public static NBTTagCompound getMuseItemTag(@Nonnull ItemStack stack) {
        if (stack.isEmpty())
            return null;

        NBTTagCompound stackTag = getNBTTag(stack);
        NBTTagCompound properties = (stackTag.hasKey(TAG_ITEM_PREFIX)) ? stackTag.getCompoundTag(TAG_ITEM_PREFIX) : new NBTTagCompound();
        stackTag.setTag(TAG_ITEM_PREFIX, properties);
        return properties;
    }

    public static NBTTagCompound getNBTTag(@Nonnull ItemStack itemStack) {
        if(!itemStack.isEmpty() && itemStack.hasTagCompound())    {
            return itemStack.getTagCompound();
        } else {
            NBTTagCompound tag = new NBTTagCompound();
            itemStack.setTagCompound(tag);
            return tag;
        }
    }
}