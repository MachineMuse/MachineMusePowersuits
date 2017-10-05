package net.machinemuse_old.numina.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:19 PM, 9/3/13
 *
 * Ported to Java by lehjr on 10/22/16.
 */
public class NuminaItemUtils {
    public static NBTTagCompound getTagCompound(ItemStack stack) {
        if(stack.hasTagCompound())    {
            return stack.getTagCompound();
        } else {
            NBTTagCompound tag = new NBTTagCompound();
            stack.setTagCompound(tag);
            return tag;
        }
    }
}