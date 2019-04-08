package net.machinemuse.powersuits.item.module.movement;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:10 AM, 8/7/13
 * <p>
 * Ported to Java by lehjr on 10/13/16.
 */
public class UUID {
    final long least;
    final long most;

    public UUID(long least, long most) {
        this.least = least;
        this.most = most;
    }

    public UUID(NBTTagCompound nbt) {
        this.least = nbt.getLong("UUIDLeast");
        this.most = nbt.getLong("UUIDMost");
    }

    public NBTTagCompound toNBT(NBTTagCompound nbt) {
        nbt.putLong("UUIDLeast", least);
        nbt.putLong("UUIDMost", most);
        return nbt;
    }
}