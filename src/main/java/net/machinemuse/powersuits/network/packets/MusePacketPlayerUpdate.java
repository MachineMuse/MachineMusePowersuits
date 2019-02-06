package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBuf;
import net.machinemuse.numina.capabilities.player.CapabilityPlayerValues;
import net.machinemuse.numina.capabilities.player.IPlayerValues;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MusePacketPlayerUpdate implements IMessage {
    EntityPlayer player;
    boolean downkeyState;
    boolean jumpKeyState;

    public MusePacketPlayerUpdate() {
    }

    public MusePacketPlayerUpdate(EntityPlayer player, boolean downKeyState, boolean jumpkeyState) {
        this.player = player;
        this.downkeyState = downKeyState;
        this.jumpKeyState = jumpkeyState;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.downkeyState = buf.readBoolean();
        this.jumpKeyState = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(downkeyState);
        buf.writeBoolean(jumpKeyState);
    }

    public boolean getDownKeyState() {
        return downkeyState;
    }

    public boolean getJumpKeyState() {
        return jumpKeyState;
    }

    public static class Handler implements IMessageHandler<MusePacketPlayerUpdate, IMessage> {
        @Override
        public IMessage onMessage(MusePacketPlayerUpdate message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                EntityPlayerMP player = ctx.getServerHandler().player;
                player.getServerWorld().addScheduledTask(() -> {
                    IPlayerValues playerCap = player.getCapability(CapabilityPlayerValues.PLAYER_VALUES, null);
                    if (playerCap != null) {
                        playerCap.setDownKeyState(message.getDownKeyState());
                        playerCap.setJumpKeyState(message.getJumpKeyState());
                    }
                });
            }
            return null;
        }
    }
}