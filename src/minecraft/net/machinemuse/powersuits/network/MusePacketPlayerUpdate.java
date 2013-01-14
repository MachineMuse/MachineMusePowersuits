package net.machinemuse.powersuits.network;

import java.io.DataInputStream;

import net.machinemuse.powersuits.item.ItemUtils;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.Player;

public class MusePacketPlayerUpdate extends MusePacket {

	protected double energyAdjustment;
	protected double exhaustionAdjustment;
	protected double newFallDistance;
	protected EntityPlayer entityPlayer;

	public MusePacketPlayerUpdate(EntityPlayer player, double energyAdjustment, double exhaustionAdjustment, double newFallDistance) {
		super((Player) player);
		writeDouble(energyAdjustment);
		writeDouble(exhaustionAdjustment);
		writeDouble(newFallDistance);
	}

	public MusePacketPlayerUpdate(DataInputStream data, Player player) {
		super(player, data);
		energyAdjustment = readDouble();
		exhaustionAdjustment = readDouble();
		newFallDistance = readDouble();
	}

	@Override
	public void handleClient(EntityClientPlayerMP player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServer(EntityPlayerMP player) {
		if (energyAdjustment < 0) {
			ItemUtils.drainPlayerEnergy(player, -energyAdjustment);
		}
		if (energyAdjustment > 0) {
			ItemUtils.givePlayerEnergy(player, energyAdjustment);
		}
		if (exhaustionAdjustment != 0) {
			player.addExhaustion((float) exhaustionAdjustment);
		}

		player.fallDistance = (float) newFallDistance;
	}
}
