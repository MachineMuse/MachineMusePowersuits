package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import net.machinemuse.numina.network.MuseByteBufferUtils;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.network.MPSPackets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MusePacketPlayerUpdate implements IMessage {
    EntityPlayer player;
    PlayerInputMap inputMap;

    public MusePacketPlayerUpdate(EntityPlayer player, PlayerInputMap inputMap) {
        this.player = player;
        this.inputMap = inputMap;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        String username = MuseByteBufferUtils.readUTF8String(buf);
        PlayerInputMap inputMap = PlayerInputMap.getInputMapFor(username);
        inputMap.readFromStream(new ByteBufInputStream(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        MuseByteBufferUtils.writeUTF8String(buf, player.getCommandSenderEntity().getName());
        MuseByteBufferUtils.writePlayerInputMap(buf, inputMap);
    }

    public static class Handler implements IMessageHandler<MusePacketPlayerUpdate, IMessage> {
        @Override
        public IMessage onMessage(MusePacketPlayerUpdate message, MessageContext ctx) {
            if (ctx.side != Side.SERVER) {
                MuseLogger.logError("Colour Info Packet sent to wrong side: " + ctx.side);
                return null;
            }

            EntityPlayerMP player = ctx.getServerHandler().player;
            PlayerInputMap inputMap = message.inputMap;

            MusePacketPlayerUpdate updatePacket = new MusePacketPlayerUpdate(player, inputMap);
            player.motionX = inputMap.motionX;
            player.motionY = inputMap.motionY;
            player.motionZ = inputMap.motionZ;
            MPSPackets.sendToAllAround(updatePacket, player, 128);



            return null;
        }
    }
}
