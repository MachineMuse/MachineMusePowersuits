package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBuf;
import net.machinemuse.numina.basemod.MuseLogger;
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

    public MusePacketCosmeticInfo(){
    }

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
            if (ctx.side == Side.SERVER) {
                final EntityPlayerMP player = ctx.getServerHandler().player;
                player.getServerWorld().addScheduledTask(() -> {
                    int itemSlot = message.itemSlot;
                    String tagName = message.tagName;
                    NBTTagCompound tagData = message.tagData;
                    ItemStack stack = player.inventory.getStackInSlot(itemSlot);

                    if (tagName != null && stack.getItem() instanceof IModularItem) {
                        NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
                        itemTag.removeTag(ModelSpecTags.TAG_COSMETIC_PRESET);

                        if (Objects.equals(tagName, ModelSpecTags.TAG_RENDER)) {
                            itemTag.removeTag(ModelSpecTags.TAG_RENDER);
                            if (!tagData.isEmpty())
                                itemTag.setTag(ModelSpecTags.TAG_RENDER, tagData);
                        } else {
                            NBTTagCompound renderTag;
                            if (!itemTag.hasKey(ModelSpecTags.TAG_RENDER)) {
                                renderTag = new NBTTagCompound();
                                itemTag.setTag(ModelSpecTags.TAG_RENDER, renderTag);
                            } else {
                                renderTag = itemTag.getCompoundTag(ModelSpecTags.TAG_RENDER);
                            }
                            if (tagData.isEmpty()) {
                                MuseLogger.logDebug("Removing tag " + tagName);
                                renderTag.removeTag(tagName);
                            } else {
                                MuseLogger.logDebug("Adding tag " + tagName + " : " + tagData);
                                renderTag.setTag(tagName, tagData);
                            }
                        }
                    }
                });
            } else {

            }
            return null;
        }
    }
}