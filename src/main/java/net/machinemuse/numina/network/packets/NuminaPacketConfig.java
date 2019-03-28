package net.machinemuse.numina.network.packets;

import io.netty.buffer.ByteBuf;
import net.machinemuse.numina.config.NuminaConfig;
import net.machinemuse.numina.config.NuminaServerSettings;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class NuminaPacketConfig implements IMessage {
    NuminaServerSettings settings;
    public NuminaPacketConfig() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        settings = new NuminaServerSettings(buf);
        NuminaConfig.INSTANCE.setServerSettings(settings);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        settings = NuminaConfig.INSTANCE.getServerSettings();
        if (settings == null) {
            settings = new NuminaServerSettings();
            NuminaConfig.INSTANCE.setServerSettings(settings);
        }
        settings.writeToBuffer(buf);
    }

    public static class Handler implements IMessageHandler<NuminaPacketConfig , IMessage> {
        @Override
        public IMessage onMessage(NuminaPacketConfig message, MessageContext ctx) {
            return null;
        }
    }
}