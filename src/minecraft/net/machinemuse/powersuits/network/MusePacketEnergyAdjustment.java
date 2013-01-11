package net.machinemuse.powersuits.network;

import java.io.DataInputStream;

import net.machinemuse.powersuits.item.ItemUtils;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.Player;

public class MusePacketEnergyAdjustment extends MusePacket {

	protected double drain;
	protected EntityPlayer entityPlayer;

	public MusePacketEnergyAdjustment(EntityPlayer player, double drain) {
		super((Player) player);
		writeDouble(drain);
	}

	public MusePacketEnergyAdjustment(DataInputStream data, Player player) {
		super(player, data);
		drain = readDouble();
	}

	@Override
	public void handleClient(EntityClientPlayerMP player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServer(EntityPlayerMP player) {
		ItemUtils.drainPlayerEnergy(player, drain);
	}
}
