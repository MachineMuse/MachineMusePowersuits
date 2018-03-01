package net.machinemuse.powersuits.block.itemblock;


import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.minecraft.item.ItemBlock;


/*
 * ItemBlock version of the Tinker Table
 */
public class ItemBlockTinkerTable extends ItemBlock {
    private static ItemBlockTinkerTable ourInstance;

    public static ItemBlockTinkerTable getInstance() {
        if (ourInstance == null)
            ourInstance = new ItemBlockTinkerTable();
        return ourInstance;
    }

    private ItemBlockTinkerTable() {
        super(BlockTinkerTable.getInstance());
//        setUnlocalizedName(MPSResourceConstants.RESOURCE_PREFIX + BlockTinkerTable.name);
        //        setRegistryName(BlockTinkerTable.name);
        setUnlocalizedName(BlockTinkerTable.getInstance().getUnlocalizedName());
        setRegistryName(BlockTinkerTable.getInstance().getRegistryName());
        this.hasSubtypes = false;
    }
}