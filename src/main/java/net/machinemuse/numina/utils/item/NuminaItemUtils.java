package net.machinemuse.numina.utils.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;

import java.util.ArrayList;
import java.util.List;

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

    public static List<ItemStack> getAllItemsInInventoryWithCapabilities(EntityPlayer entityPlayer, Capability capability) {
        List<ItemStack> capablityStackList = new ArrayList<>();
        for(int i=0; i< entityPlayer.inventory.getSizeInventory(); i++) {
            ItemStack stack = entityPlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.hasCapability(capability, null))
                capablityStackList.add(stack);
        }
        return capablityStackList;
    }

    public static List<ItemStack> getEquipedItemsInInventoryWithCapabilities(EntityPlayer entityPlayer, Capability capability) {
        List<ItemStack> capablityStackList = new ArrayList<>();
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            ItemStack stack = entityPlayer.getItemStackFromSlot(slot);
            if (stack != null && stack.hasCapability(capability, null))
                capablityStackList.add(stack);
        }
        return capablityStackList;
    }



}