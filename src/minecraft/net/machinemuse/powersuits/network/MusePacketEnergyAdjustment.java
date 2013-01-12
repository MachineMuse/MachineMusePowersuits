package net.machinemuse.powersuits.network;

import java.io.DataInputStream;

import net.machinemuse.powersuits.common.MuseLogger;
import net.machinemuse.powersuits.item.ItemUtils;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;

public class MusePacketEnergyAdjustment extends MusePacket {

	protected double adjustment;
	protected EntityPlayer entityPlayer;

	public MusePacketEnergyAdjustment(EntityPlayer player, double adjustment) {
		super((Player) player);
		writeDouble(adjustment);
	}

	public MusePacketEnergyAdjustment(DataInputStream data, Player player) {
		super(player, data);
		adjustment = readDouble();
	}

	@Override
	public void handleClient(EntityClientPlayerMP player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServer(EntityPlayerMP player) {
		MuseLogger.logDebug(FMLCommonHandler.instance().getEffectiveSide() + ": Adjustment:  " + adjustment + " from " + player);
		if (adjustment < 0) {
			ItemUtils.drainPlayerEnergy(player, -adjustment);
		}
		if (adjustment > 0) {
			ItemUtils.drainPlayerEnergy(player, -adjustment);
		}
	}
}
