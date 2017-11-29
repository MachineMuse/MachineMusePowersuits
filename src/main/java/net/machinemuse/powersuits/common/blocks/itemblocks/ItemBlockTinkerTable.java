package net.machinemuse.powersuits.common.blocks.itemblocks;

import net.machinemuse.powersuits.common.MuseConstants;
import net.machinemuse.powersuits.common.blocks.BlockTinkerTable;
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
        setUnlocalizedName(MuseConstants.RESOURCE_PREFIX + BlockTinkerTable.name);
        setRegistryName(BlockTinkerTable.name);
        this.hasSubtypes = false;
    }


}
