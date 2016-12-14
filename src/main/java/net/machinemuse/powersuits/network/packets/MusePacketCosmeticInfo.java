package net.machinemuse.powersuits.network.packets;

import net.machinemuse.api.IModularItem;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.network.MusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataInputStream;

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
            NBTTagCompound renderTag = null;
            if (!itemTag.hasKey("render")) {
                renderTag = new NBTTagCompound();
                itemTag.setTag("render", renderTag);
            } else {
                renderTag = itemTag.getCompoundTag("render");
            }
            if (tagData.hasNoTags()) {
                MuseLogger.logDebug("Removing tag " + tagName);
                renderTag.removeTag(tagName);
            } else {
                MuseLogger.logDebug("Adding tag " + tagName + " : " + tagData);
                renderTag.setTag(tagName, tagData);
            }
        }
    }

    private static MusePacketCosmeticInfoPackager PACKAGERINSTANCE;
    public static MusePacketCosmeticInfoPackager getPackagerInstance() {
        if (PACKAGERINSTANCE == null)
            PACKAGERINSTANCE = new MusePacketCosmeticInfoPackager();
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