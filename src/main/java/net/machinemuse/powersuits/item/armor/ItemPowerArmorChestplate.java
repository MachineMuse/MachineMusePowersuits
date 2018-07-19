package net.machinemuse.powersuits.item.armor;

import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
public class ItemPowerArmorChestplate extends ItemPowerArmor {
    public final EntityEquipmentSlot armorType;
    public ItemPowerArmorChestplate() {
        super(0, EntityEquipmentSlot.CHEST);
        this.setUnlocalizedName("powerArmorChestplate");
        this.armorType = EntityEquipmentSlot.CHEST;
    }
}