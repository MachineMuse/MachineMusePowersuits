package net.machinemuse.powersuits.common.config;

import net.machinemuse.powersuits.api.constants.MPSConfigConstants;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Configuration;

import java.util.HashMap;
import java.util.Map;

import static net.machinemuse.powersuits.api.constants.MPSModConstants.MODID;

@Config(modid = MODID, name = MPSConfigConstants.CONFIG_FILE + "x" )
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

        //TODO: eleiminate
        @Config.LangKey(MPSConfigConstants.CONFIG_GENERAL_BASE_MAX_HEAT)
        @Config.Comment("ItemModuleBase Heat Cap")
        public double baseMaxHeat = 50.0;

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
            put(MPSModuleConstants.MODULE_LEATHER_PLATING, true);
            put(MPSModuleConstants.MODULE_IRON_PLATING, true);
            put(MPSModuleConstants.MODULE_DIAMOND_PLATING, true);
            put(MPSModuleConstants.MODULE_ENERGY_SHIELD, true);

            // Cosmetic -------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_CITIZEN_JOE_STYLE, true);
            put(MPSModuleConstants.MODULE_GLOW, true);
            put(MPSModuleConstants.MODULE_HIGH_POLY_ARMOR, true);
            put(MPSModuleConstants.MODULE_TINT, true);
            put(MPSModuleConstants.MODULE_TRANSPARENT_ARMOR, true);

            // Energy ---------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_BATTERY_ADVANCED, true);
            put(MPSModuleConstants.MODULE_ADVANCED_SOLAR_GENERATOR, true);
            put(MPSModuleConstants.MODULE_BATTERY_BASIC, true);
            put(MPSModuleConstants.MODULE_COAL_GEN, true);
            put(MPSModuleConstants.MODULE_BATTERY_ELITE, true);
            put(MPSModuleConstants.MODULE_KINETIC_GENERATOR, true);
            put(MPSModuleConstants.MODULE_SOLAR_GENERATOR, true);
            put(MPSModuleConstants.MODULE_THERMAL_GENERATOR, true);
            put(MPSModuleConstants.MODULE_BATTERY_ULTIMATE, true);

            // Environmental --------------------------------------------------------------
            put(MPSModuleConstants.AIRTIGHT_SEAL_MODULE, true);
            put(MPSModuleConstants.MODULE_APIARIST_ARMOR, true);
            put(MPSModuleConstants.MODULE_AUTO_FEEDER, true);
            put(MPSModuleConstants.MODULE_COOLING_SYSTEM, true);
            put(MPSModuleConstants.MODULE_HAZMAT, true);
            put(MPSModuleConstants.MODULE_HEAT_SINK, true);
            put(MPSModuleConstants.MODULE_MECH_ASSISTANCE, true);
            put(MPSModuleConstants.MODULE_MOB_REPULSOR, true);
            put(MPSModuleConstants.MODULE_NITROGEN_COOLING_SYSTEM, true);
            put(MPSModuleConstants.MODULE_WATER_ELECTROLYZER, true);
            put(MPSModuleConstants.MODULE_WATER_TANK, true);

            // Movement -------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_BLINK_DRIVE, true);
            put(MPSModuleConstants.MODULE_CLIMB_ASSIST, true);
            put(MPSModuleConstants.MODULE_FLIGHT_CONTROL, true);
            put(MPSModuleConstants.MODULE_GLIDER, true);
            put(MPSModuleConstants.MODULE_JETBOOTS, true);
            put(MPSModuleConstants.MODULE_JETPACK, true);
            put(MPSModuleConstants.MODULE_JUMP_ASSIST, true);
            put(MPSModuleConstants.MODULE_PARACHUTE, true);
            put(MPSModuleConstants.MODULE_SHOCK_ABSORBER, true);
            put(MPSModuleConstants.MODULE_SPRINT_ASSIST, true);
            put(MPSModuleConstants.MODULE_SWIM_BOOST, true);

            // Special --------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_CLOCK, true);
            put(MPSModuleConstants.MODULE_COMPASS, true);
            put(MPSModuleConstants.MODULE_ACTIVE_CAMOUFLAGE, true);
            put(MPSModuleConstants.MODULE_MAGNET, true);

            // Vision ---------------------------------------------------------------------
            put(MPSModuleConstants.BINOCULARS_MODULE, true);
            put(MPSModuleConstants.MODULE_NIGHT_VISION, true); // done via mod compat
            put(MPSModuleConstants.MODULE_THAUM_GOGGLES, true);

            // Tools --------------------------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_AOE_PICK_UPGRADE, true);
            put(MPSModuleConstants.MODULE_APPENG_EC_WIRELESS_FLUID, true);
            put(MPSModuleConstants.MODULE_APPENG_WIRELESS, true);
            put(MPSModuleConstants.MODULE_AQUA_AFFINITY, true);
            put(MPSModuleConstants.MODULE_AXE, true);
            put(MPSModuleConstants.MODULE_CHISEL, true);
            put(MPSModuleConstants.MODULE_DIAMOND_PICK_UPGRADE, true);
            put(MPSModuleConstants.MODULE_DIMENSIONAL_RIFT, true);
            put(MPSModuleConstants.MODULE_FIELD_TINKER, true);
            put(MPSModuleConstants.MODULE_FLINT_AND_STEEL, true);
            put(MPSModuleConstants.MODULE_GRAFTER, true);
            put(MPSModuleConstants.MODULE_HOE, true);
            put(MPSModuleConstants.MODULE_LEAF_BLOWER, true);
            put(MPSModuleConstants.MODULE_LUX_CAPACITOR, true);
            put(MPSModuleConstants.MODULE_FIELD_TELEPORTER, true);
            put(MPSModuleConstants.MODULE_OC_TERMINAL, true);
            put(MPSModuleConstants.MODULE_OMNIPROBE, true);
            put(MPSModuleConstants.MODULE_OMNI_WRENCH, true);
            put(MPSModuleConstants.MODULE_ORE_SCANNER, true);
            put(MPSModuleConstants.MODULE_CM_PSD, true);
            put(MPSModuleConstants.MODULE_PICKAXE, true);
            put(MPSModuleConstants.MODULE_PORTABLE_CRAFTING, true);
            put(MPSModuleConstants.MODULE_REF_STOR_WIRELESS, true);
            put(MPSModuleConstants.MODULE_SCOOP, true);
            put(MPSModuleConstants.MODULE_SHEARS, true);
            put(MPSModuleConstants.MODULE_SHOVEL, true);
            put(MPSModuleConstants.MODULE_TREETAP, true);

            // Weapons ------------------------------------------------------------------------------------
            put(MPSModuleConstants.MODULE_BLADE_LAUNCHER, true);
            put(MPSModuleConstants.MODULE_LIGHTNING, true);
            put(MPSModuleConstants.MODULE_MELEE_ASSIST, true);
            put(MPSModuleConstants.MODULE_PLASMA_CANNON, true);
            put(MPSModuleConstants.MODULE_RAILGUN, true);
        }};

        @Config.LangKey(MPSConfigConstants.CONFIG_MODULE_PROPERTY_DOUBLES)
        @Config.Comment("Value of specified property")
        public Map<String, Double> propertyDouble = new HashMap<String, Double>() {{
            put( "Advanced Battery.IC2 Tier.IC2 Tier.multiplier", 2.0D );
            put( "Advanced Battery.IC2 Tier.base", 1.0D );
            put( "Advanced Battery.Maximum Energy.Battery Size.multiplier", 400000.0D );
            put( "Advanced Battery.Maximum Energy.base", 100000.0D );
            put( "Advanced Battery.Weight.Battery Size.multiplier", 8000.0D );
            put( "Advanced Battery.Weight.base", 2000.0D );
            put( "Advanced Solar Generator.Daytime Energy Generation.base", 4500.0D );
            put( "Advanced Solar Generator.Daytime Heat Generation.base", 15.0D );
            put( "Advanced Solar Generator.Nighttime Energy Generation.base", 150.0D );
            put( "Advanced Solar Generator.Nighttime Heat Generation.base", 5.0D );
            put( "Advanced Solar Generator.Weight.base", 300.0D );
            put( "Apiarist Armor.Apiarist Armor Energy Consumption.base", 10.0D );
            put( "Aqua Affinity.Underwater Energy Consumption.Power.multiplier", 100.0D );
            put( "Aqua Affinity.Underwater Energy Consumption.base", 0.0D );
            put( "Aqua Affinity.Underwater Harvest Speed.Power.multiplier", 0.8D );
            put( "Aqua Affinity.Underwater Harvest Speed.base", 0.2D );
            put( "Auto-Feeder.Auto-Feeder Efficiency.Efficiency.multiplier", 50.0D );
            put( "Auto-Feeder.Auto-Feeder Efficiency.base", 50.0D );
            put( "Auto-Feeder.Eating Energy Consumption.Efficiency.multiplier", 100.0D );
            put( "Auto-Feeder.Eating Energy Consumption.base", 100.0D );
            put( "Axe.Axe Energy Consumption.Overclock.multiplier", 950.0D );
            put( "Axe.Axe Energy Consumption.base", 50.0D );
            put( "Axe.Axe Harvest Speed.Overclock.multiplier", 22.0D );
            put( "Axe.Axe Harvest Speed.base", 8.0D );
            put( "Basic Battery.IC2 Tier.IC2 Tier.multiplier", 2.0D );
            put( "Basic Battery.IC2 Tier.base", 1.0D );
            put( "Basic Battery.Maximum Energy.Battery Size.multiplier", 80000.0D );
            put( "Basic Battery.Maximum Energy.base", 20000.0D );
            put( "Basic Battery.Weight.Battery Size.multiplier", 8000.0D );
            put( "Basic Battery.Weight.base", 2000.0D );
            put( "Binoculars.Field of View.FOV multiplier.multiplier", 9.5D );
            put( "Binoculars.Field of View.base", 0.5D );
            put( "Blade Launcher.Spinning Blade Damage.base", 6.0D );
            put( "Blade Launcher.Spinning Blade Energy Consumption.base", 500.0D );
            put( "Blink Drive.Blink Drive Energy Consuption.Range.multiplier", 3000.0D );
            put( "Blink Drive.Blink Drive Energy Consuption.base", 1000.0D );
            put( "Blink Drive.Blink Drive Range.Range.multiplier", 59.0D );
            put( "Blink Drive.Blink Drive Range.base", 5.0D );
            put( "Cooling System.Cooling Bonus.Power.multiplier", 4.0D );
            put( "Cooling System.Cooling System Energy Consumption.Power.multiplier", 10.0D );
            put( "Custom Colour Module.Blue Tint.Blue Intensity.multiplier", 1.0D );
            put( "Custom Colour Module.Green Tint.Green Intensity.multiplier", 1.0D );
            put( "Custom Colour Module.Red Tint.Red Intensity.multiplier", 1.0D );
            put( "Dimensional Tear Generator.Energy Consumption.base", 20000.0D );
            put( "Dimensional Tear Generator.Heat Generation.base", 55.0D );
            put( "Elite Battery.IC2 Tier.IC2 Tier.multiplier", 2.0D );
            put( "Elite Battery.IC2 Tier.base", 1.0D );
            put( "Elite Battery.Maximum Energy.Battery Size.multiplier", 4250000.0D );
            put( "Elite Battery.Maximum Energy.base", 750000.0D );
            put( "Elite Battery.Weight.Battery Size.multiplier", 8000.0D );
            put( "Elite Battery.Weight.base", 2000.0D );
            put( "Flight Control.Y-look ratio.Verticality.multiplier", 1.0D );
            put( "Flint and Steel.Ignition Energy Consumption.base", 1000.0D );
            put( "Glow Module.Blue Glow.Blue Glow.multiplier", 1.0D );
            put( "Glow Module.Green Glow.Green Glow.multiplier", 1.0D );
            put( "Glow Module.Red Glow.Red Glow.multiplier", 1.0D );
            put( "Grafter.Grafter Energy Consumption.base", 1000.0D );
            put( "Grafter.Grafter Heat Generation.base", 20.0D );
            put( "Heat Sink.Maximum Heat.Thickness.multiplier", 150.0D );
            put( "Heat Sink.Weight.Thickness.multiplier", 5000.0D );
            put( "Jet Boots.Jetboots Energy Consumption.Thrust.multiplier", 75.0D );
            put( "Jet Boots.Jetboots Energy Consumption.base", 0.0D );
            put( "Jet Boots.Jetboots Thrust.Thrust.multiplier", 0.08D );
            put( "Jet Boots.Jetboots Thrust.base", 0.0D );
            put( "Jetpack.Jetpack Energy Consumption.Thrust.multiplier", 150.0D );
            put( "Jetpack.Jetpack Energy Consumption.base", 0.0D );
            put( "Jetpack.Jetpack Thrust.Thrust.multiplier", 0.16D );
            put( "Jetpack.Jetpack Thrust.base", 0.0D );
            put( "Jump Assist.Jump Boost.Power.multiplier", 4.0D );
            put( "Jump Assist.Jump Boost.base", 1.0D );
            put( "Jump Assist.Jump Energy Consumption.Compensation.multiplier", 5.0D );
            put( "Jump Assist.Jump Energy Consumption.Power.multiplier", 25.0D );
            put( "Jump Assist.Jump Energy Consumption.base", 0.0D );
            put( "Jump Assist.Jump Exhaustion Compensation.Compensation.multiplier", 1.0D );
            put( "Jump Assist.Jump Exhaustion Compensation.base", 0.0D );
            put( "Kinetic Generator.Energy Per 5 Blocks.Energy Generated.multiplier", 600.0D );
            put( "Kinetic Generator.Energy Per 5 Blocks.base", 200.0D );
            put( "Kinetic Generator.Heat Generation.base", 5.0D );
            put( "Kinetic Generator.Weight.Energy Generated.multiplier", 3000.0D );
            put( "Kinetic Generator.Weight.base", 1000.0D );
            put( "Leaf Blower.Energy Consumption.base", 100.0D );
            put( "Leaf Blower.Radius.base", 1.0D );
            put( "Lightning Summoner.Energy Consumption.base", 490000.0D );
            put( "Lightning Summoner.Heat Emission.base", 100.0D );
            put( "Liquid Nitrogen Cooling System.Cooling Bonus.Power.multiplier", 7.0D );
            put( "Liquid Nitrogen Cooling System.Energy Consumption.Power.multiplier", 16.0D );
            put( "Lux Capacitor.Lux Capacitor Blue Hue.Blue.multiplier", 1.0D );
            put( "Lux Capacitor.Lux Capacitor Energy Consumption.base", 100.0D );
            put( "Lux Capacitor.Lux Capacitor Green Hue.Green.multiplier", 1.0D );
            put( "Lux Capacitor.Lux Capacitor Red Hue.Red.multiplier", 1.0D );
            put( "Magnet.Energy Consumption.base", 200.0D );
            put( "Magnet.Magnet Radius.base", 5.0D );
            put( "Magnet.Weight.base", 1000.0D );
            put( "Mechanical Assistance.Power Usage.Robotic Assistance.multiplier", 500.0D );
            put( "Mechanical Assistance.Weight.Robotic Assistance.multiplier", -10000.0D );
            put( "Melee Assist.Melee Damage.Impact.multiplier", 8.0D );
            put( "Melee Assist.Melee Damage.base", 2.0D );
            put( "Melee Assist.Melee Knockback.Carry-through.multiplier", 1.0D );
            put( "Melee Assist.Punch Energy Consumption.Carry-through.multiplier", 20.0D );
            put( "Melee Assist.Punch Energy Consumption.Impact.multiplier", 100.0D );
            put( "Melee Assist.Punch Energy Consumption.base", 10.0D );
            put( "Mob Repulsor.Repulsor Energy Consumption.base", 250.0D );
            put( "Mob Repulsor.Weight.base", 2000.0D );
            put( "Pickaxe.Pickaxe Energy Consumption.Overclock.multiplier", 950.0D );
            put( "Pickaxe.Pickaxe Energy Consumption.base", 50.0D );
            put( "Pickaxe.Pickaxe Harvest Speed.Overclock.multiplier", 22.0D );
            put( "Pickaxe.Pickaxe Harvest Speed.base", 8.0D );
            put( "Plasma Cannon.Plasma Damage At Full Charge.Amperage.multiplier", 38.0D );
            put( "Plasma Cannon.Plasma Damage At Full Charge.base", 2.0D );
            put( "Plasma Cannon.Plasma Energy Per Tick.Amperage.multiplier", 150.0D );
            put( "Plasma Cannon.Plasma Energy Per Tick.Voltage.multiplier", 50.0D );
            put( "Plasma Cannon.Plasma Energy Per Tick.base", 10.0D );
            put( "Plasma Cannon.Plasma Explosiveness.Voltage.multiplier", 0.5D );
            put( "Radiation Shielding.Weight.base", 0.5D );
            put( "Railgun.Railgun Energy Cost.Voltage.multiplier", 2500.0D );
            put( "Railgun.Railgun Energy Cost.base", 500.0D );
            put( "Railgun.Railgun Heat Emission.Voltage.multiplier", 10.0D );
            put( "Railgun.Railgun Heat Emission.base", 2.0D );
            put( "Railgun.Railgun Total Impulse.Voltage.multiplier", 2500.0D );
            put( "Railgun.Railgun Total Impulse.base", 500.0D );
            put( "Rototiller.Hoe Energy Consumption.Search Radius.multiplier", 950.0D );
            put( "Rototiller.Hoe Energy Consumption.base", 50.0D );
            put( "Rototiller.Hoe Search Radius.Search Radius.multiplier", 8.0D );
            put( "Scoop.Scoop Energy Consumption.base", 2000.0D );
            put( "Scoop.Scoop Harvest Speed.base", 5.0D );
            put( "Shears.Shearing Energy Consumption.Overclock.multiplier", 950.0D );
            put( "Shears.Shearing Energy Consumption.base", 50.0D );
            put( "Shears.Shearing Harvest Speed.Overclock.multiplier", 22.0D );
            put( "Shears.Shearing Harvest Speed.base", 8.0D );
            put( "Shock Absorber.Distance Reduction.Power.multiplier", 1.0D );
            put( "Shock Absorber.Distance Reduction.base", 0.0D );
            put( "Shock Absorber.Impact Energy consumption.Power.multiplier", 10.0D );
            put( "Shock Absorber.Impact Energy consumption.base", 0.0D );
            put( "Shovel.Shovel Energy Consumption.Overclock.multiplier", 950.0D );
            put( "Shovel.Shovel Energy Consumption.base", 50.0D );
            put( "Shovel.Shovel Harvest Speed.Overclock.multiplier", 22.0D );
            put( "Shovel.Shovel Harvest Speed.base", 8.0D );
            put( "Solar Generator.Daytime Energy Generation.base", 1500.0D );
            put( "Solar Generator.Nighttime Energy Generation.base", 150.0D );
            put( "Sprint Assist.Sprint Energy Consumption.Compensation.multiplier", 2.0D );
            put( "Sprint Assist.Sprint Energy Consumption.Power.multiplier", 10.0D );
            put( "Sprint Assist.Sprint Energy Consumption.base", 0.0D );
            put( "Sprint Assist.Sprint Exhaustion Compensation.Compensation.multiplier", 1.0D );
            put( "Sprint Assist.Sprint Exhaustion Compensation.base", 0.0D );
            put( "Sprint Assist.Sprint Speed Multiplier.Power.multiplier", 2.0D );
            put( "Sprint Assist.Sprint Speed Multiplier.base", 1.0D );
            put( "Sprint Assist.Walking Energy Consumption.Walking Assist.multiplier", 10.0D );
            put( "Sprint Assist.Walking Energy Consumption.base", 0.0D );
            put( "Sprint Assist.Walking Speed Multiplier.Walking Assist.multiplier", 1.0D );
            put( "Sprint Assist.Walking Speed Multiplier.base", 1.0D );
            put( "Swim Boost.Swim Boost Energy Consumption.Thrust.multiplier", 100.0D );
            put( "Swim Boost.Underwater Movement Boost.Thrust.multiplier", 1.0D );
            put( "Thermal Generator.Energy Generation.Energy Generated.multiplier", 25.0D );
            put( "Thermal Generator.Energy Generation.base", 25.0D );
            put( "Thermal Generator.Weight.Energy Generated.multiplier", 1000.0D );
            put( "Thermal Generator.Weight.base", 1000.0D );
            put( "Treetap.Energy Consumption.base", 100.0D );
            put( "Ultimate Battery.IC2 Tier.IC2 Tier.multiplier", 3.0D );
            put( "Ultimate Battery.IC2 Tier.base", 1.0D );
            put( "Ultimate Battery.Maximum Energy.Battery Size.multiplier", 4250000.0D );
            put( "Ultimate Battery.Maximum Energy.base", 1250000.0D );
            put( "Ultimate Battery.Weight.Battery Size.multiplier", 6000.0D );
            put( "Ultimate Battery.Weight.base", 1500.0D );
            put( "Water Electrolyzer.Jolt Energy.base", 1000.0D );
            put( "Water Tank.Heat Activation Percent.Activation Percent.multiplier", 0.5D );
            put( "Water Tank.Heat Activation Percent.base", 0.5D );
            put( "Water Tank.Tank Size.Tank Size.multiplier", 800.0D );
            put( "Water Tank.Tank Size.base", 200.0D );
            put( "Water Tank.Weight.Tank Size.multiplier", 4000.0D );
            put( "Water Tank.Weight.base", 1000.0D );
            put( "powersuits.module.diamondPlating.Armor (Physical).Plating Thickness.multiplier", 6.0D );
            put( "powersuits.module.diamondPlating.Weight.Plating Thickness.multiplier", 6000.0D );
            put( "powersuits.module.energyShield.Armor (Energy).powersuits.module.fieldStrength.multiplier", 6.0D );
            put( "powersuits.module.energyShield.Energy Per Damage.powersuits.module.fieldStrength.multiplier", 500.0D );
            put( "powersuits.module.ironPlating.Armor (Physical).Plating Thickness.multiplier", 3.0D );
            put( "powersuits.module.ironPlating.Weight.Plating Thickness.multiplier", 10000.0D );
            }};
    }

    //TODO: eleiminate; This whole thing is just temporary, I think :P
    public static Energy energy = new Energy();
    public static class Energy {
        @Config.LangKey("mekRatio")
        @Config.Comment("Energy per Mekanism MJ")
        public double mekRatio = 1D;

        @Config.LangKey("ic2Ratio")
        @Config.Comment("Energy per IC2 EU")
        public double ic2Ratio = 0.4D;

        @Config.LangKey("rfRatio")
        @Config.Comment("Energy per RF")
        public double rfRatio = 0.1D;

        @Config.LangKey("refinedStorageRatio")
        @Config.Comment("Energy per RS")
        public double refinedStorageRatio = 0.1D;

        @Config.LangKey("ae2Ratio")
        @Config.Comment("Energy per AE")
        public double ae2Ratio = 0.2D;
    }



}
