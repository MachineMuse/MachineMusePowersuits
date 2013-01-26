package net.machinemuse.powersuits.network.packets;

import java.io.DataInputStream;

import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.network.MusePacket;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.Player;

public class MusePacketToggleRequest extends MusePacket {

	String module;
	boolean value;

	protected MusePacketToggleRequest(Player player, String module, boolean active) {
		super(player);
		writeString(module);
		writeBoolean(active);
	}

	protected MusePacketToggleRequest(Player player, DataInputStream data) {
		super(player, data);
		module = readString(64);
		value = readBoolean();
	}

	@Override
	public void handleClient(EntityClientPlayerMP player) {

	}

	@Override
	public void handleServer(EntityPlayerMP player) {
		ItemUtils.toggleModuleForPlayer(player, module, value);
	}

}
