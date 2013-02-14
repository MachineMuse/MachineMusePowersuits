package net.machinemuse.powersuits.network.packets;

import java.io.DataInputStream;

import net.machinemuse.powersuits.entity.EntityBlinkDriveBolt;
import net.machinemuse.powersuits.network.MusePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.Player;

public class MusePacketBlinkDriveBolt extends MusePacket {
	protected int entityID;
	protected double size;

	public MusePacketBlinkDriveBolt(Player player, int entityID, double size) {
		super(player);
		writeInt(entityID);
		writeDouble(size);

	}

	public MusePacketBlinkDriveBolt(DataInputStream data, Player player) {
		super(data, player);
		entityID = readInt();
		size = readDouble();
	}

	@Override
	public void handleClient(EntityClientPlayerMP player) {
		try {
			EntityBlinkDriveBolt entity = (EntityBlinkDriveBolt) Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
			entity.size = this.size;
		} catch (Exception e) {
			return;
		}

	}

	@Override
	public void handleServer(EntityPlayerMP player) {
		// TODO Auto-generated method stub

	}

}
