package net.machinemuse.powersuits.block.itemblock;

import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.minecraft.item.ItemBlock;

/*
 * ItemBlock verison of the LuxCapacitor
 */
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