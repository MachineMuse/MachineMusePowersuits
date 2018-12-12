package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBuf;
import net.machinemuse.numina.common.constants.NuminaNBTConstants;
import net.machinemuse.numina.item.IModularItem;
import net.machinemuse.numina.network.MuseByteBufferUtils;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 * <p>
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketCosmeticInfo implements IMessage {
    EntityPlayer player;
    int itemSlot;
    String tagName;
    NBTTagCompound tagData;

    public MusePacketCosmeticInfo(EntityPlayer player, int itemSlot, String tagName, NBTTagCompound tagData) {
        this.player = player;
        this.itemSlot = itemSlot;
        this.tagName = tagName;
        this.tagData = tagData;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.itemSlot = buf.readInt();
        this.tagName = MuseByteBufferUtils.readUTF8String(buf);
        this.tagData = MuseByteBufferUtils.readCompressedNBT(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(itemSlot);
        MuseByteBufferUtils.writeUTF8String(buf, tagName);
        MuseByteBufferUtils.writeCompressedNBT(buf, tagData);
    }

    public static class Handler implements IMessageHandler<MusePacketCosmeticInfo, IMessage> {
        @Override
        public IMessage onMessage(MusePacketCosmeticInfo message, MessageContext ctx) {
            if (ctx.side != Side.SERVER) {
                MuseLogger.logError("Colour Info Packet sent to wrong side: " + ctx.side);
                return null;
            }

            EntityPlayerMP player = ctx.getServerHandler().player;
            int itemSlot = message.itemSlot;
            String tagName = message.tagName;
            NBTTagCompound tagData = message.tagData;

            ItemStack stack = player.inventory.getStackInSlot(itemSlot);
            if (tagName != null && !stack.isEmpty() && stack.getItem() instanceof IModularItem) {
                NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);

                //cosmetic preset tag
                if(Objects.equals(tagName, NuminaNBTConstants.TAG_COSMETIC)) {
                    itemTag.removeTag(NuminaNBTConstants.TAG_RENDER);
                    itemTag.removeTag(NuminaNBTConstants.TAG_COSMETIC);
                    itemTag.setTag(NuminaNBTConstants.TAG_COSMETIC, tagData);

                    // setting complete render tag
                } else if (Objects.equals(tagName, NuminaNBTConstants.TAG_RENDER)) {
                    itemTag.removeTag(NuminaNBTConstants.TAG_RENDER);
                    itemTag.removeTag(NuminaNBTConstants.TAG_COSMETIC);
                    NBTTagCompound cosmeticTag = new NBTTagCompound();
                    cosmeticTag.setTag(NuminaNBTConstants.TAG_RENDER, tagData);
                    itemTag.setTag(NuminaNBTConstants.TAG_COSMETIC, cosmeticTag);
                    MuseLogger.logDebug("Resetting tag to default " + tagName);
                } else {
                    NBTTagCompound cosmeticTag = itemTag.getCompoundTag(NuminaNBTConstants.TAG_COSMETIC);
                    NBTTagCompound renderTag;

                    // tag has not been moved yet.
                    if (cosmeticTag == null)
                        renderTag = itemTag.getCompoundTag(NuminaNBTConstants.TAG_RENDER);
                    else
                        renderTag = cosmeticTag.getCompoundTag(NuminaNBTConstants.TAG_RENDER);
                    if (renderTag == null) {
                        itemTag.removeTag(NuminaNBTConstants.TAG_RENDER);
                        itemTag.removeTag(NuminaNBTConstants.TAG_COSMETIC);

                        cosmeticTag = new NBTTagCompound();
                        renderTag = new NBTTagCompound();

                        cosmeticTag.setTag(NuminaNBTConstants.TAG_RENDER, renderTag);
                        itemTag.setTag(NuminaNBTConstants.TAG_COSMETIC, cosmeticTag);
                    }

                    if (tagData == null || tagData.isEmpty()) {
                        MuseLogger.logDebug("Removing tag " + tagName);
                        renderTag.removeTag(tagName);
                    } else {
                        MuseLogger.logDebug("Adding tag " + tagName + " : " + tagData);
                        renderTag.setTag(tagName, tagData);
                    }
                }
            }
            return null;
        }
    }
}
