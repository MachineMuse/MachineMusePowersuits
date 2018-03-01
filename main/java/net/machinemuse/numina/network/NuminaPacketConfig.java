package net.machinemuse.numina.network;

import net.machinemuse.numina.common.config.NuminaServerSettings;
import net.machinemuse.numina.common.config.NuminaSettings;
import net.minecraft.entity.player.EntityPlayer;

import java.io.DataInputStream;

/**
 * Sync settings between server and client
 */
public class NuminaPacketConfig extends MusePacket {
    EntityPlayer player;

    public NuminaPacketConfig(EntityPlayer playerIn) {
        player = playerIn;
    }

    @Override
    public MusePackager packager() {
        return getPackagerInstance();
    }

    @Override
    public void write() {
        if (NuminaSettings.getServerSettings() == null)
            NuminaSettings.setServerSettings(new NuminaServerSettings());
        // write values to packet to send to the client
        NuminaSettings.getServerSettings().writeToBuffer(this);
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
        NuminaServerSettings settings;
        @Override
        public MusePacket read(DataInputStream datain, EntityPlayer player) {
             settings = new NuminaServerSettings(datain);
            NuminaSettings.setServerSettings(settings);
            return new NuminaPacketConfig(player);
        }
    }
}