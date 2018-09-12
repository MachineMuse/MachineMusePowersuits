package net.machinemuse.powersuits.common.config;

import io.netty.buffer.ByteBufInputStream;
import net.machinemuse.numina.network.MusePackager;
import net.machinemuse.numina.network.MusePacket;

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
    public final double baseMaxHeatPowerFist;
    public final double baseMaxHeatHelmet;
    public final double baseMaxHeatChest;
    public final double baseMaxHeatLegs;
    public final double baseMaxHeatFeet;


    /** Modules ----------------------------------------------------------------------------------- */
    public final Map<String, Boolean> allowedModules;
    public final Map<String, Double> propertyDouble;
    public final Map<String, Integer> propertyInteger;

    /** Energy ------------------------------------------------------------------------------------ */
    public final double mekRatio;
    public final double ic2Ratio;
    public final double refinedStorageRatio;
    public final double ae2Ratio;
    public final int maxTier1;
    public final int maxTier2;
    public final int maxTier3;
    public final int maxTier4;

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
        baseMaxHeatPowerFist = MPSSettings.general.baseMaxHeatPowerFist;
        baseMaxHeatHelmet = MPSSettings.general.baseMaxHeatHelmet;
        baseMaxHeatChest = MPSSettings.general.baseMaxHeatChest;
        baseMaxHeatLegs = MPSSettings.general.baseMaxHeatLegs;
        baseMaxHeatFeet = MPSSettings.general.baseMaxHeatFeet;

        /** Modules ------------------------------------------------------------------------------- */
        allowedModules = new TreeMap<>(MPSSettings.modules.allowedModules);
        propertyDouble = new TreeMap<>(MPSSettings.modules.propertyDouble);
        propertyInteger = new TreeMap<>(MPSSettings.modules.propertyInteger);       


        /** Energy -------------------------------------------------------------------------------- */
        mekRatio = MPSSettings.energy.mekRatio;
        ic2Ratio = MPSSettings.energy.ic2Ratio;
        refinedStorageRatio = MPSSettings.energy.refinedStorageRatio;
        ae2Ratio = MPSSettings.energy.ae2Ratio;
        maxTier1 = MPSSettings.energy.maxTier1;
        maxTier2 = MPSSettings.energy.maxTier2;
        maxTier3 = MPSSettings.energy.maxTier3;
        maxTier4 = MPSSettings.energy.maxTier4;
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
        baseMaxHeatPowerFist = MusePackager.INSTANCE.readDouble(datain);
        baseMaxHeatHelmet = MusePackager.INSTANCE.readDouble(datain);
        baseMaxHeatChest = MusePackager.INSTANCE.readDouble(datain);
        baseMaxHeatLegs = MusePackager.INSTANCE.readDouble(datain);
        baseMaxHeatFeet = MusePackager.INSTANCE.readDouble(datain);


        /** Modules ------------------------------------------------------------------------------- */
//        allowedModules = MusePackager.INSTANCE.readMap(datain, String.class, Boolean.class);
//        propertyDouble = MusePackager.INSTANCE.readMap(datain, String.class, Double.class);

        allowedModules = MusePackager.INSTANCE.readMap(datain, String.class, Boolean.class);
        propertyDouble = MusePackager.INSTANCE.readMap(datain, String.class, Double.class);
        propertyInteger = MusePackager.INSTANCE.readMap(datain, String.class, Integer.class);



        /** Energy -------------------------------------------------------------------------------- */
        mekRatio = MusePackager.INSTANCE.readDouble(datain);
        ic2Ratio = MusePackager.INSTANCE.readDouble(datain);
        refinedStorageRatio = MusePackager.INSTANCE.readDouble(datain);
        ae2Ratio = MusePackager.INSTANCE.readDouble(datain);
        maxTier1 = MusePackager.INSTANCE.readInt(datain);
        maxTier2 = MusePackager.INSTANCE.readInt(datain);
        maxTier3 = MusePackager.INSTANCE.readInt(datain);
        maxTier4 = MusePackager.INSTANCE.readInt(datain);



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
        packet.writeDouble(baseMaxHeatPowerFist);
        packet.writeDouble(baseMaxHeatHelmet);
        packet.writeDouble(baseMaxHeatChest);
        packet.writeDouble(baseMaxHeatLegs);
        packet.writeDouble(baseMaxHeatFeet);


        /** Modules ------------------------------------------------------------------------------- */
        packet.writeMap(allowedModules, true);
        packet.writeMap(propertyDouble, true);
        packet.writeMap(propertyInteger, true);
        

        /** Energy -------------------------------------------------------------------------------- */
        packet.writeDouble(mekRatio);
        packet.writeDouble(ic2Ratio);
        packet.writeDouble(refinedStorageRatio);
        packet.writeDouble(ae2Ratio);
        packet.writeInt(maxTier1);
        packet.writeInt(maxTier2);
        packet.writeInt(maxTier3);
        packet.writeInt(maxTier4);
    }
}