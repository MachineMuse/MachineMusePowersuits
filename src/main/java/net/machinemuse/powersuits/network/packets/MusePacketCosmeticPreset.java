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

public class MusePacketCosmeticPreset implements IMessage {
    private EntityPlayer player;
    int itemSlot;
    String presetName;

    public MusePacketCosmeticPreset() {
    }

    public MusePacketCosmeticPreset (EntityPlayer player, int itemSlot, String presetName) {
        this.player = player;
        this.itemSlot = itemSlot;
        this.presetName = presetName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        itemSlot = buf.readInt();
        presetName = MuseByteBufferUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(itemSlot);
        MuseByteBufferUtils.writeUTF8String(buf, presetName);
    }

    public static class Handler implements IMessageHandler<MusePacketCosmeticPreset, IMessage> {
        @Override
        public IMessage onMessage(MusePacketCosmeticPreset message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                final EntityPlayerMP player = ctx.getServerHandler().player;
                player.getServerWorld().addScheduledTask(() -> {

                    int itemSlot = message.itemSlot;
                    String presetName = message.presetName;
                    ItemStack stack = player.inventory.getStackInSlot(itemSlot);

                    if (presetName != null && stack.getItem() instanceof IModularItem) {
                        NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
                        itemTag.removeTag(ModelSpecTags.TAG_RENDER);
                        itemTag.setString(ModelSpecTags.TAG_COSMETIC_PRESET, presetName);
                    }
                });
            }
            return null;
        }
    }
}