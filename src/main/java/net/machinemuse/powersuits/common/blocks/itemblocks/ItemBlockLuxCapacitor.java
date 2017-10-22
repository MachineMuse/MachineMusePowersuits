package net.machinemuse.powersuits.common.blocks.itemblocks;

import net.machinemuse.powersuits.common.blocks.BlockLuxCapacitor;
import net.minecraft.item.ItemBlock;

public class ItemBlockLuxCapacitor extends ItemBlock {
    private static ItemBlockLuxCapacitor ourInstance;

    public static ItemBlockLuxCapacitor getInstance() {
        if (ourInstance == null)
            ourInstance = new ItemBlockLuxCapacitor();
        return ourInstance;
    }

    private ItemBlockLuxCapacitor() {
        super(BlockLuxCapacitor.getInstance());
        setRegistryName(BlockLuxCapacitor.name);
    }
}
