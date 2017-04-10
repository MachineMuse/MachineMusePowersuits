package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.block.TestBlock;
import net.machinemuse.powersuits.item.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

/**
 * Created by Claire Semple on 9/9/2014.
 *
 * Ported to Java by lehjr on 10/22/16.
 */

public class MPSItems {
    public static Item powerArmorHead;
    public static Item powerArmorTorso;
    public static Item powerArmorLegs;
    public static Item powerArmorFeet;
    public static Item powerTool;
    public static Block tinkerTable;
    public static Block luxCapacitor;
    public static Block testBlock;



    public static Item components;
    public static Item dummies;

    public static void populateItems() {
        powerArmorHead = MPSRegistry.registerItem(new ItemPowerArmorHelmet(), "powerArmorHead", "powerArmorHelmet");
        powerArmorTorso = MPSRegistry.registerItem(new ItemPowerArmorChestplate(), "powerArmorTorso", "powerArmorChestplate");
        powerArmorLegs = MPSRegistry.registerItem(new ItemPowerArmorLeggings(), "powerArmorLegs", "powerArmorLeggings");
        powerArmorFeet = MPSRegistry.registerItem(new ItemPowerArmorBoots(), "powerArmorFeet", "powerArmorBoots");
        powerTool     =  MPSRegistry.registerItem(new ItemPowerFist(), "powerTool", "powerFist");

        tinkerTable = MPSRegistry.registerBlock(new BlockTinkerTable());
        luxCapacitor = MPSRegistry.registerBlock(new BlockLuxCapacitor());
        testBlock = MPSRegistry.registerBlock(new TestBlock());
    }

    public static void populateComponents() {
        components = MPSRegistry.registerItem(new ItemComponent(), "powerArmorComponent", "powerArmorComponent");
        ((ItemComponent)components).populate();
    }
 }