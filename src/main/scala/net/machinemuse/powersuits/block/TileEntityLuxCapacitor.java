package net.machinemuse.powersuits.block;

import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.tileentity.MuseTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityLuxCapacitor extends MuseTileEntity {
    public double red;
    public double green;
    public double blue;

    public TileEntityLuxCapacitor() {
        side = ForgeDirection.DOWN;
        red = 0;
        green = 0.2;
        blue = 0.9;
    }

    public TileEntityLuxCapacitor(ForgeDirection side, double red, double green, double blue) {
        this.side = side;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("s", side.ordinal());
        nbt.setDouble("r", red);
        nbt.setDouble("g", green);
        nbt.setDouble("b", blue);

    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("s")) {
            side = ForgeDirection.values()[nbt.getInteger("s")];
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

    public ForgeDirection side;

    @Override
    public boolean canUpdate() {
        return false;
    }
}
