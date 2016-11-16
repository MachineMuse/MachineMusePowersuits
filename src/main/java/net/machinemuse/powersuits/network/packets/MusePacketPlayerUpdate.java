package net.machinemuse.powersuits.network.packets;

import net.machinemuse.numina.network.MusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.DataInputStream;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketPlayerUpdate extends MusePacket {
    EntityPlayer player;
    PlayerInputMap inputMap;

    public MusePacketPlayerUpdate(EntityPlayer player, PlayerInputMap inputMap) {
        this.player = player;
        this.inputMap = inputMap;
    }

    @Override
    public MusePackager packager() {
        return getPackagerInstance();
    }

    @Override
    public void write() {
        writeString(player.getCommandSenderName());
        inputMap.writeToStream(dataout());
    }

    @Override
    public void handleServer(EntityPlayerMP player) {
        MusePacketPlayerUpdate updatePacket = new MusePacketPlayerUpdate(player, inputMap);
        player.motionX = inputMap.motionX;
        player.motionY = inputMap.motionY;
        player.motionZ = inputMap.motionZ;
        PacketSender.sendToAllAround(updatePacket, player, 128);
    }

    private static MusePacketPlayerUpdatePackager PACKAGERINSTANCE;
    public static MusePacketPlayerUpdatePackager getPackagerInstance() {
        if (PACKAGERINSTANCE == null)
            PACKAGERINSTANCE = new MusePacketPlayerUpdatePackager();
        return PACKAGERINSTANCE;
    }

    public static class MusePacketPlayerUpdatePackager extends MusePackager {
        @Override
        public MusePacket read(DataInputStream datain, EntityPlayer player) {
            String username = readString(datain);
            PlayerInputMap inputMap = PlayerInputMap.getInputMapFor(username);
            inputMap.readFromStream(datain);
            return new MusePacketPlayerUpdate(player, inputMap);
        }
    }
}