package net.machinemuse.powersuits.network;

import java.io.DataInputStream;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.Player;

public class MusePacketFallDistance extends MusePacket {

	protected double distance;
	protected EntityPlayer entityPlayer;

	public MusePacketFallDistance(EntityPlayer player, double distance) {
		super((Player) player);
		writeDouble(distance);
	}

	public MusePacketFallDistance(DataInputStream data, Player player) {
		super(player, data);
		distance = readDouble();
	}

	@Override
	public void handleClient(EntityClientPlayerMP player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServer(EntityPlayerMP player) {
		player.fallDistance = (float) distance;
	}

}
