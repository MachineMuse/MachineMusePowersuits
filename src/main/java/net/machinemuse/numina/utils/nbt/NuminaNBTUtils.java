package net.machinemuse.numina.utils.nbt;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class NuminaNBTUtils {
    /**
     * Gets or creates an NBTTagCompound
     *
     * @param stack
     * @return an NBTTagCompound, may be newly created. If stack is empty,
     *         returns empty tag.
     */
    public static NBTTagCompound getTagCompound(@Nonnull ItemStack stack) {
        if (stack.isEmpty())
            return new NBTTagCompound();

        if(stack.hasTagCompound())
            return stack.getTagCompound();
        else {
            NBTTagCompound tag = new NBTTagCompound();
            stack.setTagCompound(tag);
            return tag;
        }
    }

    /**
     * Gets or creates stack.getTagCompound().getTag(NBTPREFIX)
     *
     * @param stack
     * @return an NBTTagCompound, may be newly created. If stack is empty,
     *         returns empty tag.
     */
    public static NBTTagCompound getMuseItemTag(@Nonnull ItemStack stack) {
        NBTTagCompound stackTag = getTagCompound(stack);

        NBTTagCompound properties;
        if (stackTag.hasKey(NuminaNBTConstants.NBTPREFIX))
            properties = stackTag.getCompoundTag(NuminaNBTConstants.NBTPREFIX);
        else {
            properties = new NBTTagCompound();
            stackTag.setTag(NuminaNBTConstants.NBTPREFIX, properties);
        }
        return properties;
    }










}
