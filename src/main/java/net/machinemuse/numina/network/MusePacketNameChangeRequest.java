package net.machinemuse.numina.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.DataInputStream;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:50 PM, 9/6/13
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketNameChangeRequest extends MusePacket {
    String username;
    String newnick;
    int entityID;

    public MusePacketNameChangeRequest(EntityPlayer player, String username, String newnick, int entityID) {
        this.username = username;
        this.newnick = newnick;
        this.entityID = entityID;
    }

    @Override
    public MusePackager packager() {
        return getPackagerInstance();
    }

    @Override
    public void write() {
        writeString(username);
        writeString(newnick);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleClient(EntityPlayer player) {
        EntityPlayer anotherPlayer = (EntityPlayer) player.worldObj.getEntityByID(entityID);
        anotherPlayer.refreshDisplayName();
    }

    private static MusePacketNameChangeRequestPackager PACKAGERINSTANCE;

    public static MusePacketNameChangeRequestPackager getPackagerInstance() {
        if (PACKAGERINSTANCE == null)
            PACKAGERINSTANCE = new MusePacketNameChangeRequestPackager();
        return PACKAGERINSTANCE;
    }

    public static class MusePacketNameChangeRequestPackager extends MusePackager {
        @Override
        public MusePacket read(DataInputStream datain, EntityPlayer player) {
            String username = readString(datain);
            String newnick = readString(datain);
            return new MusePacketNameChangeRequest(player, username, newnick, 0);
        }
    }
}