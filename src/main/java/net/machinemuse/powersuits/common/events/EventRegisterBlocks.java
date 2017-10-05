package net.machinemuse.powersuits.common.events;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


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




    }

    @SubscribeEvent
    public static void initBlockItems(Register<Item> event) {



    }
}
