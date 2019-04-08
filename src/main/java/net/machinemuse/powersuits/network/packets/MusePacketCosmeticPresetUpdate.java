package net.machinemuse.powersuits.network.packets;

import net.machinemuse.numina.basemod.MuseLogger;
import net.machinemuse.numina.basemod.Numina;
import net.machinemuse.numina.network.MuseByteBufferUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MusePacketCosmeticPresetUpdate {
//    protected static int playerID;
    String registryName;
    String name;
    NBTTagCompound cosmeticSettings;

    public MusePacketCosmeticPresetUpdate() {

    }

    public MusePacketCosmeticPresetUpdate(ResourceLocation registryNameIn, String nameIn, NBTTagCompound cosmeticSettingsIn) {
//        this.playerID = playerID; // either sender or destination
        this(registryNameIn.toString(), nameIn, cosmeticSettingsIn);
    }

    public MusePacketCosmeticPresetUpdate(String registryNameIn, String nameIn, NBTTagCompound cosmeticSettingsIn) {
//        this.playerID = playerID; // either sender or destination
        this.registryName = registryNameIn;
        this.name = nameIn;
        this.cosmeticSettings = cosmeticSettingsIn;
    }

    public static void encode(MusePacketCosmeticPresetUpdate msg, PacketBuffer packetBuffer) {
//        packetBuffer.writeInt(msg.playerID);
        packetBuffer.writeString(msg.registryName);
        packetBuffer.writeString(msg.name);
        MuseByteBufferUtils.writeCompressedNBT(packetBuffer, msg.cosmeticSettings);
    }

    public static MusePacketCosmeticPresetUpdate decode(PacketBuffer packetBuffer) {
        return new MusePacketCosmeticPresetUpdate(
//                packetBuffer.readInt(),
                packetBuffer.readString(500),
        packetBuffer.readString(500),
        MuseByteBufferUtils.readCompressedNBT(packetBuffer));
    }

    public static void handle(MusePacketCosmeticPresetUpdate message, Supplier<NetworkEvent.Context> ctx) {
        MuseLogger.logger.error("this has not been implemented yet");

//        if (ctx.side == Side.SERVER) {
//            boolean allowCosmeticPresetCreation;
//            final EntityPlayerMP player = ctx.getServerHandler().player;
//            // check if player is the server owner
//            if (FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer()) {
//                allowCosmeticPresetCreation = player.getName().equals(FMLCommonHandler.instance().getMinecraftServerInstance().getServerOwner());
//            } else {
//                // check if player is top level op
//                UserListOpsEntry opEntry = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
//                int opLevel = opEntry != null ? opEntry.getPermissionLevel() : 0;
//                allowCosmeticPresetCreation = opLevel == 4;
//            }
//            if(allowCosmeticPresetCreation) {
//                player.getServerWorld().addScheduledTask(() -> {
//                    ResourceLocation registryName = message.registryName;
//                    String name = message.name;
//                    NBTTagCompound cosmeticSettings = message.cosmeticSettings;
//                    MPSServerSettings settings = MPSConfig.INSTANCE.getServerSettings();
//                    if (settings != null) {
//                        settings.updateCosmeticInfo(registryName, name, cosmeticSettings);
//                        MPSPackets.sendToAll(new MusePacketCosmeticPresetUpdate(registryName, name, cosmeticSettings));
//                    } else {
//                        MPSSettings.cosmetics.updateCosmeticInfo(registryName, name, cosmeticSettings);
//                    }
//                    if (CosmeticPresetSaveLoad.savePreset(registryName, name, cosmeticSettings))
//                        player.sendMessage(new TextComponentTranslation("gui.powersuits.savesuccessful"));
////                        else
////                            player.sendMessage(new TextComponentTranslation("gui.powersuits.fail"));
//                });
//            }
//        } else {
//            Minecraft.getInstance().addScheduledTask(() -> {
//                ResourceLocation registryName = message.registryName;
//                String name = message.name;
//                NBTTagCompound cosmeticSettings = message.cosmeticSettings;
//                MPSServerSettings settings = MPSConfig.INSTANCE.getServerSettings();
//                settings.updateCosmeticInfo(registryName, name, cosmeticSettings);
//            });
//        }
//        return null;
    }
}