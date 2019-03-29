package net.machinemuse.powersuits.block;

import net.machinemuse.numina.basemod.MuseLogger;
import net.machinemuse.numina.tileentity.MuseTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

/**
 * @author MachineMuse
 * <p>
 * Ported to Java by lehjr on 10/21/16.
 */
public class TileEntityTinkerTable extends MuseTileEntity {
    EnumFacing facing;

    public TileEntityTinkerTable() {
        this.facing = EnumFacing.NORTH;
    }

    public TileEntityTinkerTable(EnumFacing facing) {
        this.facing = facing;
    }

    public EnumFacing getFacing() {
        return (this.facing != null) ? this.facing : EnumFacing.NORTH;
    }

    public void setFacing(EnumFacing facing) {
        this.facing = facing;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("f", facing.ordinal());
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("f")) {
            facing = EnumFacing.values()[nbt.getInteger("f")];
        } else {
            MuseLogger.logDebug("No NBT found! D:");
        }
    }
}