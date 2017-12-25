package net.machinemuse.powersuits.client.modelspec;

import com.google.common.base.Objects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class Binding {
    private final MorphTarget target;
    private final EntityEquipmentSlot slot;
    private final String itemState;

    public Binding(@Nullable MorphTarget target, @Nullable EntityEquipmentSlot slot, @Nullable String itemState) {
        this.target = target;
        this.slot = slot;
        this.itemState = (itemState != null || itemState !="")? itemState : "all";
    }

    public Binding(EntityEquipmentSlot slot, @Nullable String itemState) {
        this.target = null;
        this.slot = slot;
        this.itemState = (itemState != null || itemState !="")? itemState : "all";
    }

    @Nullable
    public MorphTarget getTarget() {
        return target;
    }

    public EntityEquipmentSlot getSlot() {
        return slot;
    }

    public String getItemState() {
        return itemState;
    }


    /** From NBT ---------------------------------------------------------------------------------- */
    public Binding(NBTTagCompound nbt) {
        this.target = getNBTTarget(nbt);
        this.slot = getNBTSlot(nbt);
        this.itemState = getNBTItemState(nbt);
    }

    MorphTarget getNBTTarget(NBTTagCompound nbt) {
        return nbt.hasKey("target") ? MorphTarget.getMorph(nbt.getString("target")) : null;
    }

    String getNBTItemState(NBTTagCompound nbt) {
        return nbt.hasKey("itemstate") ? nbt.getString("itemstate") :"all";
    }


    EntityEquipmentSlot getNBTSlot(NBTTagCompound nbt) {
        return (nbt.hasKey("target")) ? EntityEquipmentSlot.fromString(nbt.getString("target")) : getNBTTarget(nbt).slot;
    }

    /** To NBT ------------------------------------------------------------------------------------ */
    public NBTTagCompound toNBT() {
        NBTTagCompound nbt= new NBTTagCompound();
        nbt = setNBTTarget(nbt);
        nbt = setNBTSlot(nbt);
        nbt = setNBTItemState(nbt);
        return nbt;
    }

    NBTTagCompound setNBTTarget(NBTTagCompound nbt) {
        if(this.target != null)
            nbt.setString("target", this.target.name.toLowerCase());
        return nbt;
    }

    NBTTagCompound setNBTSlot(NBTTagCompound nbt) {
        if(this.target == null)
            nbt.setString("slot", this.slot.getName());
        return nbt;
    }

    NBTTagCompound setNBTItemState(NBTTagCompound nbt) {
        if(this.itemState != "all")
            nbt.setString("itemstate", this.itemState);
        return nbt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Binding binding = (Binding) o;
        return Objects.equal(getTarget(), binding.getTarget()) &&
                getSlot() == binding.getSlot() &&
                Objects.equal(getItemState(), binding.getItemState());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTarget(), getSlot(), getItemState());
    }
}