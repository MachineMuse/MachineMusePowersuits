package net.machinemuse.powersuits.item;

import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
public class ItemPowerArmorChestplate extends ItemPowerArmor {

    public ItemPowerArmorChestplate() {
        super(0, EntityEquipmentSlot.CHEST);
        this.setUnlocalizedName("powerArmorChestplate");
    }

    public String iconpath() {
        String iconpath = MuseRenderer.ICON_PREFIX + "armortorso";
        return iconpath;
    }
}