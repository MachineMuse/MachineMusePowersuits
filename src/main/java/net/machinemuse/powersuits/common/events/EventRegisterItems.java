package net.machinemuse.powersuits.common.events;


import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.machinemuse.powersuits.common.Constants.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class EventRegisterItems {
    private static EventRegisterItems ourInstance;

    public static EventRegisterItems getInstance() {
        if(ourInstance == null)
            ourInstance = new EventRegisterItems();
        return ourInstance;
    }

    private EventRegisterItems() {
    }

    @SubscribeEvent
    public static void initBlockItems(RegistryEvent.Register<Item> event) {
    }
}
