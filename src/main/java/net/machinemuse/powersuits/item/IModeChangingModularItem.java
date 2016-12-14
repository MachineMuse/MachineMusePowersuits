package net.machinemuse.powersuits.item;

import net.machinemuse.numina.item.IModeChangingItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:52 PM, 9/5/13
 *
 * Ported to Java by lehjr on 12/12/16.
 */
public interface IModeChangingModularItem extends IModeChangingItem {
    void cycleModeForItem(ItemStack itemStack, EntityPlayer player, int dMode);

    IIcon getModeIcon(String mode, ItemStack itemStack, EntityPlayer player);

    List<String> getValidModes(ItemStack itemStack);

    String getActiveMode(ItemStack itemStack);
}
