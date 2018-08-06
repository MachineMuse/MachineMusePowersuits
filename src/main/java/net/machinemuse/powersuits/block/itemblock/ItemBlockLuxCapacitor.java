package net.machinemuse.powersuits.block.itemblock;

import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.minecraft.item.ItemBlock;

/*
 * ItemBlock verison of the LuxCapacitor
 */
public class ItemBlockLuxCapacitor extends ItemBlock {
    private volatile static ItemBlockLuxCapacitor INSTANCE;

    public static ItemBlockLuxCapacitor getInstance() {
        if (INSTANCE == null) {
            synchronized (ItemBlockLuxCapacitor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ItemBlockLuxCapacitor();
                }
            }
        }
        return INSTANCE;
    }

    private ItemBlockLuxCapacitor() {
        super(BlockLuxCapacitor.getInstance());
        setRegistryName(BlockLuxCapacitor.name);
    }
}