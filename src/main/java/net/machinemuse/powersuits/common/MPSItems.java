package net.machinemuse.powersuits.common;

import cpw.mods.fml.common.registry.GameRegistry;
import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.item.*;
/**
 * Created by Claire Semple on 9/9/2014.
 *
 * Ported to Java by lehjr on 10/22/16.
 */

public final class MPSItems {
    private static MPSItems ourInstance = new MPSItems();
    public static MPSItems getInstance() {
        return ourInstance;
    }

    public final ItemPowerArmorHelmet powerArmorHead;
    public final ItemPowerArmorChestplate powerArmorTorso;
    public final ItemPowerArmorLeggings powerArmorLegs;
    public final ItemPowerArmorBoots powerArmorFeet;
    public final ItemPowerFist powerTool;
    public final BlockTinkerTable tinkerTable;
    public final BlockLuxCapacitor luxCapacitor;
    public final ItemComponent components;

    private MPSItems() {
        this.powerArmorHead = new ItemPowerArmorHelmet();
        GameRegistry.registerItem(this.powerArmorHead, this.powerArmorHead.getUnlocalizedName());
        this.powerArmorTorso = new ItemPowerArmorChestplate();
        GameRegistry.registerItem(this.powerArmorTorso, this.powerArmorTorso.getUnlocalizedName());
        this.powerArmorLegs = new ItemPowerArmorLeggings();
        GameRegistry.registerItem(this.powerArmorLegs, this.powerArmorLegs.getUnlocalizedName());
        this.powerArmorFeet = new ItemPowerArmorBoots();
        GameRegistry.registerItem(this.powerArmorFeet, this.powerArmorFeet.getUnlocalizedName());
        this.powerTool = new ItemPowerFist();
        GameRegistry.registerItem(this.powerTool, this.powerTool.getUnlocalizedName());
        this.tinkerTable = new BlockTinkerTable();
        GameRegistry.registerBlock(this.tinkerTable, this.tinkerTable.getUnlocalizedName());
        this.luxCapacitor = new BlockLuxCapacitor();
        GameRegistry.registerBlock(this.luxCapacitor, this.luxCapacitor.getUnlocalizedName());
        this.components = new ItemComponent();
        this.components.setUnlocalizedName("powerArmorComponent");
        GameRegistry.registerItem(this.components, "powerArmorComponent");
        this.components.populate();
    }
}