package net.machinemuse.powersuits.item.armor;

import net.minecraft.inventory.EntityEquipmentSlot;

public class ItemPowerArmorLeggings extends ItemPowerArmor {
    public ItemPowerArmorLeggings(String regName) {
        super(EntityEquipmentSlot.LEGS);
        setRegistryName(regName);
    }
}