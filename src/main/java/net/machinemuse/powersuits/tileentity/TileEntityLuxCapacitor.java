package net.machinemuse.powersuits.tileentity;

import net.machinemuse.numina.basemod.MuseLogger;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.tileentity.MuseTileEntity;
import net.machinemuse.powersuits.basemod.MPSItems;
import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.property.IExtendedBlockState;

public class TileEntityLuxCapacitor extends MuseTileEntity {
    private Colour color;

    public TileEntityLuxCapacitor() {
        super(MPSItems.capacitorTileEntityType);
        this.color = BlockLuxCapacitor.defaultColor;
    }

    public TileEntityLuxCapacitor(Colour colour) {
        super(MPSItems.capacitorTileEntityType);
        this.color = colour;
    }

    public void setColor(Colour colour) {
        this.color = colour;
    }

    @Override
    public NBTTagCompound write(NBTTagCompound nbt) {
        super.write(nbt);
        if (color == null)
            color = ((IExtendedBlockState) this.getWorld().getBlockState(this.getPos())).getValue(BlockLuxCapacitor.COLOR);
        if (color == null)
            color = BlockLuxCapacitor.defaultColor;
        nbt.putInt("c", color.getInt());
        return nbt;
    }

    @Override
    public void read(NBTTagCompound nbt) {
        super.read(nbt);
        if (nbt.contains("c")) {
            color = new Colour(nbt.getInt("c"));
        } else {
            MuseLogger.logger.debug("No NBT found! D:");
        }
    }

    public Colour getColor() {
        return color != null ? color : BlockLuxCapacitor.defaultColor;
    }
}
