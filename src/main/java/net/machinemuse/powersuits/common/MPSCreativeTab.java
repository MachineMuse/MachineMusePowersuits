package net.machinemuse.powersuits.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Ported to Java by lehjr on 11/3/16.
 */
public class MPSCreativeTab extends CreativeTabs {
    public MPSCreativeTab() {
        super(CreativeTabs.getNextID(), "powersuits");
    }

    @Override
    public Item getTabIconItem() {
        return MPSItems.getInstance().powerArmorHead;
    }
}