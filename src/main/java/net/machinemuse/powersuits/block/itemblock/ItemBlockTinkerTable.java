package net.machinemuse.powersuits.block.itemblock;

import net.machinemuse.powersuits.common.MPSItems;
import net.minecraft.item.ItemBlock;

/*
 * ItemBlock version of the Tinker Table
 */
public class ItemBlockTinkerTable extends ItemBlock {
    public ItemBlockTinkerTable() {
        super(MPSItems.INSTANCE.tinkerTable);
        setRegistryName(MPSItems.INSTANCE.tinkerTable.getRegistryName());
        this.hasSubtypes = false;
    }
}
