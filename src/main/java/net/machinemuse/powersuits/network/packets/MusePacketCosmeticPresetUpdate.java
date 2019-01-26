package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBuf;
import net.machinemuse.numina.network.MuseByteBufferUtils;
import net.machinemuse.powersuits.common.config.CosmeticPresetSaveLoad;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.common.config.MPSServerSettings;
import net.machinemuse.powersuits.common.config.MPSSettings;
import net.machinemuse.powersuits.network.MPSPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MusePacketCosmeticPresetUpdate implements IMessage {
    ResourceLocation registryName;
    String name;
    NBTTagCompound cosmeticSettings;

    public MusePacketCosmeticPresetUpdate() {

    }

    public MusePacketCosmeticPresetUpdate(ResourceLocation registryNameIn, String nameIn, NBTTagCompound cosmeticSettingsIn) {
        this.registryName = registryNameIn;
        this.name = nameIn;
        this.cosmeticSettings = cosmeticSettingsIn;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.registryName = new ResourceLocation(MuseByteBufferUtils.readUTF8String(buf));
        this.name = MuseByteBufferUtils.readUTF8String(buf);
        this.cosmeticSettings = MuseByteBufferUtils.readCompressedNBT(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        MuseByteBufferUtils.writeUTF8String(buf, registryName.toString());
        MuseByteBufferUtils.writeUTF8String(buf, name);
        MuseByteBufferUtils.writeCompressedNBT(buf, cosmeticSettings);
    }

    public static class Handler implements IMessageHandler<MusePacketCosmeticPresetUpdate, IMessage> {
        @Override
        public IMessage onMessage(MusePacketCosmeticPresetUpdate message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                boolean allowCosmeticPresetCreation;
                final EntityPlayerMP player = ctx.getServerHandler().player;
                // check if player is the server owner
                if (FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer()) {
                    allowCosmeticPresetCreation = player.getName().equals(FMLCommonHandler.instance().getMinecraftServerInstance().getServerOwner());
                } else {
                    // check if player is top level op
                    UserListOpsEntry opEntry = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
                    int opLevel = opEntry != null ? opEntry.getPermissionLevel() : 0;
                    allowCosmeticPresetCreation = opLevel == 4;
                }
                if(allowCosmeticPresetCreation) {
                    player.getServerWorld().addScheduledTask(() -> {
                        ResourceLocation registryName = message.registryName;
                        String name = message.name;
                        NBTTagCompound cosmeticSettings = message.cosmeticSettings;
                        MPSServerSettings settings = MPSConfig.INSTANCE.getServerSettings();
                        if (settings != null) {
                            settings.updateCosmeticInfo(registryName, name, cosmeticSettings);
                            MPSPackets.sendToAll(new MusePacketCosmeticPresetUpdate(registryName, name, cosmeticSettings));
                        } else {
                            MPSSettings.cosmetics.updateCosmeticInfo(registryName, name, cosmeticSettings);
                        }
                        if (CosmeticPresetSaveLoad.savePreset(registryName, name, cosmeticSettings))
                            player.sendMessage(new TextComponentTranslation("gui.powersuits.savesuccessful"));
//                        else
//                            player.sendMessage(new TextComponentTranslation("gui.powersuits.fail"));
                    });
                }
            } else {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    ResourceLocation registryName = message.registryName;
                    String name = message.name;
                    NBTTagCompound cosmeticSettings = message.cosmeticSettings;
                    MPSServerSettings settings = MPSConfig.INSTANCE.getServerSettings();
                    settings.updateCosmeticInfo(registryName, name, cosmeticSettings);
                });
            }
            return null;
        }
    }
}