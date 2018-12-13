package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import net.machinemuse.numina.network.MuseByteBufferUtils;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.network.MPSPackets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

public class MusePacketPlayerUpdate implements IMessage {
    EntityPlayer player;
    PlayerInputMap inputMap;
    String username;

    public MusePacketPlayerUpdate() {
    }

    public MusePacketPlayerUpdate(EntityPlayer player, PlayerInputMap inputMap) {
        this.player = player;
        this.username = player.getCommandSenderEntity().getName();
        this.inputMap = inputMap;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.username = MuseByteBufferUtils.readUTF8String(buf);
        this.inputMap = PlayerInputMap.getInputMapFor(username);
        ByteBufInputStream byteBuf = new ByteBufInputStream(buf);
        this.inputMap.readFromStream(byteBuf);
        try {
            byteBuf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        MuseByteBufferUtils.writeUTF8String(buf, player.getCommandSenderEntity().getName());
        MuseByteBufferUtils.writePlayerInputMap(buf, inputMap);
    }

    public static class Handler implements IMessageHandler<MusePacketPlayerUpdate, IMessage> {
        @Override
        public IMessage onMessage(MusePacketPlayerUpdate message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                EntityPlayerMP player = ctx.getServerHandler().player;
                player.getServerWorld().addScheduledTask(() -> {
                    PlayerInputMap inputMap = message.inputMap;
                    MusePacketPlayerUpdate updatePacket = new MusePacketPlayerUpdate(player, inputMap);
                    player.motionX = inputMap.motionX;
                    player.motionY = inputMap.motionY;
                    player.motionZ = inputMap.motionZ;
                    MPSPackets.sendToAllAround(updatePacket, player,128); // <-- test me
                });
            }
            return null;
        }
    }
}
