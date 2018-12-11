package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBufInputStream;
import net.machinemuse.numina.common.constants.NuminaNBTConstants;
import net.machinemuse.numina.item.IModularItem;
import net.machinemuse.numina.network.IMusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:16 AM, 01/05/13
 * <p>
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketCosmeticInfo extends MusePacket {
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

    public static MusePacketCosmeticInfoPackager getPackagerInstance() {
        return MusePacketCosmeticInfoPackager.INSTANCE;
    }

    @Override
    public IMusePackager packager() {
        return getPackagerInstance();
    }

    @Override
    public void write() {
        writeInt(itemSlot);
        writeString(tagName);
        writeNBTTagCompound(tagData);
    }

    @Override
    public void handleServer(EntityPlayerMP player) {
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
    }

    public enum MusePacketCosmeticInfoPackager implements IMusePackager {
        INSTANCE;

        @Override
        public MusePacket read(ByteBufInputStream datain, EntityPlayer player) {
            int itemSlot = readInt(datain);
            String tagName = readString(datain);
            NBTTagCompound tagData = readNBTTagCompound(datain);
            return new MusePacketCosmeticInfo(player, itemSlot, tagName, tagData);
        }
    }
}