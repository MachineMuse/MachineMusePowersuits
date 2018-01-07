package net.machinemuse.powersuits.common.config;


import net.machinemuse.numina.network.MusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.minecraft.network.PacketBuffer;

import java.io.DataInputStream;

/**
 * A bunch of server side configurable settings.
 */
public final class ServerSettings {
    /** Cosmetic --------------------------------------------------------- */
    final boolean allowHighPollyArmorModels;
    final boolean allowCustomHighPollyArmorModels;
    final boolean allowCustomPowerFistModels;

    /**
     * Server side instance.
     */
    public ServerSettings(){
        allowHighPollyArmorModels = MPSSettings.modelconfig.allowHighPollyArmorModels;
        allowCustomHighPollyArmorModels = MPSSettings.modelconfig.allowCustomHighPollyArmor;
        allowCustomPowerFistModels = MPSSettings.modelconfig.allowCustomPowerFistModels;
    }

    /**
     * Sets all settings from a packet received client side in a new instance held in MPSSettings.
     */
    public ServerSettings(final DataInputStream datain) {
        allowHighPollyArmorModels = MusePackager.getInstance().readBoolean(datain);
        allowCustomHighPollyArmorModels = MusePackager.getInstance().readBoolean(datain);
        allowCustomPowerFistModels = MusePackager.getInstance().readBoolean(datain);
    }

    /**
     * This is a server side operation that gets the values and writes them to the packet.
     * This packet is then sent to a new client on login to sync config values. This allows
     * the server to be able to control these settings.
     */
    public void writeToBuffer(final MusePacket packet) {
        packet.writeBoolean(allowHighPollyArmorModels);
        packet.writeBoolean(allowCustomHighPollyArmorModels);
        packet.writeBoolean(allowCustomPowerFistModels);
    }
}
