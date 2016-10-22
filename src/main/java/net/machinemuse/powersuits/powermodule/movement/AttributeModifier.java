package net.machinemuse.powersuits.powermodule.movement;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:10 AM, 8/7/13
 *
 * Created by leon on 10/18/16.
 */
public class AttributeModifier {
    final int operation;
    final UUID uuid;
    final double amount;
    final String attributeName;
    final String name;

    public AttributeModifier(int operation, UUID uuid, double amount, String attributeName, String name) {
        this.operation = operation;
        this.uuid = uuid;
        this.amount = amount;
        this.attributeName = attributeName;
        this.name = name;
    }

    public AttributeModifier(NBTTagCompound nbt) {
        this.operation = nbt.getInteger("Operation");
        this.uuid = new UUID(nbt);
        this.amount = nbt.getDouble("Amount");
        this.attributeName = nbt.getString("AttributeName");
        this.name = nbt.getString("Name");
    }

    public NBTTagCompound toNBT(NBTTagCompound nbt) {
        nbt.setInteger("Operation", operation);
        uuid.toNBT(nbt);
        nbt.setDouble("Amount", amount);
        nbt.setString("AttributeName", attributeName);
        nbt.setString("Name", name);
        return nbt;
    }

    public NBTTagCompound toNBT() {
        return toNBT(new NBTTagCompound());
    }
}