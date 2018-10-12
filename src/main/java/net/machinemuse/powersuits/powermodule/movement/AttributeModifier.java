//package net.machinemuse.powersuits.powermodule.movement;
//
//import net.minecraft.inventory.EntityEquipmentSlot;
//import net.minecraft.nbt.NBTTagCompound;
//
//import java.util.Objects;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 10:10 AM, 8/7/13
// *
// * Created by leon on 10/18/16.
// */
//public class AttributeModifier {
//    final int operation;
//    final UUID uuid;
//    final double amount;
//    final String attributeName;
//    final String name;
//    final EntityEquipmentSlot slot;
//
//    public AttributeModifier(int operation, UUID uuid, double amount, String attributeName, String name, EntityEquipmentSlot slotIn) {
//        this.operation = operation;
//        this.uuid = uuid;
//        this.amount = amount;
//        this.attributeName = attributeName;
//        this.name = name;
//        this.slot = slotIn;
//    }
//
//    public AttributeModifier(NBTTagCompound nbt) {
//        this.operation = nbt.getInteger("Operation");
//        this.uuid = new UUID(nbt);
//        this.amount = nbt.getDouble("Amount");
//        this.attributeName = nbt.getString("AttributeName");
//        this.name = nbt.getString("Name");
//        this.slot = EntityEquipmentSlot.fromString(!Objects.equals(nbt.getString("Slot"), "") ? nbt.getString("Slot").toLowerCase() : "legs" );
//    }
//
//    public NBTTagCompound toNBT(NBTTagCompound nbt) {
//        nbt.setInteger("Operation", operation);
//        uuid.toNBT(nbt);
//        nbt.setDouble("Amount", amount);
//        nbt.setString("AttributeName", attributeName);
//        nbt.setString("Name", name);
//        nbt.setString("Slot", slot.getName());
//        return nbt;
//    }
//
//    public NBTTagCompound toNBT() {
//        return toNBT(new NBTTagCompound());
//    }
//}