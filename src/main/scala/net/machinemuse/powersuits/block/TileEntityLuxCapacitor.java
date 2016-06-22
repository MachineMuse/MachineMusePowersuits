package net.machinemuse.powersuits.block;

import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.tileentity.MuseTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityLuxCapacitor extends MuseTileEntity {
    public double red;
    public double green;
    public double blue;
    public EnumFacing side;

    public TileEntityLuxCapacitor() {
        side = EnumFacing.DOWN;
        red = 0;
        green = 0.2;
        blue = 0.9;
    }

    public TileEntityLuxCapacitor(EnumFacing side, double red, double green, double blue) {
        this.side = side;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("s", side.ordinal());
        nbt.setDouble("r", red);
        nbt.setDouble("g", green);
        nbt.setDouble("b", blue);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("s")) {
            side = EnumFacing.values()[nbt.getInteger("s")];
        } else {
            MuseLogger.logDebug("No NBT found! D:");
        }
        if (nbt.hasKey("r")) {
            red = nbt.getDouble("r");
        } else {
            MuseLogger.logDebug("No NBT found! D:");
        }
        if (nbt.hasKey("g")) {
            green = nbt.getDouble("g");
        } else {
            MuseLogger.logDebug("No NBT found! D:");
        }
        if (nbt.hasKey("b")) {
            blue = nbt.getDouble("b");
        } else {
            MuseLogger.logDebug("No NBT found! D:");
        }
    }

    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return (oldState.getBlock() != newSate.getBlock());
    }
//
//    @Override
//    public boolean canUpdate() {
//        return false;
//    }
}
