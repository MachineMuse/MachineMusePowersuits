package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBuf;
import net.machinemuse.numina.constants.ModelSpecTags;
import net.machinemuse.numina.item.IModularItem;
import net.machinemuse.numina.nbt.MuseNBTUtils;
import net.machinemuse.numina.network.MuseByteBufferUtils;
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

    public MusePacketColourInfo() {

    }

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
            if (ctx.side == Side.SERVER) {
                final EntityPlayerMP player = ctx.getServerHandler().player;
                player.getServerWorld().addScheduledTask(() -> {
                    int itemSlot = message.itemSlot;
                    int[] tagData = message.tagData;

                    ItemStack stack = player.inventory.getStackInSlot(itemSlot);
                    if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
                        NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
                        NBTTagCompound renderTag = itemTag.getCompoundTag(ModelSpecTags.TAG_RENDER);
                        if (renderTag == null) {
                            renderTag = new NBTTagCompound();
                            itemTag.setTag(ModelSpecTags.TAG_RENDER, renderTag);
                        }
                        if (renderTag != null)
                            renderTag.setIntArray(ModelSpecTags.TAG_COLOURS, tagData);
                    }
                });
            }
            return null;
        }
    }
}