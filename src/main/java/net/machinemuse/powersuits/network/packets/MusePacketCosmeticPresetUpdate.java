package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBuf;
import net.machinemuse.numina.network.MuseByteBufferUtils;
import net.machinemuse.powersuits.common.config.CosmeticPresetSaveLoad;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.common.config.MPSServerSettings;
import net.machinemuse.powersuits.network.MPSPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
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
        registryName = registryNameIn;
        name = nameIn;
        cosmeticSettings = cosmeticSettingsIn;
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
                final EntityPlayerMP player = ctx.getServerHandler().player;
                player.getServerWorld().addScheduledTask(() -> {
                    ResourceLocation registryName = message.registryName;
                    String name = message.name;
                    NBTTagCompound cosmeticSettings = message.cosmeticSettings;
                    MPSServerSettings settings = MPSConfig.INSTANCE.getServerSettings();
                    settings.updateCosmeticInfo(registryName, name, cosmeticSettings);
                    CosmeticPresetSaveLoad.savePreset(registryName, name, cosmeticSettings);
                    MPSPackets.sendToAll(new MusePacketCosmeticPresetUpdate(registryName, name, cosmeticSettings));
                });
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