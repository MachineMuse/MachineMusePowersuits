package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.item.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Claire Semple on 9/9/2014.
 *
 * Ported to Java by lehjr on 10/22/16.
 */

public final class MPSItems {
    static {
        new MPSItems();
    }

    private MPSItems() {
    }

//    private static MPSItems INSTANCE;
//    public static MPSItems getInstance() {
//        if (INSTANCE == null) {
//            INSTANCE = new MPSItems();
//        }
//        return INSTANCE;
//    }

    public static final Item powerArmorHead = MPSRegistry.registerItem(new ItemPowerArmorHelmet(), "powerArmorHead", "powerArmorHelmet");
    public static final Item powerArmorTorso =  MPSRegistry.registerItem(new ItemPowerArmorChestplate(), "powerArmorTorso", "powerArmorChestplate");
    public static final Item powerArmorLegs = MPSRegistry.registerItem(new ItemPowerArmorLeggings(), "powerArmorLegs", "powerArmorLeggings");
    public static final Item powerArmorFeet =  MPSRegistry.registerItem(new ItemPowerArmorBoots(), "powerArmorFeet", "powerArmorBoots");
    public static final Item powerTool =  MPSRegistry.registerItem(new ItemPowerFist(), "powerTool", "powerFist");

    public static final Block tinkerTable = MPSRegistry.registerBlock(new BlockTinkerTable());
    public static final Block luxCapacitor = MPSRegistry.registerBlock(new BlockLuxCapacitor());

    Item components = MPSRegistry.registerItem(new ItemComponent(), "powerArmorComponent", "powerArmorComponent");




//    public final ItemPowerArmorHelmet powerArmorHead;
//    public final ItemPowerArmorChestplate powerArmorTorso;
//    public final ItemPowerArmorLeggings powerArmorLegs;
//    public final ItemPowerArmorBoots powerArmorFeet;
//    public final ItemPowerFist powerTool;
//    public final BlockTinkerTable tinkerTable;
//    public final BlockLuxCapacitor luxCapacitor;
//    public final ItemComponent components;
//
//    public static MPSItems INSTANCE = new MPSItems();
//
//    private MPSItems() {
//        this.powerArmorHead = new ItemPowerArmorHelmet();
//        GameRegistry.registerItem(this.powerArmorHead, this.powerArmorHead.getUnlocalizedName());
//        this.powerArmorTorso = new ItemPowerArmorChestplate();
//        GameRegistry.registerItem(this.powerArmorTorso, this.powerArmorTorso.getUnlocalizedName());
//        this.powerArmorLegs = new ItemPowerArmorLeggings();
//        GameRegistry.registerItem(this.powerArmorLegs, this.powerArmorLegs.getUnlocalizedName());
//        this.powerArmorFeet = new ItemPowerArmorBoots();
//        GameRegistry.registerItem(this.powerArmorFeet, this.powerArmorFeet.getUnlocalizedName());
//        this.powerTool = new ItemPowerFist();
//        GameRegistry.registerItem(this.powerTool, this.powerTool.getUnlocalizedName());
//        this.tinkerTable = new BlockTinkerTable();
//        GameRegistry.registerBlock(this.tinkerTable, this.tinkerTable.getUnlocalizedName());
//        this.luxCapacitor = new BlockLuxCapacitor();
//        GameRegistry.registerBlock(this.luxCapacitor, this.luxCapacitor.getUnlocalizedName());
//        this.components = new ItemComponent();
//        this.components.setUnlocalizedName("powerArmorComponent");
//        GameRegistry.registerItem(this.components, "powerArmorComponent");
//        this.components.populate();
//    }
}