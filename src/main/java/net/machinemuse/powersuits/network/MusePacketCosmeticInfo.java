package net.machinemuse.powersuits.network;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.numina.network.MusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.io.DataInputStream;

import static net.machinemuse.powersuits.common.MPSConstants.NBT_RENDER_TAG;
import static net.machinemuse.powersuits.common.MPSConstants.NBT_SPECLIST_TAG;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:16 AM, 01/05/13
 *
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

    @Override
    public MusePackager packager() {
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
        if (tagName != null && stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            NBTTagCompound renderTag;
            if (!itemTag.hasKey(NBT_RENDER_TAG)) {
                renderTag = new NBTTagCompound();
                itemTag.setTag(NBT_RENDER_TAG, renderTag);
            } else {
                renderTag = itemTag.getCompoundTag(NBT_RENDER_TAG);
            }
            if (tagData.hasNoTags()) {
                MuseLogger.logDebug("Removing tag " + tagName);
                renderTag.removeTag(tagName);
            } else {
                if (tagName.equals(NBT_SPECLIST_TAG)) {
                    NBTTagList tagList = tagData.getTagList(tagName, Constants.NBT.TAG_COMPOUND);
                    MuseLogger.logDebug("Adding tagList " + tagName + " : " + tagList);
                    renderTag.setTag(tagName, tagList);
                } else {
                    MuseLogger.logDebug("Adding tag " + tagName + " : " + tagData.toString());
                    renderTag.setTag(tagName, tagData);
                }
            }
        }
    }

    private static MusePacketCosmeticInfoPackager PACKAGERINSTANCE;
    public static MusePacketCosmeticInfoPackager getPackagerInstance() {
        if (PACKAGERINSTANCE == null) {
            synchronized (MusePacketCosmeticInfoPackager.class) {
                if (PACKAGERINSTANCE == null) {
                    PACKAGERINSTANCE = new MusePacketCosmeticInfoPackager();
                }
            }
        }
        return PACKAGERINSTANCE;
    }

    public static class MusePacketCosmeticInfoPackager extends MusePackager {
        @Override
        public MusePacket read(DataInputStream datain, EntityPlayer player) {
            int itemSlot = readInt(datain);
            String tagName = readString(datain);
            NBTTagCompound tagData = readNBTTagCompound(datain);
            return new MusePacketCosmeticInfo(player, itemSlot, tagName, tagData);
        }
    }
}
