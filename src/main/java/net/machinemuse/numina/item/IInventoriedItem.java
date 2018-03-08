package net.machinemuse.numina.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:00 PM, 9/3/13
 *
 * Ported to Java by lehjr on 11/1/16.
 *
 * This will only slightly break Anima!
 */
public interface IInventoriedItem
{
    boolean canStoreItem(ItemStack p0);

    int getSelectedSlot(ItemStack p0);

    void setSelectedSlot(ItemStack p0, int p1);

    NBTTagList getContentsAsNBTTagList(ItemStack p0);

    NonNullList<ItemStack> getContents(ItemStack p0);

    void setContents(ItemStack p0, NonNullList<ItemStack> p1);

    void insertItem(ItemStack p0, ItemStack p1);

    int getNumStacks(ItemStack p0);
}