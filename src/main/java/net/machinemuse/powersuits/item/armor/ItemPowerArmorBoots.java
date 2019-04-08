package net.machinemuse.powersuits.item.armor;

import net.minecraft.inventory.EntityEquipmentSlot;

public class ItemPowerArmorBoots extends ItemPowerArmor {
    public ItemPowerArmorBoots(String regName) {
        super(EntityEquipmentSlot.FEET);
        setRegistryName(regName);
    }
}