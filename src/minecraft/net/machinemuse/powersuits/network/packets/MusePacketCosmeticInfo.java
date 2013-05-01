package net.machinemuse.powersuits.network.packets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import net.machinemuse.api.IModularItem;
import net.machinemuse.general.MuseLogger;
import net.machinemuse.powersuits.network.MusePacket;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:16 AM, 01/05/13
 */
public class MusePacketCosmeticInfo extends MusePacket {
    protected ItemStack stack;
    protected int itemSlot;
    protected String tagName;
    protected NBTTagCompound tagData;

    /**
     * Constructor for sending this packet.
     *
     * @param player   Player making the request
     * @param itemSlot Slot containing the item for which the cosmetic change is requested
     * @param tagName  name of the tag being updated
     * @param tagData  ref of the tag to be updated
     */
    public MusePacketCosmeticInfo(Player player, int itemSlot, String tagName, NBTTagCompound tagData) {
        super(player);
        try {
            writeInt(itemSlot);
            writeString(tagName);
            writeNBTTagCompound(tagData);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Constructor for receiving this packet.
     *
     * @param player
     * @param data
     * @throws java.io.IOException
     */
    public MusePacketCosmeticInfo(DataInputStream data, Player player) {
        super(data, player);
        try {
            itemSlot = readInt();
            tagName = readString(64);
            tagData = readNBTTagCompound();
            Side side = FMLCommonHandler.instance().getEffectiveSide();
            if (side == Side.SERVER) {
                EntityPlayerMP srvplayer = (EntityPlayerMP) player;
                stack = srvplayer.inventory.getStackInSlot(itemSlot);
            }
        } catch (IOException e) {

        }
    }

    @Override
    public void handleServer(EntityPlayerMP playerEntity) {
        if (tagName != null && stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            NBTTagCompound renderTag;
            if (!itemTag.hasKey("render")) {
                renderTag = new NBTTagCompound();
                itemTag.setCompoundTag("render", renderTag);
            } else {
                renderTag = itemTag.getCompoundTag("render");
            }
            if (tagData.hasNoTags()) {
                MuseLogger.logDebug("Removing tag " + tagName);
                renderTag.removeTag(tagName);
            } else {
                MuseLogger.logDebug("Adding tag " + tagName + " : " + tagData);
                renderTag.setCompoundTag(tagName, tagData);
            }
        }
    }

    @Override
    public void handleClient(EntityClientPlayerMP player) {
        // TODO Auto-generated method stub

    }
}
