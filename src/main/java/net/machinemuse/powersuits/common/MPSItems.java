package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.block.itemblock.ItemBlockLuxCapacitor;
import net.machinemuse.powersuits.block.itemblock.ItemBlockTinkerTable;
import net.machinemuse.powersuits.item.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.machinemuse.powersuits.common.ModularPowersuits.MODID;

/**
 * Created by Claire Semple on 9/9/2014.
 *
 * Ported to Java by lehjr on 10/22/16.
 */
@Mod.EventBusSubscriber(modid = MODID)
public enum MPSItems {
    INSTANCE;

    // Armor --------------------------------------------------------------------------------------
    public static Item powerArmorHead = itemRegister(new ItemPowerArmorHelmet(), "powerarmor_head", "powerArmorHelmet");
    public static Item powerArmorTorso = itemRegister(new ItemPowerArmorChestplate(), "powerarmor_torso", "powerArmorChestplate");
    public static Item powerArmorLegs = itemRegister(new ItemPowerArmorLeggings(), "powerarmor_legs", "powerArmorLeggings");
    public static Item powerArmorFeet = itemRegister(new ItemPowerArmorBoots(), "powerarmor_feet", "powerArmorBoots");
    // HandHeld -----------------------------------------------------------------------------------
    public static Item powerFist = itemRegister(new ItemPowerFist(), "power_fist", "powerfist");

    // Components ---------------------------------------------------------------------------------
    public static Item components = ItemComponent.getInstance();

    @SubscribeEvent
    public static void regigisterItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                powerArmorHead ,
                powerArmorTorso,
                powerArmorLegs,
                powerArmorFeet,
                powerFist,
                components,

                ItemBlockTinkerTable.getInstance(),
                ItemBlockLuxCapacitor.getInstance()
                );


        ((ItemComponent)components).populate();
        ((ItemComponent)components).registerOres();
    }

    private static Item itemRegister(Item item, String regName, String unlocalizedName) {
        // including the ModID in the unlocalized name helps keep the names unique so they can be used as keys for maps
        item.setUnlocalizedName(new StringBuilder(MODID).append(".").append(unlocalizedName).toString());
        item.setRegistryName(new ResourceLocation(MODID, regName));
        return item;
    }

    @SubscribeEvent
    public static void initBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(BlockTinkerTable.getInstance());
        event.getRegistry().register(BlockLuxCapacitor.getInstance());
//        event.getRegistry().register(new BlockLiquidNitrogen()); // TODO?

    }
 }
