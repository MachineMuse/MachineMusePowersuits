package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBuf;
import net.machinemuse.numina.common.constants.NuminaNBTConstants;
import net.machinemuse.numina.item.IModularItem;
import net.machinemuse.numina.network.MuseByteBufferUtils;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.powersuits.utils.nbt.MPSNBTUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 * <p>
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketColourInfo implements IMessage {
    EntityPlayer player;
    int itemSlot;
    int[] tagData;

    public MusePacketColourInfo(EntityPlayer player, int itemSlot, int[] tagData) {
        this.player = player;
        this.itemSlot = itemSlot;
        this.tagData = tagData;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.itemSlot = buf.readInt();
        this.tagData = MuseByteBufferUtils.readIntArray(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(itemSlot);
        MuseByteBufferUtils.writeIntArray(buf, tagData);
    }

    public static class Handler implements IMessageHandler<MusePacketColourInfo, IMessage> {
        @Override
        public IMessage onMessage(MusePacketColourInfo message, MessageContext ctx) {
            if (ctx.side != Side.SERVER) {
                MuseLogger.logError("Colour Info Packet sent to wrong side: " + ctx.side);
                return null;
            }
            int itemSlot = message.itemSlot;
            int[] tagData = message.tagData;
            EntityPlayerMP player = ctx.getServerHandler().player;

            ItemStack stack = player.inventory.getStackInSlot(itemSlot);
            if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
                NBTTagCompound renderTag = MPSNBTUtils.getMuseRenderTag(stack);
                renderTag.setIntArray(NuminaNBTConstants.TAG_COLOURS, tagData);
            }
            return null;
        }
    }
}
