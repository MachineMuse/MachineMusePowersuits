package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.common.events.EventRegisterItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * Ported to Java by lehjr on 11/3/16.
 */
public class MPSCreativeTab extends CreativeTabs {
    public MPSCreativeTab() {
        super(CreativeTabs.getNextID(), "powersuits");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(EventRegisterItems.getInstance().powerArmorHead);
    }
}