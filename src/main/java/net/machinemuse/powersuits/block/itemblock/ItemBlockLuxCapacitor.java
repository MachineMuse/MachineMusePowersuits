package net.machinemuse.powersuits.block.itemblock;

import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.common.MPSItems;
import net.minecraft.item.ItemBlock;

/*
 * ItemBlock verison of the LuxCapacitor
 */
public class ItemBlockLuxCapacitor extends ItemBlock {
    public ItemBlockLuxCapacitor() {
        super(MPSItems.INSTANCE.luxCapacitor);
        setRegistryName(MPSItems.INSTANCE.luxCapacitor.getRegistryName());
    }
}