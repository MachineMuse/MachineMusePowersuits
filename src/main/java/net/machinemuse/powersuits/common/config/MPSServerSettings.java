package net.machinemuse.powersuits.common.config;

import io.netty.buffer.ByteBufInputStream;
import net.machinemuse.numina.network.MusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Caution: make sure the order of the packets matches between Read and Write.
 */
public class MPSServerSettings {
    /** General ----------------------------------------------------------------------------------- */
    public final boolean useOldAutoFeeder;
    public final double maximumFlyingSpeedmps;
    public final double getWeightCapacity;
    public final double maximumArmorPerPiece;
    public final double getSalvageChance;
    public final double baseMaxHeat;




    /** Modules ----------------------------------------------------------------------------------- */
    public final Map<String, Boolean> allowedModules;
    public final Map<String, Double> propertyDouble;


    /** Energy ------------------------------------------------------------------------------------ */
    public final double mekRatio;
    public final double ic2Ratio;
    public final double rfRatio;
    public final double refinedStorageRatio;
    public final double ae2Ratio;

    /**
     * Server side instance.
     */
    public MPSServerSettings() {
        /** General ------------------------------------------------------------------------------- */
        useOldAutoFeeder = MPSSettings.general.useOldAutoFeeder;
        maximumFlyingSpeedmps = MPSSettings.general.getMaximumFlyingSpeedmps;
        getWeightCapacity = MPSSettings.general.getWeightCapacity;
        maximumArmorPerPiece = MPSSettings.general.getMaximumArmorPerPiece;
        getSalvageChance = MPSSettings.general.getSalvageChance;
        baseMaxHeat = MPSSettings.general.baseMaxHeat;


        /** Modules ------------------------------------------------------------------------------- */
        allowedModules = new TreeMap<>(MPSSettings.modules.allowedModules);
        propertyDouble = new TreeMap<>(MPSSettings.modules.propertyDouble);


        /** Energy -------------------------------------------------------------------------------- */
        mekRatio = MPSSettings.energy.mekRatio;
        ic2Ratio = MPSSettings.energy.ic2Ratio;
        rfRatio = MPSSettings.energy.rfRatio;
        refinedStorageRatio = MPSSettings.energy.refinedStorageRatio;
        ae2Ratio = MPSSettings.energy.ae2Ratio;
    }

    /**
     * Sets all settings from a packet received client side in a new instance held in MPSSettings.
     */
    public MPSServerSettings(final ByteBufInputStream datain) {
        /** General ------------------------------------------------------------------------------- */
        useOldAutoFeeder = MusePackager.INSTANCE.readBoolean(datain);
        maximumFlyingSpeedmps = MusePackager.INSTANCE.readDouble(datain);
        getWeightCapacity = MusePackager.INSTANCE.readDouble(datain);
        maximumArmorPerPiece = MusePackager.INSTANCE.readDouble(datain);
        getSalvageChance = MusePackager.INSTANCE.readDouble(datain);
        baseMaxHeat = MusePackager.INSTANCE.readDouble(datain);


        /** Modules ------------------------------------------------------------------------------- */
//        allowedModules = MusePackager.INSTANCE.readMap(datain, String.class, Boolean.class);
//        propertyDouble = MusePackager.INSTANCE.readMap(datain, String.class, Double.class);

        allowedModules = MusePackager.INSTANCE.readMap(datain, String.class, Boolean.class);
        propertyDouble = MusePackager.INSTANCE.readMap(datain, String.class, Double.class);



        /** Energy -------------------------------------------------------------------------------- */
        mekRatio = MusePackager.INSTANCE.readDouble(datain);
        ic2Ratio = MusePackager.INSTANCE.readDouble(datain);
        rfRatio = MusePackager.INSTANCE.readDouble(datain);
        refinedStorageRatio = MusePackager.INSTANCE.readDouble(datain);
        ae2Ratio = MusePackager.INSTANCE.readDouble(datain);




    }



    /**
     * This is a server side operation that gets the values and writes them to the packet.
     * This packet is then sent to a new client on login to sync config values. This allows
     * the server to be able to control these settings.
     */
    public void writeToBuffer(final MusePacket packet) {
        /** General ------------------------------------------------------------------------------- */
        packet.writeBoolean(useOldAutoFeeder);
        packet.writeDouble(maximumFlyingSpeedmps);
        packet.writeDouble(getWeightCapacity);
        packet.writeDouble(maximumArmorPerPiece);
        packet.writeDouble(getSalvageChance);
        packet.writeDouble(baseMaxHeat);


        /** Modules ------------------------------------------------------------------------------- */
        packet.writeMap(allowedModules, true);
        packet.writeMap(propertyDouble, true);

        /** Energy -------------------------------------------------------------------------------- */
        packet.writeDouble(mekRatio);
        packet.writeDouble(ic2Ratio);
        packet.writeDouble(rfRatio);
        packet.writeDouble(refinedStorageRatio);
        packet.writeDouble(ae2Ratio);
    }
}