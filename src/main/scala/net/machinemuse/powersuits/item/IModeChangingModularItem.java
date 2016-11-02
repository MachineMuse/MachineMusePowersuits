package net.machinemuse.powersuits.item;

import net.machinemuse.numina.item.IModeChangingItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * Ported to Java by lehjr on 11/1/16.
 */
public interface IModeChangingModularItem extends IModeChangingItem{
    IIcon getModeIcon(String mode, ItemStack stack, EntityPlayer player);

    List<String> getValidModes(ItemStack stack, EntityPlayer player);

    List<String> getValidModes(ItemStack stack);

    String getActiveMode(ItemStack stack);
}