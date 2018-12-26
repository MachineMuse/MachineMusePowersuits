package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBuf;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.common.config.MPSServerSettings;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Sync settings between server and client
 */
public class MPSPacketConfig implements IMessage {
    public MPSPacketConfig() {

    }

    // read settings from packet
    @Override
    public void fromBytes(ByteBuf buf) {
        MPSServerSettings settings = new MPSServerSettings(buf);
        MPSConfig.INSTANCE.setServerSettings(settings);
    }

    // write values to packet to send to the client
    @Override
    public void toBytes(ByteBuf buf) {
        if (MPSConfig.INSTANCE.getServerSettings() == null)
            MPSConfig.INSTANCE.setServerSettings(new MPSServerSettings());
        MPSConfig.INSTANCE.getServerSettings().writeToBuffer(buf);
    }

    public static class Handler implements IMessageHandler<MPSPacketConfig, IMessage> {
        @Override
        public IMessage onMessage(MPSPacketConfig message, MessageContext ctx) {
            return null;
        }
    }
}