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

	public MusePacketPlayerUpdate(EntityPlayer player, PlayerInputMap playerInput) {
		super((Player) player);
		playerInput.writeToStream(dataout);
	}

	public MusePacketPlayerUpdate(DataInputStream data, Player player) {
		super(data, player);
		PlayerInputMap map = PlayerInputMap.getInputMapFor(((EntityPlayer) player).username);
		map.readFromStream(datain);
	}

	@Override
	public void handleClient(EntityClientPlayerMP player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServer(EntityPlayerMP player) {
		MusePacketPlayerUpdate updatePacket = new MusePacketPlayerUpdate(player, inputMap);
		PacketDispatcher.sendPacketToAllPlayers(updatePacket.getPacket250());
	}
}
