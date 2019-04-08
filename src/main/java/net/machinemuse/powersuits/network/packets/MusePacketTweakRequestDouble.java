package net.machinemuse.powersuits.network.packets;

import net.machinemuse.numina.basemod.MuseLogger;
import net.machinemuse.numina.basemod.Numina;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MusePacketTweakRequestDouble {
    protected int playerID;
    protected int itemSlot;
    protected String moduleName;
    protected String tweakName;
    protected double tweakValue;

    public MusePacketTweakRequestDouble() {

    }

    public MusePacketTweakRequestDouble(int playerID, int itemSlot, String moduleName, String tweakName, double tweakValue) {
        this.playerID = playerID;
        this.itemSlot = itemSlot;
        this.moduleName = moduleName;
        this.tweakName = tweakName;
        this.tweakValue = tweakValue;
    }

    public static void encode(MusePacketTweakRequestDouble msg, PacketBuffer packetBuffer) {
        packetBuffer.writeInt(msg.playerID);
        packetBuffer.writeInt(msg.itemSlot);
        packetBuffer.writeString(msg.moduleName);
        packetBuffer.writeString(msg.tweakName);
        packetBuffer.writeDouble(msg.tweakValue);
    }

    public static MusePacketTweakRequestDouble decode(PacketBuffer packetBuffer) {
        return new MusePacketTweakRequestDouble(
                packetBuffer.readInt(),
                packetBuffer.readInt(),
                packetBuffer.readString(500),
                packetBuffer.readString(500),
                packetBuffer.readDouble());
    }

    public static void handle(MusePacketTweakRequestDouble message, Supplier<NetworkEvent.Context> ctx) {
        final EntityPlayerMP player = ctx.get().getSender();

        if (player == null || player.getServer() == null)
            return;

        final EntityPlayer actualPlayer;
        int playerID = message.playerID;

        Entity entity = player.world.getEntityByID(playerID);
        if (!(player.world.getEntityByID(playerID) instanceof EntityPlayer ))
            return;
        else
            actualPlayer = (EntityPlayer) player.world.getEntityByID(playerID);

        if (actualPlayer == null)
            return;

        player.getServerWorld().addScheduledTask(() -> {
            int itemSlot = message.itemSlot;
            String moduleName = message.moduleName;
            String tweakName = message.tweakName;
            double tweakValue = message.tweakValue;

            if (moduleName != null && tweakName != null) {
                ItemStack stack = player.inventory.getStackInSlot(itemSlot);

                MuseLogger.logger.error("this has not been implemented yet");
//                NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
//
//                if (itemTag != null && ModuleManager.INSTANCE.tagHasModule(itemTag, moduleName)) {
//                    MuseNBTUtils.removeMuseValuesTag(stack);
//                    NBTTagCompound moduleTag = itemTag.getCompoundTag(moduleName);
//                    moduleTag.setDouble(tweakName, tweakValue);
//                }
            }
        });
    }
}