package net.machinemuse.powersuits.network.packets;

import net.machinemuse.api.IModularItem;
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
 * Created: 12:28 PM, 5/6/13
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketColourInfo extends MusePacket {
    EntityPlayer player;
    int itemSlot;
    int[] tagData;

    public MusePacketColourInfo(EntityPlayer player, int itemSlot, int[] tagData) {
        this.player = player;
        this.itemSlot = itemSlot;
        this.tagData = tagData;
    }

    @Override
    public MusePackager packager() {
        return getPackagerInstance();
    }

    @Override
    public void write() {
        writeInt(itemSlot);
        writeIntArray(tagData);
    }

    @Override
    public void handleServer(EntityPlayerMP player) {
        ItemStack stack = player.inventory.getStackInSlot(itemSlot);
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound renderTag = MuseItemUtils.getMuseRenderTag(stack);
            renderTag.setIntArray("colours", tagData);
        }
    }

    private static MusePacketColourInfoPackager PACKAGERINSTANCE;
    public static MusePacketColourInfoPackager getPackagerInstance() {
        if (PACKAGERINSTANCE == null)
            PACKAGERINSTANCE = new MusePacketColourInfoPackager();
        return PACKAGERINSTANCE;
    }

    public static class MusePacketColourInfoPackager extends MusePackager {
        @Override
        public MusePacket read(DataInputStream datain, EntityPlayer player) {
            int itemSlot = readInt(datain);
            int[] tagData = readIntArray(datain);
            return new MusePacketColourInfo(player, itemSlot, tagData);
        }
    }
}