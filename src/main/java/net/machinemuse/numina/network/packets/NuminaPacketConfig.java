package net.machinemuse.numina.network.packets;

import io.netty.buffer.ByteBuf;
import net.machinemuse.numina.common.config.NuminaConfig;
import net.machinemuse.numina.common.config.NuminaServerSettings;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class NuminaPacketConfig implements IMessage {
    public NuminaPacketConfig() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        NuminaServerSettings settings = new NuminaServerSettings(buf);
        NuminaConfig.INSTANCE.setServerSettings(settings);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (NuminaConfig.INSTANCE.getServerSettings() == null)
            NuminaConfig.INSTANCE.setServerSettings(new NuminaServerSettings());
        // write values to packet to send to the client
        NuminaConfig.INSTANCE.getServerSettings().writeToBuffer(buf);
    }

    public static class Handler implements IMessageHandler<NuminaPacketConfig , IMessage> {
        @Override
        public IMessage onMessage(NuminaPacketConfig message, MessageContext ctx) {
            return null;
        }
    }
}