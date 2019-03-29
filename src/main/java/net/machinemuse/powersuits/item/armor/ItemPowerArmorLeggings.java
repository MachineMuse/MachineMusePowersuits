package net.machinemuse.powersuits.item.armor;

import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
public class ItemPowerArmorLeggings extends ItemPowerArmor {
    public final EntityEquipmentSlot armorType;

    public ItemPowerArmorLeggings(String regName) {
        super(regName, "powerArmorLeggings",0, EntityEquipmentSlot.LEGS);
        this.armorType = EntityEquipmentSlot.LEGS;
    }
}