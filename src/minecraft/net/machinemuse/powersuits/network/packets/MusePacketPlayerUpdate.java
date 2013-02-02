package net.machinemuse.powersuits.network.packets;

import java.io.DataInputStream;

import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.powersuits.event.MovementManager;
import net.machinemuse.powersuits.network.MusePacket;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.Player;

public class MusePacketPlayerUpdate extends MusePacket {

	protected double energyAdjustment;
	protected double exhaustionAdjustment;
	protected double motionX;
	protected double motionY;
	protected double motionZ;
	protected EntityPlayer entityPlayer;

	public MusePacketPlayerUpdate(EntityPlayer player, double energyAdjustment, double exhaustionAdjustment) {
		super((Player) player);
		writeDouble(energyAdjustment);
		writeDouble(exhaustionAdjustment);
		writeDouble(player.motionX);
		writeDouble(player.motionY);
		writeDouble(player.motionZ);
	}

	public MusePacketPlayerUpdate(DataInputStream data, Player player) {
		super(data, player);
		energyAdjustment = readDouble();
		exhaustionAdjustment = readDouble();
		motionX = readDouble();
		motionY = readDouble();
		motionZ = readDouble();
	}

	@Override
	public void handleClient(EntityClientPlayerMP player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServer(EntityPlayerMP player) {
		if (energyAdjustment < 0) {
			MuseItemUtils.drainPlayerEnergy(player, -energyAdjustment);
		}
		if (energyAdjustment > 0) {
			MuseItemUtils.givePlayerEnergy(player, energyAdjustment);
		}
		if (exhaustionAdjustment != 0) {
			player.addExhaustion((float) exhaustionAdjustment);
		}

		player.fallDistance = (float) MovementManager.computeFallHeightFromVelocity(motionY);
		player.motionX = motionX;
		player.motionY = motionY;
		player.motionZ = motionZ;
		
	}
}
