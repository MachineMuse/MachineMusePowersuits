package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBufInputStream;
import net.machinemuse.numina.network.IMusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.common.config.MPSServerSettings;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Sync settings between server and client
 */
public class MPSPacketConfig extends MusePacket {
    EntityPlayer player;

    public MPSPacketConfig(EntityPlayer playerIn) {
        player = playerIn;
    }

    @Override
    public IMusePackager packager() {
        return MPSPacketConfigPackager.INSTANCE;
    }

    @Override
    public void write() {
        if (MPSConfig.INSTANCE.getServerSettings() == null)
            MPSConfig.INSTANCE.setServerSettings(new MPSServerSettings());
        // write values to packet to send to the client
        MPSConfig.INSTANCE.getServerSettings().writeToBuffer(this);
    }

    public static MPSPacketConfigPackager getPackagerInstance() {
        return MPSPacketConfigPackager.INSTANCE;
    }

    public enum MPSPacketConfigPackager implements IMusePackager {
        INSTANCE;

        // The packet is read on the client side to copy the settings from the server to the client
        MPSServerSettings settings;
        @Override
        public MusePacket read(ByteBufInputStream datain, EntityPlayer player) {
            settings = new MPSServerSettings(datain);
            MPSConfig.INSTANCE.setServerSettings(settings);
            return new MPSPacketConfig(player);
        }
    }
}