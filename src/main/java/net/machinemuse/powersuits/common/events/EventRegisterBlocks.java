package net.machinemuse.powersuits.common.events;

import net.machinemuse.powersuits.common.blocks.BlockLuxCapacitor;
import net.machinemuse.powersuits.common.blocks.BlockTinkerTable;
import net.machinemuse.powersuits.common.blocks.itemblocks.ItemBlockLuxCapacitor;
import net.machinemuse.powersuits.common.blocks.itemblocks.ItemBlockTinkerTable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.machinemuse.powersuits.common.MPSConstants.MODID;


@Mod.EventBusSubscriber(modid = MODID)
public class EventRegisterBlocks {
    private static EventRegisterBlocks ourInstance;

    public static EventRegisterBlocks getInstance() {
        if(ourInstance == null)
            ourInstance = new EventRegisterBlocks();
        return ourInstance;
    }

    private EventRegisterBlocks() {
    }

    @SubscribeEvent
    public static void initBlocks(Register<Block> event) {
        event.getRegistry().register(BlockTinkerTable.getInstance());
        event.getRegistry().register(BlockLuxCapacitor.getInstance());
    }

    @SubscribeEvent
    public static void initBlockItems(Register<Item> event) {
        event.getRegistry().register(ItemBlockTinkerTable.getInstance());
        event.getRegistry().register(ItemBlockLuxCapacitor.getInstance());
    }
}
