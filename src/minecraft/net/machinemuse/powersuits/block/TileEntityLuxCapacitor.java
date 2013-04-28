package net.machinemuse.powersuits.block;

import net.machinemuse.general.MuseLogger;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityLuxCapacitor extends TileEntity {
	public TileEntityLuxCapacitor() {
		side = ForgeDirection.DOWN;
	}

	public TileEntityLuxCapacitor(ForgeDirection side) {
		this.side = side;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("s", side.ordinal());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("s")) {
			side = ForgeDirection.values()[nbt.getInteger("s")];
		} else {
			MuseLogger.logDebug("No NBT found! D:");
		}
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
	{
		readFromNBT(pkt.customParam1);

		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);
	}

	public ForgeDirection side;

    @Override
    public boolean canUpdate() {
        return false;
    }
}
