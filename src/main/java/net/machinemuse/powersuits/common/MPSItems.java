package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.item.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

/**
 * Created by Claire Semple on 9/9/2014.
 *
 * Ported to Java by lehjr on 10/22/16.
 */

public class MPSItems {
    public static Item power_armor_head;
    public static Item power_armor_torso;
    public static Item power_armor_legs;
    public static Item power_armor_feet;
    public static Item power_tool;
    public static Block tinkerTable;
    public static Block luxCapacitor;

    public static Item components;

    public static void populateItems() {
        power_armor_head = MPSRegistry.registerItem(new ItemPowerArmorHelmet(), "power_armor_head", "power_armor_helmet");
        power_armor_torso = MPSRegistry.registerItem(new ItemPowerArmorChestplate(), "power_armor_torso", "powerArmorChestplate");
        power_armor_legs = MPSRegistry.registerItem(new ItemPowerArmorLeggings(), "power_armor_legs", "powerArmorLeggings");
        power_armor_feet = MPSRegistry.registerItem(new ItemPowerArmorBoots(), "power_armor_feet", "powerArmorBoots");
        power_tool     =  MPSRegistry.registerItem(new ItemPowerFist(), "power_tool", "powerFist");

        tinkerTable = MPSRegistry.registerBlock(new BlockTinkerTable());
        luxCapacitor = MPSRegistry.registerBlock(new BlockLuxCapacitor());
    }

    public static void populateComponents() {
        components = MPSRegistry.registerItem(new ItemComponent(), "power_armor_component", "power_armor_component");
        ((ItemComponent)components).populate();
    }
 }