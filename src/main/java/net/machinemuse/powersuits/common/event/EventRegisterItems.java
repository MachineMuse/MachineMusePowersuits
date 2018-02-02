package net.machinemuse.powersuits.common.event;


import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.items.old.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.MODID;

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
    public static Item powerTool = itemInit(new ItemPowerFist(), "power_fist", "powerfist");

    // Keys for modules // TODO: switch to string based logic
    public static List<IModularItem> ARMORONLY;
    public static List<IModularItem> ALLITEMS;
    public static List<IModularItem> HEADONLY;
    public static List<IModularItem> TORSOONLY;
    public static List<IModularItem> LEGSONLY;
    public static List<IModularItem> FEETONLY;
    public static List<IModularItem> TOOLONLY;

    private static Item itemInit(Item item, String regName, String unlocalizedName) {
        item.setUnlocalizedName(unlocalizedName);
        item.setRegistryName(new ResourceLocation(MODID, regName));
        return item;
    }

    @SubscribeEvent
    public static void initItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ItemComponent.getInstance(),
                powerArmorHead,
                powerArmorTorso,
                powerArmorLegs,
                powerArmorFeet,
                powerTool
        );

        // Module keys
        ARMORONLY = Arrays.asList(
                (IModularItem) powerArmorHead,
                (IModularItem) powerArmorTorso,
                (IModularItem)powerArmorLegs,
                (IModularItem)powerArmorFeet);
        ALLITEMS = Arrays.asList(
                (IModularItem)powerArmorHead,
                (IModularItem)powerArmorTorso,
                (IModularItem)powerArmorLegs,
                (IModularItem)powerArmorFeet,
                (IModularItem)powerTool);
        HEADONLY = Collections.singletonList((IModularItem)powerArmorHead);
        TORSOONLY = Collections.singletonList((IModularItem)powerArmorTorso);
        LEGSONLY = Collections.singletonList((IModularItem)EventRegisterItems.getInstance().powerArmorLegs);
        FEETONLY = Collections.singletonList((IModularItem)EventRegisterItems.getInstance().powerArmorFeet);
        TOOLONLY = Collections.singletonList((IModularItem)EventRegisterItems.getInstance().powerTool);
    }
}
