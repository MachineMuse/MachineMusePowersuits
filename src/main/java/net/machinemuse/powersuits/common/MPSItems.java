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
    public static Item powerArmorHead;
    public static Item powerArmorTorso;
    public static Item powerArmorLegs;
    public static Item powerArmorFeet;
    public static Item powerTool;

    public static Block tinkerTable;
    public static Block luxCapacitor;

    public static Item components;

    private MPSItems() {
        this.powerArmorHead = MPSRegistry.registerItem(new ItemPowerArmorHelmet(), "powerArmorHead", "powerArmorHelmet");
        this.powerArmorTorso = MPSRegistry.registerItem(new ItemPowerArmorChestplate(), "powerArmorTorso", "powerArmorChestplate");
        this.powerArmorLegs = MPSRegistry.registerItem(new ItemPowerArmorLeggings(), "powerArmorLegs", "powerArmorLeggings");
        this.powerArmorFeet = MPSRegistry.registerItem(new ItemPowerArmorBoots(), "powerArmorFeet", "powerArmorBoots");
        this.powerTool     =  MPSRegistry.registerItem(new ItemPowerFist(), "powerTool", "powerFist");

        this.tinkerTable = MPSRegistry.registerBlock(new BlockTinkerTable());
        this.luxCapacitor = MPSRegistry.registerBlock(new BlockLuxCapacitor());

        this.components = MPSRegistry.registerItem(new ItemComponent(), "powerArmorComponent", "powerArmorComponent");
        ((ItemComponent)components).populate();
    }

    private static MPSItems INSTANCE;
    public static MPSItems getInstance() {
        if (INSTANCE == null)
            INSTANCE = new MPSItems();
        return INSTANCE;
    }
}