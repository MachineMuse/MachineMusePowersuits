package net.machinemuse.powersuits.block;

import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.tileentity.MuseTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;


public class TileEntityLuxCapacitor extends MuseTileEntity {
    private EnumFacing side;
    private Colour color = new Colour(0);

    public TileEntityLuxCapacitor() {
        side = EnumFacing.UP;
        this.color.r = 0;
        this.color.g = 0.2;
        this.color.b = 0.9;
        this.color.a = 1;
    }

    public TileEntityLuxCapacitor(EnumFacing side, Colour color) {
        this.side = side;
        this.color = color;
    }

    public TileEntityLuxCapacitor(EnumFacing side, double red, double green, double blue) {
        this.side = side;
        this.color.r = red;
        this.color.g = green;
        this.color.b = blue;
        this.color.a = 1;
    }

    public EnumFacing getFacing() {
        return this.side;
    }

    public void setFacing(EnumFacing facing) {
        this.side = facing;
    }


    public Colour getColor() {
        return this.color;
    }

    public void setColor(Colour color) {
        this.color = color;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        if (nbt.hasKey("s")) {
            side = EnumFacing.values()[nbt.getInteger("s")];
        } else {
            MuseLogger.logDebug("No NBT found! D:");
        }

        if (nbt.hasKey("c")) {
            color = new Colour(nbt.getInteger("c"));

        } else {
            MuseLogger.logDebug("No NBT found! D:");
        }
        color.a = 1;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("s", side.getIndex());
        nbt.setInteger("c", color.getInt());

        nbt.setInteger("x", this.pos.getX());
        nbt.setInteger("y", this.pos.getY());
        nbt.setInteger("z", this.pos.getZ());

        return nbt;
    }
}
