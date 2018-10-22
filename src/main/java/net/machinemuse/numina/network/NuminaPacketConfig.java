package net.machinemuse.numina.network;

import io.netty.buffer.ByteBufInputStream;
import net.machinemuse.numina.common.config.NuminaConfig;
import net.machinemuse.numina.common.config.NuminaServerSettings;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Sync settings between server and client
 */
public class NuminaPacketConfig extends MusePacket {
    EntityPlayer player;

    public NuminaPacketConfig(EntityPlayer playerIn) {
        player = playerIn;
    }

    public static MusePacketConfigPackager getPackagerInstance() {
        return MusePacketConfigPackager.INSTANCE;
    }

    @Override
    public IMusePackager packager() {
        return MusePacketConfigPackager.INSTANCE;
    }

    @Override
    public void write() {
        if (NuminaConfig.INSTANCE.getServerSettings() == null)
            NuminaConfig.INSTANCE.setServerSettings(new NuminaServerSettings());
        // write values to packet to send to the client
        NuminaConfig.INSTANCE.getServerSettings().writeToBuffer(this);
    }

    public enum MusePacketConfigPackager implements IMusePackager {
        INSTANCE;

        NuminaServerSettings settings;

        @Override
        public MusePacket read(ByteBufInputStream datain, EntityPlayer player) {
            settings = new NuminaServerSettings(datain);
            NuminaConfig.INSTANCE.setServerSettings(settings);
            return new NuminaPacketConfig(player);
        }
    }
}