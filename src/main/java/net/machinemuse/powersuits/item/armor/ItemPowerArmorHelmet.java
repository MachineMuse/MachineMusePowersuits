package net.machinemuse.powersuits.item.armor;

import net.minecraft.inventory.EntityEquipmentSlot;

public class ItemPowerArmorHelmet extends ItemPowerArmor {
    public ItemPowerArmorHelmet(String regName) {
        super(EntityEquipmentSlot.HEAD);
        setRegistryName(regName);
    }
}
