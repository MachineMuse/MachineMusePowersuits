package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBufInputStream;
import net.machinemuse.numina.network.IMusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.common.config.MPSServerSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * Provides a way for server ops to create cosmetic presets and send to the server where they will be saved.
 */
public class MusePacketCosmeticPresetUpdate extends MusePacket {
    EntityPlayer player;
    ResourceLocation registryName;
    String name;
    NBTTagCompound cosmeticSettings;

    public MusePacketCosmeticPresetUpdate(EntityPlayer playerIn, ResourceLocation registryNameIn, String nameIn, NBTTagCompound cosmeticSettingsIn) {
        player = playerIn;
        registryName = registryNameIn;
        name = nameIn;
        cosmeticSettings = cosmeticSettingsIn;
        System.out.println("creating new packet");
    }

    public static MusePacketCosmeticPresetUpdateRequestPackager getPackagerInstance() {
        return MusePacketCosmeticPresetUpdateRequestPackager.INSTANCE;
    }

    @Override
    public IMusePackager packager() {
        return getPackagerInstance();
    }

    @Override
    public void write() {
        this.writeString(registryName.toString());
        this.writeString(name);
        this.writeNBTTagCompound(cosmeticSettings);

        System.out.println("writing packet");
    }

    @Override
    public void handleClient(EntityPlayer player) {
        super.handleClient(player);
        System.out.println("handling client");

        // fixme... save to file on client side?
    }

    @Override
    public void handleServer(EntityPlayerMP player) {
        super.handleServer(player);

        System.out.println("handling server");

        // fixme... save to file
    }

    public enum MusePacketCosmeticPresetUpdateRequestPackager implements IMusePackager {
        INSTANCE;


        @Override
        public MusePacket read(ByteBufInputStream datain, EntityPlayer player) {
            System.out.println("reading packet");


            ResourceLocation location = new ResourceLocation(readString(datain));
            String name = readString(datain);
            NBTTagCompound cosmeticSettings = readNBTTagCompound(datain);

            System.out.println("registry name: " + location.toString());
            System.out.println("name: " + name);
            System.out.println("cosmeticSettings: " + cosmeticSettings.toString());



            MPSServerSettings settings = MPSConfig.INSTANCE.getServerSettings();
            if (settings != null) {
                settings.updateCosmeticInfo(location, name, cosmeticSettings);
            } else {
                // FIXME: update local config map instead

                System.out.println("update local config map instead");
            }
            return new MusePacketCosmeticPresetUpdate(player, location, name, cosmeticSettings);
        }
    }
}
