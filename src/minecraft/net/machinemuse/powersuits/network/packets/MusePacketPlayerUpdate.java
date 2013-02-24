package net.machinemuse.powersuits.network.packets;

import java.io.DataInputStream;

import net.machinemuse.powersuits.common.PlayerInputMap;
import net.machinemuse.powersuits.network.MusePacket;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class MusePacketPlayerUpdate extends MusePacket {
	protected PlayerInputMap inputMap;
	protected String username;

	public MusePacketPlayerUpdate(EntityPlayer player, PlayerInputMap playerInput) {
		super((Player) player);
		writeString(player.username);
		playerInput.writeToStream(dataout);
	}

	public MusePacketPlayerUpdate(DataInputStream data, Player player) {
		super(data, player);
		username = readString(64);
		inputMap = PlayerInputMap.getInputMapFor(username);
		inputMap.readFromStream(datain);
	}

	@Override
	public void handleClient(EntityClientPlayerMP player) {
	}

	@Override
	public void handleServer(EntityPlayerMP player) {
		MusePacketPlayerUpdate updatePacket = new MusePacketPlayerUpdate(player, inputMap);
		updatePacket.username = this.username;
		player.motionX = inputMap.motionX;
		player.motionY = inputMap.motionY;
		player.motionZ = inputMap.motionZ;
		PacketDispatcher.sendPacketToAllPlayers(updatePacket.getPacket250());
	}
}
