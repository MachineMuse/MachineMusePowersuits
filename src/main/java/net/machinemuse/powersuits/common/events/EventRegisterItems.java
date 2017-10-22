package net.machinemuse.powersuits.common.events;


import net.machinemuse.powersuits.common.items.*;
import net.machinemuse.powersuits.common.items.old.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.machinemuse.powersuits.common.MuseConstants.MODID;

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

    public static Item powerArmorHead = itemInit(new ItemPowerArmorHelmet(), "powerarmor_head", "powerArmorHelmet");
    public static Item powerArmorTorso = itemInit(new ItemPowerArmorChestplate(), "powerarmor_torso", "powerArmorChestplate");
    public static Item powerArmorLegs = itemInit(new ItemPowerArmorLeggings(), "powerarmor_legs", "powerArmorLeggings");
    public static Item powerArmorFeet = itemInit(new ItemPowerArmorBoots(), "powerarmor_feet", "powerArmorBoots");
    public static Item powerTool = itemInit(new ItemPowerFist(), "power_fist", "powerFist");

    private static Item itemInit(Item item, String regName, String unlocalizedName) {
        item.setUnlocalizedName(unlocalizedName);
        item.setRegistryName(new ResourceLocation(MODID, regName));
        return item;
    }

    @SubscribeEvent
    public static void initBlockItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ItemComponent.getInstance(),
                                         powerArmorHead,
                                         powerArmorTorso,
                                         powerArmorLegs,
                                         powerArmorFeet,
                                         powerTool
                                        );
    }
}
