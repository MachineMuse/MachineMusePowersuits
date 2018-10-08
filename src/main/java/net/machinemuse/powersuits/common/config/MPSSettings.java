package net.machinemuse.powersuits.common.config;

import net.machinemuse.powersuits.api.constants.MPSConfigConstants;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.minecraftforge.common.config.Config;

import java.util.HashMap;
import java.util.Map;

import static net.machinemuse.numina.api.constants.NuminaConstants.*;
import static net.machinemuse.powersuits.api.constants.MPSModConstants.MODID;

@Config(modid = MODID, name = MPSConfigConstants.CONFIG_FILE)
public class MPSSettings {
    /**
     * The are all client side settings
     */
    public static HUD hud = new HUD();
    public static class HUD {
        //FIXME: This doesn't seem to even be used
        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_USE_HUD)
        @Config.Comment("Use HUD for certain module (Auto Feeder, Compass, Clock, etc.")
        public static boolean useHUDStuff = true;

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

        @Config.LangKey(MPSConfigConstants.CONFIG_HUD_USE_GRAPHICAL_METERS)
        @Config.Comment("Use Graphical Meters")
        public static boolean useGraphicalMeters = true;
    }


    /**
     * A mixture of client and server side settings
     */
    public static General general = new General();
    public static class General {
        // Client side settings ------------------------------------------------------
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_USE_24_HOUR_CLOCK)
        @Config.Comment("Use a 24h clock instead of 12h")
        public boolean use24hClock = false;

        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_ALLOW_CONFLICTING_KEYBINDS)
        @Config.Comment("Allow Conflicting Keybinds")
        public boolean allowConflictingKeybinds = true;

        // Server side settings -----------------------------------------------
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_USE_OLD_AUTOFEEDER)
        @Config.Comment("Use Old Auto Feeder Method")
        public static boolean useOldAutoFeeder = false;

        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_MAX_FLYING_SPEED)
        @Config.Comment("Maximum flight speed (in m/s)")
        public double getMaximumFlyingSpeedmps = 25.0;

        //TODO: eleiminate
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_WEIGHT_LIMIT)
        @Config.Comment("Weight Limit (grams)")
        public static double getWeightCapacity = 25000.0;

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
     *
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
            put(MPSModuleConstants.AIRTIGHT_SEAL_MODULE__DATANAME, true);
            put(MPSModuleConstants.MODULE_APIARIST_ARMOR__DATANAME, true);
            put(MPSModuleConstants.MODULE_AUTO_FEEDER__DATANAME, true);
            put(MPSModuleConstants.MODULE_COOLING_SYSTEM__DATANAME, true);
            put(MPSModuleConstants.MODULE_HAZMAT__DATANAME, true);
            put(MPSModuleConstants.MODULE_MOB_REPULSOR__DATANAME, true);
            put(MPSModuleConstants.MODULE_NITROGEN_COOLING_SYSTEM__DATANAME, true);
            put(MPSModuleConstants.MODULE_WATER_ELECTROLYZER__DATANAME, true);
//            put(MPSModuleConstants.MODULE_WATER_TANK__DATANAME, true);

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
            put(MPSModuleConstants.MODULE_AOE_PICK_UPGRADE__DATANAME, true);
            put(MPSModuleConstants.MODULE_APPENG_EC_WIRELESS_FLUID__DATANAME, true);
            put(MPSModuleConstants.MODULE_APPENG_WIRELESS__DATANAME, true);
            put(MPSModuleConstants.MODULE_AQUA_AFFINITY__DATANAME, true);
            put(MPSModuleConstants.MODULE_AXE__DATANAME, true);
            put(MPSModuleConstants.MODULE_CHISEL__DATANAME, true);
            put(MPSModuleConstants.MODULE_DIAMOND_PICK_UPGRADE__DATANAME, true);
            put(MPSModuleConstants.MODULE_DIMENSIONAL_RIFT__DATANAME, true);
            put(MPSModuleConstants.MODULE_FIELD_TINKER__DATANAME, true);
            put(MPSModuleConstants.MODULE_FLINT_AND_STEEL__DATANAME, true);
            put(MPSModuleConstants.MODULE_GRAFTER__DATANAME, true);
            put(MPSModuleConstants.MODULE_HOE__DATANAME, true);
            put(MPSModuleConstants.MODULE_LEAF_BLOWER__DATANAME, true);
            put(MPSModuleConstants.MODULE_LUX_CAPACITOR__DATANAME, true);
            put(MPSModuleConstants.MODULE_FIELD_TELEPORTER__DATANAME, true);
            put(MPSModuleConstants.MODULE_MAD_MODULE__DATANAME, true);
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
            put( "advSolarGenerator.Daytime Energy Generation.base", 45000.0D );
            put( "advSolarGenerator.Daytime Heat Generation.base", 15.0D );
            put( "advSolarGenerator.Nighttime Energy Generation.base", 1500.0D );
            put( "advSolarGenerator.Nighttime Heat Generation.base", 5.0D );
            put( "advSolarGenerator.Slot Points.base", 5.0D );
            put( "advancedBattery.Maximum Energy.Battery Size.multiplier", 4000000.0D );
            put( "advancedBattery.Maximum Energy.base", 1000000.0D );
            put( "advancedBattery.Slot Points.base", 1.0D );
            put( "advancedCoolingSystem.Cooling Bonus.Advanced Cooling Power.multiplier", 7.0D );
            put( "advancedCoolingSystem.NitrogenCoolingSysEnergy Consumption.Advanced Cooling Power.multiplier", 160.0D );
            put( "advancedCoolingSystem.Slot Points.base", 5.0D );
            put( "apiaristArmor.Apiarist Armor Energy Consumption.base", 100.0D );
            put( "apiaristArmor.Slot Points.base", 5.0D );
            put( "appengECWirelessFluid.Slot Points.base", 5.0D );
            put( "appengWireless.Slot Points.base", 5.0D );
            put( "aquaAffinity.Slot Points.base", 5.0D );
            put( "aquaAffinity.Underwater Energy Consumption.Power.multiplier", 1000.0D );
            put( "aquaAffinity.Underwater Energy Consumption.base", 0.0D );
            put( "aquaAffinity.Underwater Harvest Speed.Power.multiplier", 0.8D );
            put( "aquaAffinity.Underwater Harvest Speed.base", 0.2D );
            put( "aurameter.Slot Points.base", 5.0D );
            put( "autoFeeder.Auto-Feeder Efficiency.Efficiency.multiplier", 50.0D );
            put( "autoFeeder.Auto-Feeder Efficiency.base", 50.0D );
            put( "autoFeeder.Eating Energy Consumption.Efficiency.multiplier", 1000.0D );
            put( "autoFeeder.Eating Energy Consumption.base", 100.0D );
            put( "autoFeeder.Slot Points.base", 1.0D );
            put( "axe.Axe Energy Consumption.Overclock.multiplier", 9500.0D );
            put( "axe.Axe Energy Consumption.base", 500.0D );
            put( "axe.Axe Harvest Speed.Overclock.multiplier", 22.0D );
            put( "axe.Axe Harvest Speed.base", 8.0D );
            put( "axe.Slot Points.base", 5.0D );
            put( "basicBattery.Maximum Energy.Battery Size.multiplier", 800000.0D );
            put( "basicBattery.Maximum Energy.base", 200000.0D );
            put( "basicBattery.Slot Points.base", 1.0D );
            put( "basicCoolingSystem.Cooling Bonus.Water Cooling Power.multiplier", 4.0D );
            put( "basicCoolingSystem.Cooling System Energy Consumption.Water Cooling Power.multiplier", 100.0D );
            put( "basicCoolingSystem.Slot Points.base", 5.0D );
            put( "binoculars.Field of View.FOV multiplier.multiplier", 9.5D );
            put( "binoculars.Field of View.base", 0.5D );
            put( "binoculars.Slot Points.base", 5.0D );
            put( "bladeLauncher.Slot Points.base", 5.0D );
            put( "bladeLauncher.Spinning Blade Damage.base", 6.0D );
            put( "bladeLauncher.Spinning Blade Energy Consumption.base", 5000.0D );
            put( "blinkDrive.Blink Drive Energy Consuption.Range.multiplier", 30000.0D );
            put( "blinkDrive.Blink Drive Energy Consuption.base", 10000.0D );
            put( "blinkDrive.Blink Drive Range.Range.multiplier", 59.0D );
            put( "blinkDrive.Blink Drive Range.base", 5.0D );
            put( "blinkDrive.Slot Points.base", 5.0D );
            put( "climbAssist.Slot Points.base", 5.0D );
            put( "diamondPickUpgrade.Slot Points.base", 5.0D );
            put( "diamondPlating.Armor (Physical).Plating Thickness.multiplier", 6.0D );
            put( "diamondPlating.Maximum Heat.Plating Thickness.multiplier", 400.0D );
            put( "diamondPlating.Slot Points.base", 1.0D );
            put( "dimRiftGen.Energy Consumption.base", 200000.0D );
            put( "dimRiftGen.Heat Generation.base", 55.0D );
            put( "dimRiftGen.Slot Points.base", 5.0D );
            put( "eliteBattery.Maximum Energy.Battery Size.multiplier", 4.25E7D );
            put( "eliteBattery.Maximum Energy.base", 7500000.0D );
            put( "eliteBattery.Slot Points.base", 1.0D );
            put( "energyShield.Armor (Energy).Field Strength.multiplier", 6.0D );
            put( "energyShield.Energy Per Damage.Field Strength.multiplier", 5000.0D );
            put( "energyShield.Maximum Heat.Field Strength.multiplier", 500.0D );
            put( "energyShield.Slot Points.base", 1.0D );
            put( "flightControl.Slot Points.base", 5.0D );
            put( "flightControl.Y-look ratio.Verticality.multiplier", 1.0D );
            put( "flintAndSteel.Ignition Energy Consumption.base", 10000.0D );
            put( "flintAndSteel.Slot Points.base", 5.0D );
            put( "glider.Slot Points.base", 5.0D );
            put( "grafter.Grafter Energy Consumption.base", 10000.0D );
            put( "grafter.Grafter Heat Generation.base", 20.0D );
            put( "grafter.Slot Points.base", 5.0D );
            put( "hazmat.Slot Points.base", 5.0D );
            put( "hoe.Hoe Energy Consumption.Search Radius.multiplier", 9500.0D );
            put( "hoe.Hoe Energy Consumption.base", 500.0D );
            put( "hoe.Hoe Search Radius.Search Radius.multiplier", 8.0D );
            put( "hoe.Slot Points.base", 5.0D );
            put( "ironPlating.Armor (Physical).Plating Thickness.multiplier", 5.0D );
            put( "ironPlating.Maximum Heat.Plating Thickness.multiplier", 300.0D );
            put( "ironPlating.Slot Points.base", 1.0D );
            put( "jetBoots.Jetboots Energy Consumption.Thrust.multiplier", 750.0D );
            put( "jetBoots.Jetboots Energy Consumption.base", 0.0D );
            put( "jetBoots.Jetboots Thrust.Thrust.multiplier", 0.08D );
            put( "jetBoots.Jetboots Thrust.base", 0.0D );
            put( "jetBoots.Slot Points.base", 1.0D );
            put( "jetpack.Jetpack Energy Consumption.Thrust.multiplier", 1500.0D );
            put( "jetpack.Jetpack Energy Consumption.base", 0.0D );
            put( "jetpack.Jetpack Thrust.Thrust.multiplier", 0.16D );
            put( "jetpack.Jetpack Thrust.base", 0.0D );
            put( "jetpack.Slot Points.base", 1.0D );
            put( "jumpAssist.Jump Boost.Power.multiplier", 4.0D );
            put( "jumpAssist.Jump Boost.base", 1.0D );
            put( "jumpAssist.Jump Energy Consumption.Compensation.multiplier", 50.0D );
            put( "jumpAssist.Jump Energy Consumption.Power.multiplier", 250.0D );
            put( "jumpAssist.Jump Energy Consumption.base", 0.0D );
            put( "jumpAssist.Jump Exhaustion Compensation.Compensation.multiplier", 1.0D );
            put( "jumpAssist.Jump Exhaustion Compensation.base", 0.0D );
            put( "jumpAssist.Slot Points.base", 1.0D );
            put( "kineticGenerator.Energy Per 5 Blocks.Energy Generated.multiplier", 6000.0D );
            put( "kineticGenerator.Energy Per 5 Blocks.base", 2000.0D );
            put( "kineticGenerator.Heat Generation.base", 5.0D );
            put( "kineticGenerator.Slot Points.base", 1.0D );
            put( "leafBlower.Energy Consumption.Radius.multiplier", 9500.0D );
            put( "leafBlower.Energy Consumption.base", 500.0D );
            put( "leafBlower.Radius.Radius.multiplier", 15.0D );
            put( "leafBlower.Radius.base", 1.0D );
            put( "leafBlower.Slot Points.base", 5.0D );
            put( "leatherPlating.Armor (Physical).Plating Thickness.multiplier", 3.0D );
            put( "leatherPlating.Maximum Heat.Plating Thickness.multiplier", 75.0D );
            put( "leatherPlating.Slot Points.base", 1.0D );
            put( "lightningSummoner.Energy Consumption.base", 4900000.0D );
            put( "lightningSummoner.Heat Emission.base", 100.0D );
            put( "lightningSummoner.Slot Points.base", 5.0D );
            put( "luxCapacitor.Lux Capacitor Blue Hue.Blue.multiplier", 1.0D );
            put( "luxCapacitor.Lux Capacitor Energy Consumption.base", 1000.0D );
            put( "luxCapacitor.Lux Capacitor Green Hue.Green.multiplier", 1.0D );
            put( "luxCapacitor.Lux Capacitor Red Hue.Red.multiplier", 1.0D );
            put( "luxCapacitor.Slot Points.base", 5.0D );
            put( "madModule.Slot Points.base", 5.0D );
            put( "magnet.Energy Consumption.Power.multiplier", 2000.0D );
            put( "magnet.Energy Consumption.base", 0.0D );
            put( "magnet.Magnet Radius.Power.multiplier", 10.0D );
            put( "magnet.Magnet Radius.base", 5.0D );
            put( "magnet.Slot Points.base", 1.0D );
            put( "meleeAssist.Melee Damage.Impact.multiplier", 8.0D );
            put( "meleeAssist.Melee Damage.base", 2.0D );
            put( "meleeAssist.Melee Knockback.Carry-through.multiplier", 1.0D );
            put( "meleeAssist.Punch Energy Consumption.Carry-through.multiplier", 200.0D );
            put( "meleeAssist.Punch Energy Consumption.Impact.multiplier", 1000.0D );
            put( "meleeAssist.Punch Energy Consumption.base", 10.0D );
            put( "meleeAssist.Slot Points.base", 5.0D );
            put( "mobRepulsor.Repulsor Energy Consumption.base", 2500.0D );
            put( "mobRepulsor.Slot Points.base", 5.0D );
            put( "nightVision.Slot Points.base", 5.0D );
            put( "omniProbe.Slot Points.base", 5.0D );
            put( "omniwrench.Slot Points.base", 5.0D );
            put( "oreScanner.Slot Points.base", 5.0D );
            put( "parachute.Slot Points.base", 5.0D );
            put( "pickaxe.Pickaxe Energy Consumption.Overclock.multiplier", 9500.0D );
            put( "pickaxe.Pickaxe Energy Consumption.base", 500.0D );
            put( "pickaxe.Pickaxe Harvest Speed.Overclock.multiplier", 52.0D );
            put( "pickaxe.Pickaxe Harvest Speed.base", 8.0D );
            put( "pickaxe.Slot Points.base", 5.0D );
            put( "plasmaCannon.Plasma Damage At Full Charge.Amperage.multiplier", 38.0D );
            put( "plasmaCannon.Plasma Damage At Full Charge.base", 2.0D );
            put( "plasmaCannon.Plasma Energy Per Tick.Amperage.multiplier", 1500.0D );
            put( "plasmaCannon.Plasma Energy Per Tick.Voltage.multiplier", 500.0D );
            put( "plasmaCannon.Plasma Energy Per Tick.base", 100.0D );
            put( "plasmaCannon.Plasma Explosiveness.Voltage.multiplier", 0.5D );
            put( "plasmaCannon.Slot Points.base", 5.0D );
            put( "portableCraftingTable.Slot Points.base", 5.0D );
            put( "railgun.Railgun Energy Cost.Voltage.multiplier", 25000.0D );
            put( "railgun.Railgun Energy Cost.base", 5000.0D );
            put( "railgun.Railgun Heat Emission.Voltage.multiplier", 10.0D );
            put( "railgun.Railgun Heat Emission.base", 2.0D );
            put( "railgun.Railgun Total Impulse.Voltage.multiplier", 2500.0D );
            put( "railgun.Railgun Total Impulse.base", 500.0D );
            put( "railgun.Slot Points.base", 5.0D );
            put( "refinedStorageWirelessGrid.Slot Points.base", 5.0D );
            put( "scoop.Scoop Energy Consumption.base", 20000.0D );
            put( "scoop.Scoop Harvest Speed.base", 5.0D );
            put( "scoop.Slot Points.base", 5.0D );
            put( "shears.Shearing Energy Consumption.base", 1000.0D );
            put( "shears.Shearing Harvest Speed.base", 8.0D );
            put( "shears.Slot Points.base", 5.0D );
            put( "shockAbsorber.Distance Reduction.Power.multiplier", 10.0D );
            put( "shockAbsorber.Distance Reduction.base", 0.0D );
            put( "shockAbsorber.Impact Energy Consumption.Power.multiplier", 100.0D );
            put( "shockAbsorber.Impact Energy Consumption.base", 0.0D );
            put( "shockAbsorber.Slot Points.base", 1.0D );
            put( "shovel.Shovel Energy Consumption.Overclock.multiplier", 9500.0D );
            put( "shovel.Shovel Energy Consumption.base", 500.0D );
            put( "shovel.Shovel Harvest Speed.Overclock.multiplier", 22.0D );
            put( "shovel.Shovel Harvest Speed.base", 8.0D );
            put( "shovel.Slot Points.base", 5.0D );
            put( "silk_touch.Slot Points.base", 5.0D );
            put( "solarGenerator.Daytime Energy Generation.base", 15000.0D );
            put( "solarGenerator.Nighttime Energy Generation.base", 1500.0D );
            put( "solarGenerator.Slot Points.base", 5.0D );
            put( "sprintAssist.Slot Points.base", 4.0D );
            put( "sprintAssist.Sprint Energy Consumption.Compensation.multiplier", 20.0D );
            put( "sprintAssist.Sprint Energy Consumption.Sprint Assist.multiplier", 100.0D );
            put( "sprintAssist.Sprint Energy Consumption.base", 0.0D );
            put( "sprintAssist.Sprint Exhaustion Compensation.Compensation.multiplier", 1.0D );
            put( "sprintAssist.Sprint Exhaustion Compensation.base", 0.0D );
            put( "sprintAssist.Sprint Speed Multiplier.Sprint Assist.multiplier", 2.49D );
            put( "sprintAssist.Sprint Speed Multiplier.base", 0.01D );
            put( "sprintAssist.Walking Energy Consumption.Walking Assist.multiplier", 100.0D );
            put( "sprintAssist.Walking Energy Consumption.base", 0.0D );
            put( "sprintAssist.Walking Speed Multiplier.Walking Assist.multiplier", 1.99D );
            put( "sprintAssist.Walking Speed Multiplier.base", 0.01D );
            put( "swimAssist.Slot Points.base", 1.0D );
            put( "swimAssist.Swim Boost Energy Consumption.Thrust.multiplier", 1000.0D );
            put( "swimAssist.Underwater Movement Boost.Thrust.multiplier", 1.0D );
            put( "thermalGenerator.Energy Generation.Energy Generated.multiplier", 250.0D );
            put( "thermalGenerator.Energy Generation.base", 250.0D );
            put( "thermalGenerator.Slot Points.base", 1.0D );
            put( "treetap.Energy Consumption.base", 1000.0D );
            put( "treetap.Slot Points.base", 5.0D );
            put( "ultimateBattery.Maximum Energy.Battery Size.multiplier", 4.25E7D );
            put( "ultimateBattery.Maximum Energy.base", 1.25E7D );
            put( "ultimateBattery.Slot Points.base", 1.0D );
            put( "waterElectrolyzer.Jolt Energy.base", 10000.0D );
            put( "waterElectrolyzer.Slot Points.base", 5.0D );
        }};


        @Config.LangKey(MPSConfigConstants.CONFIG_MODULE_PROPERTY_INTEGERS)
        @Config.Comment("Value of specified property")
        public Map<String, Integer> propertyInteger = new HashMap<String, Integer>() {{

        }};

    }

    //TODO: eleiminate; This whole thing is just temporary, I think :P
    public static Energy energy = new Energy();
    public static class Energy {
        // 1 RF = 0.1 MJ
        @Config.LangKey("mekRatio")
        @Config.Comment("Energy per Mekanism MJ")
        public double mekRatio = 0.1D;

        // 1 RF = 0.25 EU
        @Config.LangKey("ic2Ratio")
        @Config.Comment("Energy per IC2 EU")
        public double ic2Ratio = 0.25D;

        // 1 RF = 1 RS
        @Config.LangKey("refinedStorageRatio")
        @Config.Comment("Energy per RS")
        public double refinedStorageRatio = 1D;

        // 1 rf = 0.5 AE
        @Config.LangKey("ae2Ratio")
        @Config.Comment("Energy per AE")
        public double ae2Ratio = 0.5D;

        // (100KJ or 1M-RF)
        @Config.LangKey(CONFIG_TIER_1_ENERGY_LVL)
        @Config.Comment("Maximum amount of RF energy for Tier 1.")
        @Config.RangeInt(min = 0)
        public static int maxTier1 = (int) (1 * Math.pow(10, 6));

        // advanced capacitor (500KJ or 5M-RF)
        @Config.LangKey(CONFIG_TIER_2_ENERGY_LVL)
        @Config.Comment("Maximum amount of RF energy for Tier 2.")
        @Config.RangeInt(min = 0)
        public static int maxTier2 = (int) (5 * Math.pow(10, 6));

        // elite capacitor (5MJ or 50M-RF)
        @Config.LangKey(CONFIG_TIER_3_ENERGY_LVL)
        @Config.Comment("Maximum amount of RF energy for Tier 3.")
        @Config.RangeInt(min = 0)
        public static int maxTier3 = (int) (5 * Math.pow(10, 7));

        // ultimate capacitor (10MJ or 100M-RF)
        @Config.LangKey(CONFIG_TIER_4_ENERGY_LVL)
        @Config.Comment("Maximum amount of RF energy for Tier 4.")
        @Config.RangeInt(min = 0)
        public static int maxTier4 = (int) (1 * Math.pow(10, 8));
    }
}
