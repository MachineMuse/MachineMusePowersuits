package net.machinemuse.powersuits.network;

import net.machinemuse.numina.network.MusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.powersuits.common.config.MPSSettings;
import net.machinemuse.powersuits.common.config.ServerSettings;
import net.minecraft.entity.player.EntityPlayer;

import java.io.DataInputStream;

/**
 * Sync settings between server and client
 */
public class MusePacketConfig extends MusePacket {
    EntityPlayer player;

    public MusePacketConfig(EntityPlayer playerIn) {
        player = playerIn;
    }

    @Override
    public MusePackager packager() {
        return getPackagerInstance();
    }

    @Override
    public void write() {
        if (MPSSettings.getServerSettings() == null)
            MPSSettings.setServerSettings(new ServerSettings());
        // write values to packet to send to the client
        MPSSettings.getServerSettings().writeToBuffer(this);
    }

    private static MusePacketConfigPackager PACKAGERINSTANCE;

    public static MusePacketConfigPackager getPackagerInstance() {
        if (PACKAGERINSTANCE == null) {
            synchronized (MusePacketConfigPackager.class) {
                if (PACKAGERINSTANCE == null) {
                    PACKAGERINSTANCE = new MusePacketConfigPackager();
                }
            }
        }
        return PACKAGERINSTANCE;
    }

    public static final class MusePacketConfigPackager extends MusePackager {
        ServerSettings settings;
        @Override
        public MusePacket read(DataInputStream datain, EntityPlayer player) {
             settings = new ServerSettings(datain);
            MPSSettings.setServerSettings(settings);
            return new  MusePacketConfig(player);
        }
    }
}