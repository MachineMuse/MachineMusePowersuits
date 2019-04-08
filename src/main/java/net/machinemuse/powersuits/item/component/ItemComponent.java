package net.machinemuse.powersuits.item.component;

import net.machinemuse.powersuits.basemod.MPSItems;
import net.minecraft.item.Item;

public class ItemComponent extends Item {
    public ItemComponent(String regName) {
        super(new Properties().group(MPSItems.INSTANCE.creativeTab));
        setRegistryName(regName);
    }
}