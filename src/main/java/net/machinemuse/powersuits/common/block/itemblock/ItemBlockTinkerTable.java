package net.machinemuse.powersuits.common.block.itemblock;

import net.machinemuse.powersuits.common.MPSConstants;
import net.machinemuse.powersuits.common.block.BlockTinkerTable;
import net.minecraft.item.ItemBlock;

public class ItemBlockTinkerTable extends ItemBlock {
    private static ItemBlockTinkerTable ourInstance;

    public static ItemBlockTinkerTable getInstance() {
        if (ourInstance == null)
            ourInstance = new ItemBlockTinkerTable();
        return ourInstance;
    }

    private ItemBlockTinkerTable() {
        super(BlockTinkerTable.getInstance());
        setUnlocalizedName(MPSConstants.RESOURCE_PREFIX + BlockTinkerTable.name);
        setRegistryName(BlockTinkerTable.name);
        this.hasSubtypes = false;
    }


}
