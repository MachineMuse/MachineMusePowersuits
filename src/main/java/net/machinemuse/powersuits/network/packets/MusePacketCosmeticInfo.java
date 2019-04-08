package net.machinemuse.powersuits.network.packets;

import net.machinemuse.numina.basemod.MuseLogger;
import net.machinemuse.numina.capabilities.inventory.IModeChangingItem;
import net.machinemuse.numina.capabilities.inventory.IModularItem;
import net.machinemuse.numina.constants.ModelSpecTags;
import net.machinemuse.numina.nbt.MuseNBTUtils;
import net.machinemuse.numina.network.MuseByteBufferUtils;
import net.machinemuse.powersuits.basemod.ModuleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 * <p>
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketCosmeticInfo {
    protected static int playerID;
    protected static int itemSlot;
    protected String tagName;
    protected NBTTagCompound tagData;

    public MusePacketCosmeticInfo() {
    }

    public MusePacketCosmeticInfo(int playerID, int itemSlot, String tagName, NBTTagCompound tagData) {
        this.playerID = playerID;
        this.itemSlot = itemSlot;
        this.tagName = tagName;
        this.tagData = tagData;
    }

    public static void encode(MusePacketCosmeticInfo msg, PacketBuffer packetBuffer) {
        packetBuffer.writeInt(msg.playerID);
        packetBuffer.writeInt(msg.itemSlot);
        packetBuffer.writeString(msg.tagName);
        MuseByteBufferUtils.writeCompressedNBT(packetBuffer, msg.tagData);
    }

    public static MusePacketCosmeticInfo decode(PacketBuffer packetBuffer) {
        return new MusePacketCosmeticInfo(
                packetBuffer.readInt(),
                packetBuffer.readInt(),
                packetBuffer.readString(500),
                MuseByteBufferUtils.readCompressedNBT(packetBuffer));
    }

    public static void handle(MusePacketCosmeticInfo message, Supplier<NetworkEvent.Context> ctx) {
        final EntityPlayerMP player = ctx.get().getSender();

        if (player == null || player.getServer() == null)
            return;

        player.getServerWorld().addScheduledTask(() -> {
            final EntityPlayer actualPlayer;
            int playerID = message.playerID;
            int itemSlot = message.itemSlot;
            String tagName = message.tagName;
            NBTTagCompound tagData = message.tagData;
            Entity entity = player.world.getEntityByID(playerID);
            if (!(player.world.getEntityByID(playerID) instanceof EntityPlayer ))
                return;
            else
                actualPlayer = (EntityPlayer) player.world.getEntityByID(playerID);

            if (actualPlayer == null)
                return;

            ItemStack itemStack = actualPlayer.inventory.getStackInSlot(itemSlot);

            LazyOptional<IModularItem> modularItemCap = ModuleManager.INSTANCE.getModularItemCapability(itemStack);
            LazyOptional<IModeChangingItem> modeChangingItemCap = ModuleManager.INSTANCE.getModeChangingModularItemCapability(itemStack);

            if (tagName != null && (modularItemCap.isPresent()|| modeChangingItemCap.isPresent())) {
                NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(itemStack);
                itemTag.remove(ModelSpecTags.TAG_COSMETIC_PRESET);

                if (Objects.equals(tagName, ModelSpecTags.TAG_RENDER)) {
                    itemTag.remove(ModelSpecTags.TAG_RENDER);
                    if (!tagData.isEmpty())
                        itemTag.put(ModelSpecTags.TAG_RENDER, tagData);
                } else {
                    NBTTagCompound renderTag;
                    if (!itemTag.contains(ModelSpecTags.TAG_RENDER)) {
                        renderTag = new NBTTagCompound();
                        itemTag.put(ModelSpecTags.TAG_RENDER, renderTag);
                    } else {
                        renderTag = itemTag.getCompound(ModelSpecTags.TAG_RENDER);
                    }
                    if (tagData.isEmpty()) {
                        MuseLogger.logger.debug("Removing tag " + tagName);
                        renderTag.remove(tagName);
                    } else {
                        MuseLogger.logger.debug("Adding tag " + tagName + " : " + tagData);
                        renderTag.put(tagName, tagData);
                    }
                }
            }
        });
    }
}