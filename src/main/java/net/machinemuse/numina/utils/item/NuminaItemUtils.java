package net.machinemuse.numina.utils.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
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
    public static NonNullList<ItemStack> getAllItemsInInventoryWithCapabilities(EntityPlayer entityPlayer, Capability capability) {
        NonNullList<ItemStack> capablityStackList =  NonNullList.create();
        for(int i=0; i< entityPlayer.inventory.getSizeInventory(); i++) {
            ItemStack stack = entityPlayer.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.hasCapability(capability, null))
                capablityStackList.add(stack);
        }
        return capablityStackList;
    }

    public static NonNullList<ItemStack> getEquipedItemsInInventoryWithCapabilities(EntityPlayer entityPlayer, Capability capability) {
        NonNullList<ItemStack> capablityStackList =  NonNullList.create();

        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            ItemStack stack = entityPlayer.getItemStackFromSlot(slot);
            if (!stack.isEmpty() && stack.hasCapability(capability, null))
                capablityStackList.add(stack);
        }
        return capablityStackList;
    }



}