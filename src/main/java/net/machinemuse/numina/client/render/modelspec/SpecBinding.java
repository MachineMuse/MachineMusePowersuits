package net.machinemuse.numina.client.render.modelspec;

import net.minecraft.inventory.EntityEquipmentSlot;

import javax.annotation.Nullable;

public class SpecBinding {
    private final MorphTarget target;
    private final EntityEquipmentSlot slot;
    private final String itemStateString;

    public SpecBinding(@Nullable MorphTarget target, @Nullable EntityEquipmentSlot slot, @Nullable String itemState) {
        this.target = target;
        this.slot = slot;
        this.itemStateString = (itemState != null || itemState != "") ? itemState : "all";
    }

    public SpecBinding(EntityEquipmentSlot slot, @Nullable String itemState) {
        this.target = null;
        this.slot = slot;
        this.itemStateString = (itemState != null || itemState != "") ? itemState : "all";
    }

    @Nullable
    public MorphTarget getTarget() {
        return target;
    }

    public EntityEquipmentSlot getSlot() {
        return slot;
    }

    public String getItemState() {
        return itemStateString;
    }
}