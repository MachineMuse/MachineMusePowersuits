package net.machinemuse.powersuits.common.config;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import net.machinemuse.numina.basemod.MuseLogger;
import net.machinemuse.powersuits.api.constants.MPSConfigConstants;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.common.InstallCost;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorBoots;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorChestplate;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorHelmet;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorLeggings;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static net.machinemuse.powersuits.api.constants.MPSModConstants.MODID;

@Config(modid = MODID, name = MPSConfigConstants.CONFIG_FILE)
public class MPSSettings {
    /**
     * The are all client side settings
     */
    public static HUD hud = new HUD();
    public static class HUD {
        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_USE_GRAPHICAL_METERS)
        @Config.Comment("Use Graphical Meters")
        public static boolean useGraphicalMeters = true;

        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_TOGGLE_MODULE_SPAM)
        @Config.Comment("Chat message when toggling module")
        public boolean toggleModuleSpam = false;

        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_DISPLAY_HUD)
        @Config.Comment("Display HUD")
        public boolean keybindHUDon = true;

        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_KEYBIND_HUD_X)
        @Config.Comment("x position")
        @Config.RangeDouble(min = 0)
        public double keybindHUDx = 8.0;

        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_KEYBIND_HUD_Y)
        @Config.Comment("y position")
        @Config.RangeDouble(min = 0)
        public double keybindHUDy = 32.0;
    }

    public static RecipesAllowed recipesAllowed = new RecipesAllowed();
    public static class RecipesAllowed {

        @Config.Comment("Use recipes for Thermal Expansion")
        public boolean useThermalExpansionRecipes = true;

        @Config.Comment("Use recipes for EnderIO")
        public boolean useEnderIORecipes = true;

        @Config.Comment("Use recipes for TechReborn")
        public boolean useTechRebornRecipes = true;

        @Config.Comment("Use recipes for IndustrialCraft 2")
        public boolean useIC2Recipes = true;
    }

    /**
     * A mixture of client and server side settings
     */
    public static General general = new General();
    public static class General {
        // Server side settings -----------------------------------------------
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_USE_OLD_AUTOFEEDER)
        @Config.Comment("Use Old Auto Feeder Method")
        public boolean useOldAutoFeeder = false;
        // Client side settings ------------------------------------------------------
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_USE_24_HOUR_CLOCK)
        @Config.Comment("Use a 24h clock instead of 12h")
        public boolean use24hClock = false;
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_ALLOW_CONFLICTING_KEYBINDS)
        @Config.Comment("Allow Conflicting Keybinds")
        public boolean allowConflictingKeybinds = true;
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_MAX_FLYING_SPEED)
        @Config.Comment("Maximum flight speed (in m/s)")
        public double getMaximumFlyingSpeedmps = 25.0;
        /**
         * The maximum amount of armor contribution allowed per armor piece. Total
         * armor when the full set is worn can never exceed 4 times this amount.
         */
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_GET_MAX_ARMOR_PER_PIECE)
        @Config.Comment("Maximum Armor per Piece")
        @Config.RangeDouble(min = 0, max = 8.0)
        @Config.RequiresWorldRestart
        public double getMaximumArmorPerPiece = 6.0;

        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_SALVAGE_CHANCE)
        @Config.Comment("Chance of each item being returned when salvaged")
        @Config.RangeDouble(min = 0, max = 1.0)
        public double getSalvageChance = 0.9;

        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_BASE_MAX_HEAT_POWERFIST)
        @Config.Comment("PowerFist Base Heat Cap")
        public double baseMaxHeatPowerFist = 5.0;

        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_BASE_MAX_HEAT_HELMET)
        @Config.Comment("Power Armor Helmet Heat Cap")
        public double baseMaxHeatHelmet = 5.0;

        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_BASE_MAX_HEAT_CHEST)
        @Config.Comment("Power Armor Chestplate Heat Cap")
        public double baseMaxHeatChest = 20.0;

        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_BASE_MAX_HEAT_LEGS)
        @Config.Comment("Power Armor Leggings Heat Cap")
        public double baseMaxHeatLegs = 15.0;

        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_BASE_MAX_HEAT_FEET)
        @Config.Comment("ItemModuleBase Heat Cap")
        public double baseMaxHeatFeet = 5.0;
    }

    /**
     * Currently maps need to be initialized and populated at runtime otherwise the values are not read from the config file
     * <p>
     * TODO: move to server config
     */
    public static Modules modules = new Modules();
    public static class Modules {
        @Config.LangKey(MPSConfigConstants.CONFIG_MODULES)
        @Config.Comment("Whether or not specified module is allowed")
        public Map<String, Boolean> allowedModules = new HashMap<String, Boolean>() {{
            // Debug ----------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_DEBUG, true);

            // Armor ----------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_LEATHER_PLATING__DATANAME, true);
            put(MPSModuleConstants.MODULE_IRON_PLATING__DATANAME, true);
            put(MPSModuleConstants.MODULE_DIAMOND_PLATING__DATANAME, true);
            put(MPSModuleConstants.MODULE_ENERGY_SHIELD__DATANAME, true);

            // Cosmetic -------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_TRANSPARENT_ARMOR__DATANAME, true);

            // Energy ---------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_BATTERY_BASIC__DATANAME, true);
            put(MPSModuleConstants.MODULE_BATTERY_ADVANCED__DATANAME, true);
            put(MPSModuleConstants.MODULE_BATTERY_ELITE__DATANAME, true);
            put(MPSModuleConstants.MODULE_BATTERY_ULTIMATE__DATANAME, true);
            put(MPSModuleConstants.MODULE_ADVANCED_SOLAR_GENERATOR__DATANAME, true);
            put(MPSModuleConstants.MODULE_COAL_GEN__DATANAME, true);
            put(MPSModuleConstants.MODULE_KINETIC_GENERATOR__DATANAME, true);
            put(MPSModuleConstants.MODULE_SOLAR_GENERATOR__DATANAME, true);
            put(MPSModuleConstants.MODULE_THERMAL_GENERATOR__DATANAME, true);

            // Environmental --------------------------------------------------------------
            put(MPSModuleConstants.MODULE_AIRTIGHT_SEAL__DATANAME, true);
            put(MPSModuleConstants.MODULE_APIARIST_ARMOR__DATANAME, true);
            put(MPSModuleConstants.MODULE_AUTO_FEEDER__DATANAME, true);
            put(MPSModuleConstants.MODULE_BASIC_COOLING_SYSTEM__DATANAME, true);
            put(MPSModuleConstants.MODULE_HAZMAT__DATANAME, true);
            put(MPSModuleConstants.MODULE_MOB_REPULSOR__DATANAME, true);
            put(MPSModuleConstants.MODULE_ADVANCED_COOLING_SYSTEM__DATANAME, true);
            put(MPSModuleConstants.MODULE_WATER_ELECTROLYZER__DATANAME, true);

            // Movement -------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_BLINK_DRIVE__DATANAME, true);
            put(MPSModuleConstants.MODULE_CLIMB_ASSIST__DATANAME, true);
            put(MPSModuleConstants.MODULE_FLIGHT_CONTROL__DATANAME, true);
            put(MPSModuleConstants.MODULE_GLIDER__DATANAME, true);
            put(MPSModuleConstants.MODULE_JETBOOTS__DATANAME, true);
            put(MPSModuleConstants.MODULE_JETPACK__DATANAME, true);
            put(MPSModuleConstants.MODULE_JUMP_ASSIST__DATANAME, true);
            put(MPSModuleConstants.MODULE_PARACHUTE__DATANAME, true);
            put(MPSModuleConstants.MODULE_SHOCK_ABSORBER__DATANAME, true);
            put(MPSModuleConstants.MODULE_SPRINT_ASSIST__DATANAME, true);
            put(MPSModuleConstants.MODULE_SWIM_BOOST__DATANAME, true);

            // Special --------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_CLOCK__DATANAME, true);
            put(MPSModuleConstants.MODULE_COMPASS__DATANAME, true);
            put(MPSModuleConstants.MODULE_ACTIVE_CAMOUFLAGE__DATANAME, true);
            put(MPSModuleConstants.MODULE_MAGNET__DATANAME, true);

            // Vision ---------------------------------------------------------------------
            put(MPSModuleConstants.BINOCULARS_MODULE__DATANAME, true);
            put(MPSModuleConstants.MODULE_NIGHT_VISION__DATANAME, true);
            put(MPSModuleConstants.MODULE_THAUM_GOGGLES__DATANAME, true);// done via mod compat

            // Tools --------------------------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_APPENG_EC_WIRELESS_FLUID__DATANAME, true);
            put(MPSModuleConstants.MODULE_APPENG_WIRELESS__DATANAME, true);
            put(MPSModuleConstants.MODULE_AXE__DATANAME, true);
            put(MPSModuleConstants.MODULE_CHISEL__DATANAME, false);
            put(MPSModuleConstants.MODULE_DIAMOND_PICK_UPGRADE__DATANAME, true);
            put(MPSModuleConstants.MODULE_DIMENSIONAL_RIFT__DATANAME, true);
            put(MPSModuleConstants.MODULE_FIELD_TINKER__DATANAME, true);
            put(MPSModuleConstants.MODULE_FLINT_AND_STEEL__DATANAME, true);
            put(MPSModuleConstants.MODULE_GRAFTER__DATANAME, true);
            put(MPSModuleConstants.MODULE_HOE__DATANAME, true);
            put(MPSModuleConstants.MODULE_LEAF_BLOWER__DATANAME, true);
            put(MPSModuleConstants.MODULE_LUX_CAPACITOR__DATANAME, true);
            put(MPSModuleConstants.MODULE_FIELD_TELEPORTER__DATANAME, true);
            put(MPSModuleConstants.MODULE_OC_TERMINAL__DATANAME, true);
            put(MPSModuleConstants.MODULE_OMNIPROBE__DATANAME, true);
            put(MPSModuleConstants.MODULE_OMNI_WRENCH__DATANAME, true);
            put(MPSModuleConstants.MODULE_ORE_SCANNER__DATANAME, true);
            put(MPSModuleConstants.MODULE_CM_PSD__DATANAME, true);
            put(MPSModuleConstants.MODULE_PICKAXE__DATANAME, true);
            put(MPSModuleConstants.MODULE_PORTABLE_CRAFTING__DATANAME, true);
            put(MPSModuleConstants.MODULE_REF_STOR_WIRELESS__DATANAME, true);
            put(MPSModuleConstants.MODULE_SCOOP__DATANAME, true);
            put(MPSModuleConstants.MODULE_SHEARS__DATANAME, true);
            put(MPSModuleConstants.MODULE_SHOVEL__DATANAME, true);
            put(MPSModuleConstants.MODULE_TREETAP__DATANAME, true);

            // Mining Enhancements ------------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_AOE_PICK_UPGRADE__DATANAME, true);
            put(MPSModuleConstants.MODULE_AQUA_AFFINITY__DATANAME, true);
            put(MPSModuleConstants.MODULE_FORTUNE_DATANAME, false);
            put(MPSModuleConstants.MODULE_MAD__DATANAME, true);
            put(MPSModuleConstants.MODULE_SILK_TOUCH__DATANAME, true);

            // Weapons ------------------------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_BLADE_LAUNCHER__DATANAME, true);
            put(MPSModuleConstants.MODULE_LIGHTNING__DATANAME, true);
            put(MPSModuleConstants.MODULE_MELEE_ASSIST__DATANAME, true);
            put(MPSModuleConstants.MODULE_PLASMA_CANNON__DATANAME, true);
            put(MPSModuleConstants.MODULE_RAILGUN__DATANAME, true);
        }};

        @Config.LangKey(MPSConfigConstants.CONFIG_MODULE_PROPERTY_DOUBLES)
        @Config.Comment("Value of specified property")
        public Map<String, Double> propertyDouble = new HashMap<String, Double>() {{
            put( "advSolarGenerator.daytimeEnergyGen.base", 45000.0D );
            put( "advSolarGenerator.daytimeHeatGen.base", 15.0D );
            put( "advSolarGenerator.nightTimeEnergyGen.base", 1500.0D );
            put( "advSolarGenerator.nightTimeHeatGen.base", 5.0D );
            put( "advSolarGenerator.slotPoints.base", 1.0D );
            put( "advancedBattery.maxEnergy.base", 5000000.0D );
            put( "advancedBattery.slotPoints.base", 1.0D );
            put( "advancedCoolingSystem.advCoolSysEnergyCon.advancedCoolingPower.multiplier", 160.0D );
            put( "advancedCoolingSystem.coolingBonus.advancedCoolingPower.multiplier", 7.0D );
            put( "advancedCoolingSystem.slotPoints.base", 1.0D );
            put( "aoePickUpgrade.aoeEnergyCon.base", 500.0D );
            put( "aoePickUpgrade.aoeEnergyCon.diameter.multiplier", 9500.0D );
            put( "aoePickUpgrade.aoeMiningDiameter.diameter.multiplier", 5D);
            put( "aoePickUpgrade.slotPoints.base", 1.0D );
            put( "apiaristArmor.apiaristArmorEnergyCon.base", 100.0D );
            put( "apiaristArmor.slotPoints.base", 1.0D );
            put( "appengECWirelessFluid.slotPoints.base", 1.0D );
            put( "appengWireless.slotPoints.base", 1.0D );
            put( "aquaAffinity.slotPoints.base", 1.0D );
            put( "aquaAffinity.underWaterEnergyCon.base", 0.0D );
            put( "aquaAffinity.underWaterEnergyCon.power.multiplier", 1000.0D );
            put( "aquaAffinity.underWaterHarvSpeed.base", 0.2D );
            put( "aquaAffinity.underWaterHarvSpeed.power.multiplier", 0.8D );
            put( "aurameter.slotPoints.base", 1.0D );
            put( "autoFeeder.autoFeederEfficiency.base", 50.0D );
            put( "autoFeeder.autoFeederEfficiency.efficiency.multiplier", 50.0D );
            put( "autoFeeder.eatingEnergyCon.base", 100.0D );
            put( "autoFeeder.eatingEnergyCon.efficiency.multiplier", 1000.0D );
            put( "autoFeeder.slotPoints.base", 1.0D );
            put( "axe.axeEnergyCon.base", 500.0D );
            put( "axe.axeEnergyCon.overclock.multiplier", 9500.0D );
            put( "axe.axeHarvSpd.base", 8.0D );
            put( "axe.axeHarvSpd.overclock.multiplier", 22.0D );
            put( "axe.slotPoints.base", 1.0D );
            put( "basicBattery.maxEnergy.base", 1000000.0D );
            put( "basicBattery.slotPoints.base", 1.0D );
            put( "basicCoolingSystem.coolingBonus.basicCoolingPower.multiplier", 4.0D );
            put( "basicCoolingSystem.coolingSystemEnergyCon.basicCoolingPower.multiplier", 100.0D );
            put( "basicCoolingSystem.slotPoints.base", 1.0D );
            put( "binoculars.fieldOfView.base", 0.5D );
            put( "binoculars.fieldOfView.fOVMult.multiplier", 9.5D );
            put( "binoculars.slotPoints.base", 1.0D );
            put( "bladeLauncher.slotPoints.base", 1.0D );
            put( "bladeLauncher.spinBladeDam.base", 6.0D );
            put( "bladeLauncher.spinBladeEnergyCon.base", 5000.0D );
            put( "blinkDrive.blinkDriveEnergyCon.base", 10000.0D );
            put( "blinkDrive.blinkDriveEnergyCon.range.multiplier", 30000.0D );
            put( "blinkDrive.blinkDriveRange.base", 5.0D );
            put( "blinkDrive.blinkDriveRange.range.multiplier", 59.0D );
            put( "blinkDrive.slotPoints.base", 1.0D );
            put( "climbAssist.slotPoints.base", 1.0D );
            put( "clock.slotPoints.base", 1.0D );
            put( "compass.slotPoints.base", 1.0D );
            put( "diamondPickUpgrade.slotPoints.base", 1.0D );
            put( "diamondPlating.armorPhysical.base", 5.0D );
            put( "diamondPlating.maxHeat.base", 400.0D );
            put( "diamondPlating.slotPoints.base", 1.0D );
            put( "dimRiftGen.energyCon.base", 200000.0D );
            put( "dimRiftGen.heatGeneration.base", 55.0D );
            put( "dimRiftGen.slotPoints.base", 1.0D );
            put( "eliteBattery.maxEnergy.base", 5.0E7D );
            put( "eliteBattery.slotPoints.base", 1.0D );
            put( "energyShield.armorEnergy.fieldStrength.multiplier", 6.0D );
            put( "energyShield.armorEnergyPerDamage.fieldStrength.multiplier", 5000.0D );
            put( "energyShield.maxHeat.fieldStrength.multiplier", 500.0D );
            put( "energyShield.slotPoints.base", 1.0D );
            put( "fieldTinkerer.slotPoints.base", 1.0D );
            put( "flightControl.slotPoints.base", 1.0D );
            put( "flightControl.yLookRatio.vertically.multiplier", 1.0D );
            put( "flintAndSteel.ignitEnergyCon.base", 10000.0D );
            put( "flintAndSteel.slotPoints.base", 1.0D );
            put( "fortuneModule.fortuneEnCon.base", 500.0D );
            put( "fortuneModule.fortuneEnCon.enchLevel.multiplier", 9500.0D );
            put( "fortuneModule.fortuneLevel.enchLevel.multiplier", 3D);
            put( "fortuneModule.slotPoints.base", 1.0D );
            put( "glider.slotPoints.base", 1.0D );
            put( "grafter.grafterEnergyCon.base", 10000.0D );
            put( "grafter.grafterHeatGen.base", 20.0D );
            put( "grafter.slotPoints.base", 1.0D );
            put( "hazmat.slotPoints.base", 1.0D );
            put( "hoe.hoeEnergyCon.base", 500.0D );
            put( "hoe.hoeEnergyCon.radius.multiplier", 9500.0D );
            put( "hoe.hoeSearchRad.radius.multiplier", 8.0D );
            put( "hoe.slotPoints.base", 1.0D );
            put( "invisibility.slotPoints.base", 1.0D );
            put( "ironPlating.armorPhysical.base", 4.0D );
            put( "ironPlating.maxHeat.base", 300.0D );
            put( "ironPlating.slotPoints.base", 1.0D );
            put( "jetBoots.jetBootsEnergyCon.base", 0.0D );
            put( "jetBoots.jetBootsEnergyCon.thrust.multiplier", 750.0D );
            put( "jetBoots.jetbootsThrust.base", 0.0D );
            put( "jetBoots.jetbootsThrust.thrust.multiplier", 0.08D );
            put( "jetBoots.slotPoints.base", 1.0D );
            put( "jetpack.jetpackEnergyCon.base", 0.0D );
            put( "jetpack.jetpackEnergyCon.thrust.multiplier", 1500.0D );
            put( "jetpack.jetpackThrust.base", 0.0D );
            put( "jetpack.jetpackThrust.thrust.multiplier", 0.16D );
            put( "jetpack.slotPoints.base", 1.0D );
            put( "jumpAssist.jumpBoost.base", 1.0D );
            put( "jumpAssist.jumpBoost.power.multiplier", 4.0D );
            put( "jumpAssist.jumpEnergyCon.base", 0.0D );
            put( "jumpAssist.jumpEnergyCon.compensation.multiplier", 50.0D );
            put( "jumpAssist.jumpEnergyCon.power.multiplier", 250.0D );
            put( "jumpAssist.jumpExhaustComp.base", 0.0D );
            put( "jumpAssist.jumpExhaustComp.compensation.multiplier", 1.0D );
            put( "jumpAssist.slotPoints.base", 1.0D );
            put( "kineticGenerator.energyPerBlock.base", 2000.0D );
            put( "kineticGenerator.energyPerBlock.energyGenerated.multiplier", 6000.0D );
            put( "kineticGenerator.movementResistance.base", 0.0D );
            put( "kineticGenerator.movementResistance.energyGenerated.multiplier", 0.5D );
            put( "kineticGenerator.slotPoints.base", 1.0D );
            put( "leafBlower.energyCon.base", 500.0D );
            put( "leafBlower.energyCon.radius.multiplier", 9500.0D );
            put( "leafBlower.radius.base", 1.0D );
            put( "leafBlower.radius.radius.multiplier", 15.0D );
            put( "leafBlower.slotPoints.base", 1.0D );
            put( "leatherPlating.armorPhysical.base", 3.0D );
            put( "leatherPlating.maxHeat.base", 75.0D );
            put( "leatherPlating.slotPoints.base", 1.0D );
            put( "lightningSummoner.energyCon.base", 4900000.0D );
            put( "lightningSummoner.heatEmission.base", 100.0D );
            put( "lightningSummoner.slotPoints.base", 1.0D );
            put( "luxCapacitor.luxCapBlue.blue.multiplier", 1.0D );
            put( "luxCapacitor.luxCapEnergyCon.base", 1000.0D );
            put( "luxCapacitor.luxCapGreen.green.multiplier", 1.0D );
            put( "luxCapacitor.luxCapRed.red.multiplier", 1.0D );
            put( "luxCapacitor.slotPoints.base", 1.0D );
            put( "madModule.energyCon.base", 100.0D );
            put( "madModule.slotPoints.base", 1.0D );
            put( "magnet.energyCon.base", 0.0D );
            put( "magnet.energyCon.power.multiplier", 2000.0D );
            put( "magnet.magnetRadius.base", 5.0D );
            put( "magnet.magnetRadius.power.multiplier", 10.0D );
            put( "magnet.slotPoints.base", 1.0D );
            put( "meleeAssist.meleeDamage.base", 2.0D );
            put( "meleeAssist.meleeDamage.impact.multiplier", 8.0D );
            put( "meleeAssist.meleeKnockback.carryThrough.multiplier", 1.0D );
            put( "meleeAssist.punchEnergyCon.base", 10.0D );
            put( "meleeAssist.punchEnergyCon.carryThrough.multiplier", 200.0D );
            put( "meleeAssist.punchEnergyCon.impact.multiplier", 1000.0D );
            put( "meleeAssist.slotPoints.base", 1.0D );
            put( "mobRepulsor.repulsorEnergyCon.base", 2500.0D );
            put( "mobRepulsor.slotPoints.base", 1.0D );
            put( "nightVision.slotPoints.base", 1.0D );
            put( "omniwrench.slotPoints.base", 1.0D );
            put( "oreScanner.slotPoints.base", 1.0D );
            put( "parachute.slotPoints.base", 1.0D );
            put( "pickaxe.pickHarvSpd.base", 8.0D );
            put( "pickaxe.pickHarvSpd.overclock.multiplier", 52.0D );
            put( "pickaxe.pickaxeEnergyCon.base", 500.0D );
            put( "pickaxe.pickaxeEnergyCon.overclock.multiplier", 9500.0D );
            put( "pickaxe.slotPoints.base", 1.0D );
            put( "plasmaCannon.plasmaDamage.amperage.multiplier", 38.0D );
            put( "plasmaCannon.plasmaDamage.base", 2.0D );
            put( "plasmaCannon.plasmaEnergyPerTick.amperage.multiplier", 1500.0D );
            put( "plasmaCannon.plasmaEnergyPerTick.base", 100.0D );
            put( "plasmaCannon.plasmaEnergyPerTick.voltage.multiplier", 500.0D );
            put( "plasmaCannon.plasmaExplosiveness.voltage.multiplier", 0.5D );
            put( "plasmaCannon.slotPoints.base", 1.0D );
            put( "portableCraftingTable.slotPoints.base", 1.0D );
            put( "railgun.railgunEnergyCost;.base", 5000.0D );
            put( "railgun.railgunEnergyCost;.voltage.multiplier", 25000.0D );
            put( "railgun.railgunHeatEm.base", 2.0D );
            put( "railgun.railgunHeatEm.voltage.multiplier", 10.0D );
            put( "railgun.railgunTotalImpulse.base", 500.0D );
            put( "railgun.railgunTotalImpulse.voltage.multiplier", 2500.0D );
            put( "railgun.slotPoints.base", 1.0D );
            put( "refinedStorageWirelessGrid.slotPoints.base", 1.0D );
            put( "scoop.scoopEnergyCon.base", 20000.0D );
            put( "scoop.scoopHarSpd.base", 5.0D );
            put( "scoop.slotPoints.base", 1.0D );
            put( "shears.shearEnergyCon.base", 1000.0D );
            put( "shears.shearHarvSpd.base", 8.0D );
            put( "shears.slotPoints.base", 1.0D );
            put( "shockAbsorber.distanceRed.base", 0.0D );
            put( "shockAbsorber.distanceRed.power.multiplier", 10.0D );
            put( "shockAbsorber.impactEnergyCon.base", 0.0D );
            put( "shockAbsorber.impactEnergyCon.power.multiplier", 100.0D );
            put( "shockAbsorber.slotPoints.base", 1.0D );
            put( "shovel.shovelEnergyCon.base", 500.0D );
            put( "shovel.shovelEnergyCon.overclock.multiplier", 9500.0D );
            put( "shovel.shovelHarvSpd.base", 8.0D );
            put( "shovel.shovelHarvSpd.overclock.multiplier", 22.0D );
            put( "shovel.slotPoints.base", 1.0D );
            put( "silk_touch.silkTouchEnCon.base", 2500.0D );
            put( "silk_touch.slotPoints.base", 1.0D );
            put( "solarGenerator.daytimeEnergyGen.base", 15000.0D );
            put( "solarGenerator.nightTimeEnergyGen.base", 1500.0D );
            put( "solarGenerator.slotPoints.base", 1.0D );
            put( "sprintAssist.slotPoints.base", 1.0D );
            put( "sprintAssist.sprintEnergyCon.base", 0.0D );
            put( "sprintAssist.sprintEnergyCon.compensation.multiplier", 20.0D );
            put( "sprintAssist.sprintEnergyCon.sprintAssist.multiplier", 100.0D );
            put( "sprintAssist.sprintExComp.base", 0.0D );
            put( "sprintAssist.sprintExComp.compensation.multiplier", 1.0D );
            put( "sprintAssist.sprintSpeedMult.base", 0.01D );
            put( "sprintAssist.sprintSpeedMult.sprintAssist.multiplier", 2.49D );
            put( "sprintAssist.walkingEnergyCon.base", 0.0D );
            put( "sprintAssist.walkingEnergyCon.walkingAssist.multiplier", 100.0D );
            put( "sprintAssist.walkingSpeedMult.base", 0.01D );
            put( "sprintAssist.walkingSpeedMult.walkingAssist.multiplier", 1.99D );
            put( "swimAssist.slotPoints.base", 1.0D );
            put( "swimAssist.swimBoostEnergyCon.thrust.multiplier", 1000.0D );
            put( "swimAssist.underwaterMovBoost.thrust.multiplier", 1.0D );
            put( "thermalGenerator.slotPoints.base", 1.0D );
            put( "thermalGenerator.thermalEnergyGen.base", 250.0D );
            put( "thermalGenerator.thermalEnergyGen.energyGenerated.multiplier", 250.0D );
            put( "transparentArmor.slotPoints.base", 1.0D );
            put( "treetap.energyCon.base", 1000.0D );
            put( "treetap.slotPoints.base", 1.0D );
            put( "ultimateBattery.maxEnergy.base", 1.0E8D );
            put( "ultimateBattery.slotPoints.base", 1.0D );
            put( "waterElectrolyzer.joltEnergy.base", 10000.0D );
            put( "waterElectrolyzer.slotPoints.base", 1.0D );
        }};

        @Config.LangKey(MPSConfigConstants.CONFIG_MODULE_PROPERTY_INTEGERS)
        @Config.Comment("Value of specified property")
        public Map<String, Integer> propertyInteger = new HashMap<String, Integer>() {{

        }};
    }

    public static Limits limits = new Limits();
    public static class Limits {
        @Config.LangKey(MPSConfigConstants.CONFIG_LIMITS_MAX_ARMOR_MODULES)
        @Config.Comment("Max number of Armor modules per armor item")
        @Config.RangeInt(min = 0, max = 99)
        public int maxArmorModules = 1;

        @Config.LangKey(MPSConfigConstants.CONFIG_LIMITS_MAX_ENERGY_STORAGE_MODULES)
        @Config.Comment("Max number of Energy Storage modules per item")
        @Config.RangeInt(min = 0, max = 99)
        public int maxEnergyStorageModules = 1;

        @Config.LangKey(MPSConfigConstants.CONFIG_LIMITS_MAX_ENERGY_GENERATION_MODULES)
        @Config.Comment("Max number of Energy Storage modules per item")
        @Config.RangeInt(min = 0, max = 99)
        public int maxEnergyGenModules = 1;

        @Config.LangKey(MPSConfigConstants.CONFIG_LIMITS_MAX_TOOL_MODULES)
        @Config.Comment("Max number of Tool modules per Power Fist")
        @Config.RangeInt(min = 0, max = 99)
        public int maxToolModules = 99;

        @Config.LangKey(MPSConfigConstants.CONFIG_LIMITS_MAX_WEAPON_MODULES)
        @Config.Comment("Max number of Weapon modules per Power Fist")
        @Config.RangeInt(min = 0, max = 99)
        public int maxWeaponModules = 99;

        @Config.LangKey(MPSConfigConstants.CONFIG_LIMITS_MAX_MOVEMENT_MODULES)
        @Config.Comment("Max number of Movement modules per item")
        @Config.RangeInt(min = 0, max = 99)
        public int maxMovementModules = 99;

        @Config.LangKey(MPSConfigConstants.CONFIG_LIMITS_COSMETIC_MODULES)
        @Config.Comment("Max number of Cosmetic modules per item")
        @Config.RangeInt(min = 0, max = 99)
        public int maxCosmeticModules = 99;

        @Config.LangKey(MPSConfigConstants.CONFIG_LIMITS_MAX_VISION_MODULES)
        @Config.Comment("Max number of Vision modules per item")
        @Config.RangeInt(min = 0, max = 99)
        public int maxVisionModules = 99;

        @Config.LangKey(MPSConfigConstants.CONFIG_LIMITS_MAX_ENVIRONMENTAL_MODULES)
        @Config.Comment("Max number of Environmental modules per item")
        @Config.RangeInt(min = 0, max = 99)
        public int maxEnvironmentalModules = 99;

        @Config.LangKey(MPSConfigConstants.CONFIG_LIMITS_MAX_SPECIAL_MODULES)
        @Config.Comment("Max number of Special modules per item")
        @Config.RangeInt(min = 0, max = 99)
        public int maxSpecialModules = 99;

        @Config.LangKey(MPSConfigConstants.CONFIG_LIMITS_MAX_MINING_ENHANCEMENT_MODULES)
        @Config.Comment("Max number of Mining Enhancement modules per Power Fist")
        @Config.RangeInt(min = 0, max = 99)
        public int maxMiningEnhancementModules = 99;
    }

    public static Cosmetics cosmetics = new Cosmetics();
    public static class Cosmetics {
        @Config.Comment("Use legacy cosmetic configuration instead of cosmetic presets")
        public boolean useLegacyCosmeticSystem=true;

        //        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_ALLOW_CONFLICTING_KEYBINDS)
        @Config.Comment("Allow high polly armor models instead of just skins")
        public boolean allowHighPollyArmorModuels = true;

        @Config.Comment("Allow PowerFist model to be customized")
        public boolean allowPowerFistCustomization=false;

        public void updateCosmeticInfo(ResourceLocation location, String name, NBTTagCompound cosmeticInfo) {
            Item item = Item.REGISTRY.getObject(location);

            if (item instanceof ItemPowerFist)
                cosmeticPresetsPowerFist.put(name, cosmeticInfo);
            else if (item instanceof ItemPowerArmorHelmet)
                cosmeticPresetsPowerArmorHelmet.put(name, cosmeticInfo);
            else if (item instanceof ItemPowerArmorChestplate)
                cosmeticPresetsPowerArmorChestplate.put(name, cosmeticInfo);
            else if (item instanceof ItemPowerArmorLeggings)
                cosmeticPresetsPowerArmorLeggings.put(name, cosmeticInfo);
            else if (item instanceof ItemPowerArmorBoots)
                cosmeticPresetsPowerArmorBoots.put(name, cosmeticInfo);
        }

        @Config.Ignore
        private BiMap<String, NBTTagCompound> cosmeticPresetsPowerFist = HashBiMap.create();
        public BiMap<String, NBTTagCompound> getCosmeticPresetsPowerFist() {
            if (cosmeticPresetsPowerFist.isEmpty() && !allowPowerFistCustomization)
                cosmeticPresetsPowerFist = CosmeticPresetSaveLoad.loadPresetsForItem(MPSItems.powerFist, 0);
            return cosmeticPresetsPowerFist;
        }

        @Config.Ignore
        private BiMap<String, NBTTagCompound> cosmeticPresetsPowerArmorHelmet = HashBiMap.create();
        public BiMap<String, NBTTagCompound> getCosmeticPresetsPowerArmorHelmet() {
            if (cosmeticPresetsPowerArmorHelmet.isEmpty() && !useLegacyCosmeticSystem)
                cosmeticPresetsPowerArmorHelmet = CosmeticPresetSaveLoad.loadPresetsForItem(MPSItems.powerArmorHead, 0);
            return cosmeticPresetsPowerArmorHelmet;
        }

        @Config.Ignore
        private BiMap<String, NBTTagCompound> cosmeticPresetsPowerArmorChestplate = HashBiMap.create();
        public BiMap<String, NBTTagCompound> getCosmeticPresetsPowerArmorChestplate() {
            if(cosmeticPresetsPowerArmorChestplate.isEmpty() && !useLegacyCosmeticSystem)
                cosmeticPresetsPowerArmorChestplate = CosmeticPresetSaveLoad.loadPresetsForItem(MPSItems.powerArmorTorso, 0);
            return cosmeticPresetsPowerArmorChestplate;
        }

        @Config.Ignore
        private BiMap<String, NBTTagCompound> cosmeticPresetsPowerArmorLeggings = HashBiMap.create();
        public BiMap<String, NBTTagCompound> getCosmeticPresetsPowerArmorLeggings() {
            if(cosmeticPresetsPowerArmorLeggings.isEmpty() && !useLegacyCosmeticSystem)
                cosmeticPresetsPowerArmorLeggings = CosmeticPresetSaveLoad.loadPresetsForItem(MPSItems.powerArmorLegs, 0);
            return cosmeticPresetsPowerArmorLeggings;
        }

        @Config.Ignore
        private BiMap<String, NBTTagCompound>  cosmeticPresetsPowerArmorBoots = HashBiMap.create();
        public BiMap<String, NBTTagCompound> getCosmeticPresetsPowerArmorBoots() {
            if(cosmeticPresetsPowerArmorBoots.isEmpty() && !useLegacyCosmeticSystem)
                cosmeticPresetsPowerArmorBoots = CosmeticPresetSaveLoad.loadPresetsForItem(MPSItems.powerArmorFeet, 0);
            return cosmeticPresetsPowerArmorBoots;
        }
    }

    public static void loadCustomInstallCosts() {
        try {
            File installCostFile = new File(Loader.instance().getConfigDir() + "/machinemuse/", "custominstallcosts.json");
            Gson gson = new Gson();
            if (installCostFile.exists()) {
                DataInputStream is = new DataInputStream(new FileInputStream(installCostFile));
                byte[] bytes = new byte[(int) installCostFile.length()];
                is.readFully(bytes);
                String string = Charset.defaultCharset().decode(ByteBuffer.wrap(bytes)).toString();
                is.close();

                MuseLogger.logDebug(string);
                InstallCost[] costs = (InstallCost[])gson.fromJson(string, (Class)InstallCost[].class);
                for(InstallCost cost: costs) {
                    String moduleName = cost.moduleName;
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(cost.modId, cost.itemName));
                    if(item != null) {
                        int metadata = (cost.itemMetadata == null) ? 0 : cost.itemMetadata;
                        int quantity = (cost.itemQuantity == null) ? 1 : cost.itemQuantity;
                        ItemStack stack = new ItemStack(item, quantity, metadata);
                        if(!stack.isEmpty()) {
                            ModuleManager.INSTANCE.addCustomInstallCost(moduleName, stack);
                        } else {
                            MuseLogger.logError("Invalid Itemstack in custom install cost. Module [" + cost.moduleName + "], item [" + cost.itemName + "]");
                        }
                    } else {
                        MuseLogger.logError("Invalid Item in custom install cost. Module [" + cost.moduleName + "], item [" + cost.itemName + "]");
                    }
                }
            } else {
                installCostFile.createNewFile();
                InstallCost examplecost = new InstallCost();
                examplecost.moduleName = "Shock Absorber";
                examplecost.itemName = "wool";
                examplecost.modId = "minecraft";
                examplecost.itemQuantity = 2;
                examplecost.itemMetadata = 0;
                InstallCost examplecost2 = new InstallCost();
                examplecost2.moduleName = "Shock Absorber";
                examplecost2.itemName = "powerArmorComponent";
                examplecost2.modId = "powersuits";
                examplecost2.itemQuantity = 2;
                examplecost2.itemMetadata = 2;
                InstallCost[] output = { examplecost, examplecost2 };
                String json = gson.toJson(output);
                PrintWriter dest = new PrintWriter(installCostFile);
                dest.write(json);
                dest.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}