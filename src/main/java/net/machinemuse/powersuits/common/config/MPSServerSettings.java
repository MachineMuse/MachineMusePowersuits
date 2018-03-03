package net.machinemuse.powersuits.common.config;


import net.machinemuse.numina.network.MusePackager;
import net.machinemuse.numina.network.MusePacket;

import java.io.DataInputStream;
import java.util.Map;

/**
 * A bunch of server side configurable settings.
 */
public final class MPSServerSettings {
    /** General ----------------------------------------------------------------------------------- */
    public final double maximumArmorPerPiece;
    public final double maximumFlyingSpeedmps;
    public final double baseMaxHeat;
    public final boolean useOldAutoFeeder;
    public final boolean useCheatyLeatherRecipe;
    public final double getWeightCapacity;
    public final double getSalvageChance;
    public final boolean useAdvancedOreScannerMessage;

    /** Inventory --------------------------------------------------------------------------------- */
    public final int maxModuleCountPowerFist;
    public final int maxModuleCountArmorHelmet;
    public final int maxModuleCountArmorChest;
    public final int maxModuleCountArmorLegs;
    public final int maxModuleCountArmorFeet;

    /** Energy ------------------------------------------------------------------------------------ */
    public final int maxEnergyBasicBattery;
    public final int maxEnergyAdvancedBattery;
    public final int maxEnergyEliteBattery;
    public final int maxEnergyUltimateBattery;
    public final double rfEnergyPerIC2EU;

    /** Heat -------------------------------------------------------------------------------------- */
    public final int maxHeatPowerFist;
    public final int maxHeatPowerArmorHelmet;
    public final int maxHeatPowerArmorChestplate;
    public final int maxHeatPowerArmorLeggings;
    public final int maxHeatPowerArmorBoots;

    /** Models ------------------------------------------------------------------------------------ */
    public final boolean modelSetup;
    public final boolean allowHighPollyArmorModels;
    public final boolean allowCustomHighPollyArmorModels;
    public final boolean allowHighPollyPowerFistModels;
    public final boolean allowCustomPowerFistModels;
    public final boolean allowCustomHighPollyPowerFistModels;

    /** Modules ----------------------------------------------------------------------------------- */
    public final Map<String, Boolean> allowedModules;
    public final Map<String, Integer> propertyInteger;
    public final Map<String, Double> propertyDouble;






























    // TODO: maye convert to/from NBT and use that for compression




    /** Cosmetic --------------------------------------------------------- */



    /** Allowed Modules -------------------------------------------------- */
//    final Map<String, Boolean> allowedModules;



    /**
     * Server side instance.
     */
    public MPSServerSettings(){
        /** General ------------------------------------------------------------------------------- */
        maximumArmorPerPiece = MPSSettings.general.getMaximumArmorPerPiece;
        maximumFlyingSpeedmps = MPSSettings.general.getMaximumFlyingSpeedmps;
        baseMaxHeat = MPSSettings.general.baseMaxHeat;
        useOldAutoFeeder = MPSSettings.general.useOldAutoFeeder;
        useCheatyLeatherRecipe = MPSSettings.general.useCheatyLeatherRecipe;
        getWeightCapacity = MPSSettings.general.getWeightCapacity;
        getSalvageChance = MPSSettings.general.getSalvageChance;
        useAdvancedOreScannerMessage = MPSSettings.general.useAdvancedOreScannerMessage;

        /** Inventory ----------------------------------------------------------------------------- */
        maxModuleCountPowerFist = MPSSettings.inventory.maxModuleCountPowerFist;
        maxModuleCountArmorHelmet = MPSSettings.inventory.maxModuleCountArmorHelmet;
        maxModuleCountArmorChest = MPSSettings.inventory.maxModuleCountArmorChest;
        maxModuleCountArmorLegs = MPSSettings.inventory.maxModuleCountArmorLegs;
        maxModuleCountArmorFeet = MPSSettings.inventory.maxModuleCountArmorFeet;

        /** Energy -------------------------------------------------------------------------------- */
        maxEnergyBasicBattery = MPSSettings.energy.maxEnergyBasicBattery;
        maxEnergyAdvancedBattery = MPSSettings.energy.maxEnergyAdvancedBattery;
        maxEnergyEliteBattery = MPSSettings.energy.maxEnergyEliteBattery;
        maxEnergyUltimateBattery = MPSSettings.energy.maxEnergyUltimateBattery;
        rfEnergyPerIC2EU = MPSSettings.energy.rfEnergyPerIC2EU;

        /** Heat ---------------------------------------------------------------------------------- */
        maxHeatPowerFist = MPSSettings.heat.maxHeatPowerFist;
        maxHeatPowerArmorHelmet = MPSSettings.heat.maxHeatPowerArmorHelmet;
        maxHeatPowerArmorChestplate = MPSSettings.heat.maxHeatPowerArmorChestplate;
        maxHeatPowerArmorLeggings = MPSSettings.heat.maxHeatPowerArmorLeggings;
        maxHeatPowerArmorBoots = MPSSettings.heat.maxHeatPowerArmorBoots;

        /** Models -------------------------------------------------------------------------------- */
        modelSetup = MPSSettings.modelconfig.modelSetup;
        allowHighPollyArmorModels = MPSSettings.modelconfig.allowHighPollyArmorModels;
        allowCustomHighPollyArmorModels = MPSSettings.modelconfig.allowCustomHighPollyArmor;
        allowHighPollyPowerFistModels = MPSSettings.modelconfig.allowHighPollyPowerFistModels;
        allowCustomPowerFistModels = MPSSettings.modelconfig.allowCustomPowerFistModels;
        allowCustomHighPollyPowerFistModels = MPSSettings.modelconfig.allowCustomHighPollyPowerFistModels;

        /** Modules ----------------------------------------------------------------------------------- */
        allowedModules = MPSSettings.modules.allowedModules;
        propertyInteger = MPSSettings.modules.propertyInteger;
        propertyDouble = MPSSettings.modules.propertyDouble;

    }

    /**
     * Sets all settings from a packet received client side in a new instance held in MPSSettings.
     */
    public MPSServerSettings(final DataInputStream datain) {
        /** General --------------------------------------------------------------- */
        maximumArmorPerPiece = MusePackager.getInstance().readDouble(datain);
        maximumFlyingSpeedmps = MusePackager.getInstance().readDouble(datain);
        baseMaxHeat = MusePackager.getInstance().readDouble(datain);
        useOldAutoFeeder = MusePackager.getInstance().readBoolean(datain);
        useCheatyLeatherRecipe = MusePackager.getInstance().readBoolean(datain);
        getWeightCapacity = MusePackager.getInstance().readDouble(datain);
        getSalvageChance = MusePackager.getInstance().readDouble(datain);
        useAdvancedOreScannerMessage = MusePackager.getInstance().readBoolean(datain);

        /** Inventory ------------------------------------------------------------- */
        maxModuleCountPowerFist= MusePackager.getInstance().readInt(datain);
        maxModuleCountArmorHelmet= MusePackager.getInstance().readInt(datain);
        maxModuleCountArmorChest= MusePackager.getInstance().readInt(datain);
        maxModuleCountArmorLegs= MusePackager.getInstance().readInt(datain);
        maxModuleCountArmorFeet= MusePackager.getInstance().readInt(datain);

        /** Energy ---------------------------------------------------------------- */
        maxEnergyBasicBattery = MusePackager.getInstance().readInt(datain);
        maxEnergyAdvancedBattery = MusePackager.getInstance().readInt(datain);
        maxEnergyEliteBattery = MusePackager.getInstance().readInt(datain);
        maxEnergyUltimateBattery = MusePackager.getInstance().readInt(datain);
        rfEnergyPerIC2EU = MusePackager.getInstance().readDouble(datain);

        /** Heat -------------------------------------------------------------------------------------- */
        maxHeatPowerFist = MusePackager.getInstance().readInt(datain);
        maxHeatPowerArmorHelmet = MusePackager.getInstance().readInt(datain);
        maxHeatPowerArmorChestplate = MusePackager.getInstance().readInt(datain);
        maxHeatPowerArmorLeggings = MusePackager.getInstance().readInt(datain);
        maxHeatPowerArmorBoots = MusePackager.getInstance().readInt(datain);

        /** Models ---------------------------------------------------------------- */
        modelSetup = MusePackager.getInstance().readBoolean(datain);
        allowHighPollyArmorModels = MusePackager.getInstance().readBoolean(datain);
        allowCustomHighPollyArmorModels = MusePackager.getInstance().readBoolean(datain);
        allowHighPollyPowerFistModels = MusePackager.getInstance().readBoolean(datain);
        allowCustomPowerFistModels = MusePackager.getInstance().readBoolean(datain);
        allowCustomHighPollyPowerFistModels = MusePackager.getInstance().readBoolean(datain);

        /** Modules --------------------------------------------------------------- */
        allowedModules = MusePackager.getInstance().readMap(datain, String.class, Boolean.class);
        propertyInteger = MusePackager.getInstance().readMap(datain, String.class, Integer.class);
        propertyDouble = MusePackager.getInstance().readMap(datain, String.class, Double.class);
    }

    /**
     * This is a server side operation that gets the values and writes them to the packet.
     * This packet is then sent to a new client on login to sync config values. This allows
     * the server to be able to control these settings.
     */
    public void writeToBuffer(final MusePacket packet) {
        /** General --------------------------------------------------------------- */
        packet.writeDouble(maximumArmorPerPiece);
        packet.writeDouble(maximumFlyingSpeedmps);
        packet.writeDouble(baseMaxHeat);
        packet.writeBoolean(useOldAutoFeeder);
        packet.writeBoolean(useCheatyLeatherRecipe);
        packet.writeDouble(getWeightCapacity);
        packet.writeDouble(getSalvageChance);
        packet.writeBoolean(useAdvancedOreScannerMessage);

        /** Inventory ------------------------------------------------------------- */
        packet.writeInt(maxModuleCountPowerFist);
        packet.writeInt(maxModuleCountArmorHelmet);
        packet.writeInt(maxModuleCountArmorChest);
        packet.writeInt(maxModuleCountArmorLegs);
        packet.writeInt(maxModuleCountArmorFeet);

        /** Energy ---------------------------------------------------------------- */
        packet.writeInt(maxEnergyBasicBattery);
        packet.writeInt(maxEnergyAdvancedBattery);
        packet.writeInt(maxEnergyEliteBattery);
        packet.writeInt(maxEnergyUltimateBattery);
        packet.writeDouble(rfEnergyPerIC2EU);

        /** Heat -------------------------------------------------------------------------------------- */
        packet.writeInt(maxHeatPowerFist);
        packet.writeInt(maxHeatPowerArmorHelmet);
        packet.writeInt(maxHeatPowerArmorChestplate);
        packet.writeInt(maxHeatPowerArmorLeggings);
        packet.writeInt(maxHeatPowerArmorBoots);

        /** Models ---------------------------------------------------------------- */
        packet.writeBoolean(modelSetup);
        packet.writeBoolean(allowHighPollyArmorModels);
        packet.writeBoolean(allowCustomHighPollyArmorModels);
        packet.writeBoolean(allowHighPollyPowerFistModels);
        packet.writeBoolean(allowCustomPowerFistModels);
        packet.writeBoolean(allowCustomHighPollyPowerFistModels);

        /** Modules --------------------------------------------------------------- */
        packet.writeMap(allowedModules);
        packet.writeMap(propertyInteger);
        packet.writeMap(propertyDouble);

    }

//    public void writeToNBT(final NBTTagCompound nbt) {
//
//
//
//
//
//
//
//
//
//
//    }
}