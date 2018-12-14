package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBuf;
import net.machinemuse.numina.network.MuseByteBufferUtils;
import net.machinemuse.powersuits.common.ModuleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MusePacketToggleRequest implements IMessage {
    EntityPlayer player;
    String module;
    Boolean active;

    public MusePacketToggleRequest() {

    }


    public MusePacketToggleRequest(EntityPlayer player, String module, Boolean active) {
        this.player = player;
        this.module = module;
        this.active = active;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.module = MuseByteBufferUtils.readUTF8String(buf);
        this.active = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        MuseByteBufferUtils.writeUTF8String(buf, module);
        buf.writeBoolean(active);
    }

    public static class Handler implements IMessageHandler<MusePacketToggleRequest,IMessage> {
        @Override
        public IMessage onMessage(MusePacketToggleRequest message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                final EntityPlayerMP player = ctx.getServerHandler().player;
                player.getServerWorld().addScheduledTask(() -> {
                    String module = message.module;
                    Boolean active = message.active;
                    ModuleManager.INSTANCE.toggleModuleForPlayer(player, module, active);
                });
            }
            return null;
        }
    }
}
