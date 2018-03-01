package net.machinemuse.numina.item;

import net.machinemuse.numina.utils.item.NuminaItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:00 PM, 9/3/13
 *
 * Ported to Java by lehjr on 11/1/16.
 *
 * This will only slightly break Anima!
 */
public class InventoriedItem implements IInventoriedItem {
    @Override
    public boolean canStoreItem(ItemStack p0) {
        return false;
    }

    @Override
    public int getSelectedSlot( ItemStack stack) {
        NBTTagCompound tag = NuminaItemUtils.getTagCompound(stack);
        int integer;
        if (tag.hasKey("selected")) {
            integer = tag.getInteger("selected");
        }
        else {
            tag.setInteger("selected", 0);
            integer = 0;
        }
        return integer;
    }

    @Override
    public void setSelectedSlot(ItemStack stack, int i) {
        NBTTagCompound tag = NuminaItemUtils.getTagCompound(stack);
        tag.setInteger("selected", i);
    }

    @Override
    public NBTTagList getContentsAsNBTTagList(ItemStack stack) {
        NBTTagCompound tag = NuminaItemUtils.getTagCompound(stack);
        NBTTagList tagList;
        if (tag.hasKey("contents")) {
            tagList = tag.getTagList("contents", 10);
        }
        else {
            NBTTagList list = new NBTTagList();
            tag.setTag("contents", (NBTBase)list);
            tagList = list;
        }
        return tagList;
    }

    @Override
    public List<ItemStack> getContents(ItemStack stack) {
        NBTTagList list = this.getContentsAsNBTTagList(stack);
        List<ItemStack> stackList = new ArrayList<>();
        for (int i = 0; i < list.tagCount(); i++) {
            stackList.add(new ItemStack(list.getCompoundTagAt(i)));
        }
        return stackList;
    }

    @Override
    public void setContents(ItemStack stack, List<ItemStack> contents) {
        NBTTagList list = new NBTTagList();
        for (ItemStack item : contents)
            list.appendTag(item.writeToNBT(new NBTTagCompound()));
        NuminaItemUtils.getTagCompound(stack).setTag("contents", list);
    }

    @Override
    public void insertItem(ItemStack bag, ItemStack stackToInsert) {
        List<ItemStack> contents = getContents(bag);
        contents.add(stackToInsert);
        setContents(bag, contents);
    }

    @Override
    public int getNumStacks(ItemStack stack) {
        NBTTagCompound tag = NuminaItemUtils.getTagCompound(stack);
        return tag.hasKey("contents") ? tag.getTagList("contents", 10).tagCount() : 0;
    }
}