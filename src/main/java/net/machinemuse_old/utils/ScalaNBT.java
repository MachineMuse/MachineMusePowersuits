package net.machinemuse_old.utils;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 5:47 AM, 29/04/13
 *
 * Ported to Java by lehjr on 10/21/16.
 */
public class ScalaNBT extends NBTTagCompound {
    private final NBTTagCompound nbt;

    public NBTTagCompound nbt() {
        return this.nbt;
    }

    public ScalaNBT wrap(final NBTTagCompound nbt) {
        return new ScalaNBT(nbt);
    }

    public ScalaNBT(final NBTTagCompound nbt) {
        this.nbt = nbt;
    }
}