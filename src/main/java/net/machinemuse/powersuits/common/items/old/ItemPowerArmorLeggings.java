package net.machinemuse.powersuits.common.items.old;

import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
public class ItemPowerArmorLeggings extends ItemPowerArmor {
    public final EntityEquipmentSlot armorType;
    public ItemPowerArmorLeggings() {
        super(0, EntityEquipmentSlot.LEGS);
        this.setUnlocalizedName("powerArmorLeggings");
        this.armorType = EntityEquipmentSlot.LEGS;
    }
}