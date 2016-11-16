package net.machinemuse.powersuits.network.packets;

import net.machinemuse.numina.network.MusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.DataInputStream;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketToggleRequest extends MusePacket{
    EntityPlayer player;
    String module;
    Boolean active;
    public MusePacketToggleRequest(EntityPlayer player, String module, Boolean active) {
        this.player= player;
        this.module = module;
        this.active = active;
    }

    @Override
    public MusePackager packager() {
        return getPackagerInstance();
    }

    @Override
    public void write() {
        writeString(module);
        writeBoolean(active);
    }

    @Override
    public void handleServer(EntityPlayerMP player) {
        MuseItemUtils.toggleModuleForPlayer(player, module, active);
    }

    private static MusePacketToggleRequestPackager PACKAGERINSTANCE;
    public static MusePacketToggleRequestPackager getPackagerInstance() {
        if (PACKAGERINSTANCE == null)
            PACKAGERINSTANCE = new MusePacketToggleRequestPackager();
        return PACKAGERINSTANCE;
    }

    public static class MusePacketToggleRequestPackager extends MusePackager {
        @Override
        public MusePacket read(DataInputStream datain, EntityPlayer player) {
            String module = readString(datain);
            boolean value = readBoolean(datain);
            return new MusePacketToggleRequest(player, module, value);
        }
    }
}