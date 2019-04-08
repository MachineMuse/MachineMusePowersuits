package net.machinemuse.powersuits.network.packets;

import net.machinemuse.numina.constants.ModelSpecTags;
import net.machinemuse.numina.nbt.MuseNBTUtils;
import net.machinemuse.powersuits.basemod.ModuleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 * <p>
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketColourInfo {
    protected int playerID;
    protected int itemSlot;
    protected int[] tagData;

    public MusePacketColourInfo() {

    }

    public MusePacketColourInfo(int playerID, int itemSlot, int[] tagData) {
        this.playerID = playerID;
        this.itemSlot = itemSlot;
        this.tagData = tagData;
    }

    public static void encode(MusePacketColourInfo msg, PacketBuffer packetBuffer) {
        packetBuffer.writeInt(msg.playerID);
        packetBuffer.writeInt(msg.itemSlot);
        packetBuffer.writeVarIntArray(msg.tagData);
    }

    public static MusePacketColourInfo decode(PacketBuffer packetBuffer) {
        return new MusePacketColourInfo(packetBuffer.readInt(), packetBuffer.readInt(), packetBuffer.readVarIntArray());
    }

    public static void handle(MusePacketColourInfo message, Supplier<NetworkEvent.Context> ctx) {
        final EntityPlayerMP player = ctx.get().getSender();

        if (player == null || player.getServer() == null)
            return;

        final EntityPlayer actualPlayer;
        int playerID = message.playerID;
        int itemSlot = message.itemSlot;
        int[] tagData = message.tagData;
        Entity entity = player.world.getEntityByID(playerID);
        if (!(player.world.getEntityByID(playerID) instanceof EntityPlayer ))
            return;
        else
            actualPlayer = (EntityPlayer) player.world.getEntityByID(playerID);

        if (actualPlayer == null)
            return;

        player.getServerWorld().addScheduledTask(() -> {
            ItemStack stack = actualPlayer.inventory.getStackInSlot(itemSlot);
            if (ModuleManager.INSTANCE.isIModularItem(stack)) {
                NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
                NBTTagCompound renderTag = itemTag.getCompound(ModelSpecTags.TAG_RENDER);
                if (renderTag == null) {
                    renderTag = new NBTTagCompound();
                    itemTag.put(ModelSpecTags.TAG_RENDER, renderTag);
                }
                if (renderTag != null)
                    renderTag.putIntArray(ModelSpecTags.TAG_COLOURS, tagData);
            }
        });
    }
}