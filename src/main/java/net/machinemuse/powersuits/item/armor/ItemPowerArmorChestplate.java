package net.machinemuse.powersuits.item.armor;

import net.minecraft.inventory.EntityEquipmentSlot;

public class ItemPowerArmorChestplate extends ItemPowerArmor {
    public ItemPowerArmorChestplate(String regName) {
        super(EntityEquipmentSlot.CHEST);
        setRegistryName(regName);
    }
}