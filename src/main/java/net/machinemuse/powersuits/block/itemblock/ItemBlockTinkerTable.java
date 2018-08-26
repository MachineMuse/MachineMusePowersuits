package net.machinemuse.powersuits.block.itemblock;

import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.common.MPSItems;
import net.minecraft.item.ItemBlock;

/*
 * ItemBlock version of the Tinker Table
 */
public class ItemBlockTinkerTable extends ItemBlock {
    private volatile static ItemBlockTinkerTable INSTANCE;

    public static ItemBlockTinkerTable getInstance() {
        if (INSTANCE == null) {
            synchronized (ItemBlockTinkerTable.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ItemBlockTinkerTable();
                }
            }
        }
        return INSTANCE;
    }

    private ItemBlockTinkerTable() {
        super(MPSItems.INSTANCE.tinkerTable);
        setRegistryName(MPSItems.INSTANCE.tinkerTable.getRegistryName());
        this.hasSubtypes = false;
    }
}
