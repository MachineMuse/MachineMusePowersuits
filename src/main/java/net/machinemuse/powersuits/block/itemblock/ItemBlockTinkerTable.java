package net.machinemuse.powersuits.block.itemblock;

import net.machinemuse.powersuits.block.BlockTinkerTable;
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
        super(BlockTinkerTable.getInstance());
//        setUnlocalizedName(BlockTinkerTable.getInstance().getUnlocalizedName());
        setRegistryName(BlockTinkerTable.getInstance().getRegistryName());
        System.out.println("tinker table reg name " + this.getRegistryName());
        System.out.println("tinker table unlocalized name " + this.getUnlocalizedName());

        this.hasSubtypes = false;
    }
}
