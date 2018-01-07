package net.machinemuse.powersuits.common.tileentities;

import net.machinemuse.numina.common.tileentity.MuseTileEntity;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.powersuits.client.helpers.EnumColour;
import net.machinemuse.powersuits.common.block.BlockLuxCapacitor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.property.IExtendedBlockState;

public class TileEntityLuxCapacitor extends MuseTileEntity {
    private EnumColour color;
    public TileEntityLuxCapacitor() {
        this.color = BlockLuxCapacitor.defaultColor;
    }

    public TileEntityLuxCapacitor(EnumColour colour) {
        this.color = colour;
    }

    public EnumColour getColor(){
        return color != null ? color : BlockLuxCapacitor.defaultColor;
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        if (nbt.hasKey("c")) {
            color = EnumColour.getColourEnumFromIndex(Byte.toUnsignedInt(nbt.getByte("c")));
        } else {
            MuseLogger.logDebug("No NBT found! D:");
        }
    }

    @Override
    public NBTTagCompound writeNBTData(NBTTagCompound nbt) {
        if (color == null)
            color = ((IExtendedBlockState)this.getWorld().getBlockState(this.getPos())).getValue(BlockLuxCapacitor.COLOUR);
        if (color == null)
            color = BlockLuxCapacitor.defaultColor;
        nbt.setByte("c", (byte) color.getIndex());
        return nbt;
    }
}
