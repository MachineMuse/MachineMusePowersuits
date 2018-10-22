package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBufInputStream;
import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.network.IMusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.powersuits.utils.nbt.MPSNBTUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 * <p>
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

    public static MusePacketColourInfoPackager getPackagerInstance() {
        return MusePacketColourInfoPackager.INSTANCE;
    }

    @Override
    public IMusePackager packager() {
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
        if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
            NBTTagCompound renderTag = MPSNBTUtils.getMuseRenderTag(stack);
            renderTag.setIntArray(NuminaNBTConstants.TAG_COLOURS, tagData);
        }
    }

    public enum MusePacketColourInfoPackager implements IMusePackager {
        INSTANCE;

        @Override
        public MusePacket read(ByteBufInputStream datain, EntityPlayer player) {
            int itemSlot = readInt(datain);
            int[] tagData = readIntArray(datain);
            return new MusePacketColourInfo(player, itemSlot, tagData);
        }
    }
}