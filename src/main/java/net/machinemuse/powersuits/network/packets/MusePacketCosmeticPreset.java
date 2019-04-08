package net.machinemuse.powersuits.network.packets;

import net.machinemuse.numina.constants.ModelSpecTags;
import net.machinemuse.numina.nbt.MuseNBTUtils;
import net.machinemuse.powersuits.basemod.ModuleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MusePacketCosmeticPreset {
    protected int playerID;
    protected int itemSlot;
    protected String presetName;

    public MusePacketCosmeticPreset() {
    }

    public MusePacketCosmeticPreset(int playerID, int itemSlot, String presetName) {
        this.playerID = playerID;
        this.itemSlot = itemSlot;
        this.presetName = presetName;
    }

    public static void encode(MusePacketCosmeticPreset msg, PacketBuffer packetBuffer) {
        packetBuffer.writeInt(msg.playerID);
        packetBuffer.writeInt(msg.itemSlot);
        packetBuffer.writeString(msg.presetName);
    }

    public static MusePacketCosmeticPreset decode(PacketBuffer packetBuffer) {
        return new MusePacketCosmeticPreset(packetBuffer.readInt(), packetBuffer.readInt(), packetBuffer.readString(500));
    }

    public static void handle(MusePacketCosmeticPreset message, Supplier<NetworkEvent.Context> ctx) {
        final EntityPlayerMP player = ctx.get().getSender();

        if (player == null || player.getServer() == null)
            return;

        final EntityPlayer actualPlayer;
        int playerID = message.playerID;
        int itemSlot = message.itemSlot;
        if (!(player.world.getEntityByID(playerID) instanceof EntityPlayer ))
            return;
        else
            actualPlayer = (EntityPlayer) player.world.getEntityByID(playerID);

        if (actualPlayer == null)
            return;

        player.getServerWorld().addScheduledTask(() -> {

            String presetName = message.presetName;
            ItemStack stack = actualPlayer.inventory.getStackInSlot(itemSlot);

            if (ModuleManager.INSTANCE.isIModularItem(stack)) {
                NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
                itemTag.remove(ModelSpecTags.TAG_RENDER);
                itemTag.putString(ModelSpecTags.TAG_COSMETIC_PRESET, presetName);
            }
        });
    }
}