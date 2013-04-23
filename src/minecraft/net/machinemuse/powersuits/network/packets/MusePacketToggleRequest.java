package net.machinemuse.powersuits.network.packets;

import cpw.mods.fml.common.network.Player;
import net.machinemuse.powersuits.network.MusePacket;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.DataInputStream;

public class MusePacketToggleRequest extends MusePacket {

    String module;
    boolean value;

    public MusePacketToggleRequest(Player player, String module, boolean active) {
        super(player);
        writeString(module);
        writeBoolean(active);
    }

    public MusePacketToggleRequest(DataInputStream data, Player player) {
        super(data, player);
        module = readString(64);
        value = readBoolean();
    }

    @Override
    public void handleClient(EntityClientPlayerMP player) {

    }

    @Override
    public void handleServer(EntityPlayerMP player) {
        MuseItemUtils.toggleModuleForPlayer(player, module, value);
    }

}
