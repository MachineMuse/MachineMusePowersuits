package net.machinemuse.numina.network.packets;

import io.netty.buffer.ByteBuf;
import net.machinemuse.numina.item.IModeChangingItem;
import net.machinemuse.numina.module.IModuleManager;
import net.machinemuse.numina.network.MuseByteBufferUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MusePacketModeChangeRequest implements IMessage {
    protected EntityPlayer player;
    protected String mode;
    protected int slot;

    public MusePacketModeChangeRequest(EntityPlayer player, String mode, int slot) {
        this.player = player;
        this.mode = mode;
        this.slot = slot;
    }

    public MusePacketModeChangeRequest() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.mode = MuseByteBufferUtils.readUTF8String(buf);
        this.slot = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        MuseByteBufferUtils.writeUTF8String(buf, mode);
        buf.writeInt(slot);
    }

    public static class Handler implements IMessageHandler<MusePacketModeChangeRequest, IMessage> {
        @Override
        public IMessage onMessage(MusePacketModeChangeRequest message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                EntityPlayerMP player = ctx.getServerHandler().player;
                player.getServerWorld().addScheduledTask(() -> {
                    int slot = message.slot;
                    String mode = message.mode;
                    if (slot > -1 && slot < 9) {
                        ItemStack stack = player.inventory.mainInventory.get(slot);
                        if (!stack.isEmpty() && stack.getItem() instanceof IModeChangingItem) {
                            IModeChangingItem modeChangingItem = ((IModeChangingItem) stack.getItem());
                            IModuleManager moduleManager = modeChangingItem.getModuleManager();
                            if (moduleManager.isValidForItem(stack, mode))
                                modeChangingItem.setActiveMode(stack, mode);
                        }
                    }
                });
            }
            return null;
        }
    }
}