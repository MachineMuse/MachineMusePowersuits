package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.block.fluid.BlockLiquidNitrogen;
import net.machinemuse.powersuits.block.itemblock.ItemBlockLuxCapacitor;
import net.machinemuse.powersuits.block.itemblock.ItemBlockTinkerTable;
import net.machinemuse.powersuits.fluid.FluidLiquidNitrogen;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorBoots;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorChestplate;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorHelmet;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorLeggings;
import net.machinemuse.powersuits.item.module.armor.DiamondPlatingModule;
import net.machinemuse.powersuits.item.module.armor.EnergyShieldModule;
import net.machinemuse.powersuits.item.module.armor.IronPlatingModule;
import net.machinemuse.powersuits.item.module.armor.LeatherPlatingModule;
import net.machinemuse.powersuits.item.module.cosmetic.*;
import net.machinemuse.powersuits.item.module.debug.DebugModule;
import net.machinemuse.powersuits.item.module.energy.*;
import net.machinemuse.powersuits.item.module.environmental.*;
import net.machinemuse.powersuits.item.module.movement.*;
import net.machinemuse.powersuits.item.module.special.ClockModule;
import net.machinemuse.powersuits.item.module.special.CompassModule;
import net.machinemuse.powersuits.item.module.special.InvisibilityModule;
import net.machinemuse.powersuits.item.module.special.MagnetModule;
import net.machinemuse.powersuits.item.module.tool.*;
import net.machinemuse.powersuits.item.module.vision.BinocularsModule;
import net.machinemuse.powersuits.item.module.vision.NightVisionModule;
import net.machinemuse.powersuits.item.module.vision.ThaumGogglesModule;
import net.machinemuse.powersuits.item.module.weapon.*;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.minecraft.block.Block;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

import static net.machinemuse.powersuits.api.constants.MPSModConstants.MODID;

/**
 * Created by Claire Semple on 9/9/2014.
 *
 * Ported to Java by lehjr on 10/22/16.
 */
@Mod.EventBusSubscriber(modid = MODID)
public class MPSItems {
    private static MPSItems INSTANCE;

    public static MPSItems getInstance() {
        if (INSTANCE == null) synchronized (MPSItems.class) {
            if (INSTANCE == null) INSTANCE = new MPSItems();
        }
        return INSTANCE;
    }
    private MPSItems() {}

    // Armor --------------------------------------------------------------------------------------
    public static Item powerArmorHead = itemRegister(new ItemPowerArmorHelmet(), "powerarmor_head", "powerArmorHelmet");
    public static Item powerArmorTorso = itemRegister(new ItemPowerArmorChestplate(), "powerarmor_torso", "powerArmorChestplate");
    public static Item powerArmorLegs = itemRegister(new ItemPowerArmorLeggings(), "powerarmor_legs", "powerArmorLeggings");
    public static Item powerArmorFeet = itemRegister(new ItemPowerArmorBoots(), "powerarmor_feet", "powerArmorBoots");
    // HandHeld -----------------------------------------------------------------------------------
    public static Item powerFist = itemRegister(new ItemPowerFist(), "power_fist", "powerfist");

    // Components ---------------------------------------------------------------------------------
    public static Item components = ItemComponent.getInstance();

    /** Modules ----------------------------------------------------------------------------------- */
    // Debug --------------------------------------------------------------------------------------
    public static Item module_debug = new DebugModule("debug", MPSModuleConstants.MODULE_DEBUG);
    // Armor
    public static Item module_leather_plating = new LeatherPlatingModule("leather_plating", MPSModuleConstants.MODULE_LEATHER_PLATING);
    public static Item module_iron_plating = new IronPlatingModule("iron_plating", MPSModuleConstants.MODULE_IRON_PLATING);
    public static Item module_diamond_plating = new DiamondPlatingModule("diamond_plating", MPSModuleConstants.MODULE_DIAMOND_PLATING);
    public static Item module_energy_shield = new EnergyShieldModule("energy_shield", MPSModuleConstants.MODULE_ENERGY_SHIELD);

    // Cosmetic
    public static Item module_citizen_joe = new CitizenJoeStyle("citizen_joe", MPSModuleConstants.MODULE_CITIZEN_JOE_STYLE);
    public static Item module_cosmetic_glow = new CosmeticGlowModule("cosmetic_glow", MPSModuleConstants.MODULE_GLOW);
    public static Item module_3d_armor = new HighPolyArmor("3d_armor", MPSModuleConstants.MODULE_HIGH_POLY_ARMOR);
    public static Item module_tint = new TintModule("tint", MPSModuleConstants.MODULE_TINT);
    public static Item module_transparent_armor = new TransparentArmorModule("transparent_armor", MPSModuleConstants.MODULE_TRANSPARENT_ARMOR);

    // Energy
    public static Item module_basic_battery = new BasicBatteryModule("basic_battery", MPSModuleConstants.MODULE_BATTERY_BASIC);
    public static Item module_advanced_battery = new AdvancedBatteryModule("advanced_battery", MPSModuleConstants.MODULE_BATTERY_ADVANCED);
    public static Item module_elite_battery = new EliteBatteryModule("elite_battery", MPSModuleConstants.MODULE_BATTERY_ELITE);
    public static Item module_ultimate_battery = new UltimateBatteryModule("ultimate_battery", MPSModuleConstants.MODULE_BATTERY_ULTIMATE);
    public static Item module_solar_generator = new SolarGeneratorModule("solar_generator", MPSModuleConstants.MODULE_SOLAR_GENERATOR);
    public static Item module_advanced_generator = new AdvancedSolarGenerator("advanced_generator", MPSModuleConstants.MODULE_ADVANCED_SOLAR_GENERATOR);
    public static Item module_coal_generator = new CoalGenerator("coal_generator", MPSModuleConstants.MODULE_COAL_GEN);
    public static Item module_kinetic_generator = new KineticGeneratorModule("kinetic_generator", MPSModuleConstants.MODULE_KINETIC_GENERATOR);
    public static Item module_stirling_generator = new ThermalGeneratorModule("stirling_generator", MPSModuleConstants.MODULE_THERMAL_GENERATOR);

    // Environmental
    public static Item module_autofeeder = new AutoFeederModule("autofeeder", MPSModuleConstants.MODULE_AUTO_FEEDER);
    public static Item module_airtight_seal = new AirtightSealModule("airtight_seal", MPSModuleConstants.AIRTIGHT_SEAL_MODULE);
    public static Item module_apiarist_armor = new ApiaristArmorModule("apiarist_armor", MPSModuleConstants.MODULE_APIARIST_ARMOR);
    public static Item module_cooling_system = new CoolingSystemModule("cooling_system", MPSModuleConstants.MODULE_COOLING_SYSTEM);
    public static Item module_hazmat = new HazmatModule("hazmat", MPSModuleConstants.MODULE_HAZMAT);
    public static Item module_mechanical_assistance = new MechanicalAssistance("mechanical_assistance", MPSModuleConstants.MODULE_MECH_ASSISTANCE);
    public static Item module_mob_repulsor = new MobRepulsorModule("mob_repulsor", MPSModuleConstants.MODULE_MOB_REPULSOR);
    public static Item module_heat_sink = new HeatSinkModule("heat_sink", MPSModuleConstants.MODULE_HEAT_SINK);
    public static Item module_nitrogen_cooling_system = new NitrogenCoolingSystem("nitrogen_cooling_system", MPSModuleConstants.MODULE_NITROGEN_COOLING_SYSTEM);
    public static Item module_water_electrolyzer = new WaterElectrolyzerModule("water_electrolyzer", MPSModuleConstants.MODULE_WATER_ELECTROLYZER);
    public static Item module_water_tank = new WaterTankModule("water_tank", MPSModuleConstants.MODULE_WATER_TANK);

    // Movement
    public static Item module_blink_drive = new BlinkDriveModule("blink_drive", MPSModuleConstants.MODULE_BLINK_DRIVE);
    public static Item module_climb_assist = new ClimbAssistModule("climb_assist", MPSModuleConstants.MODULE_CLIMB_ASSIST);
    public static Item module_flight_control = new FlightControlModule("flight_control", MPSModuleConstants.MODULE_FLIGHT_CONTROL);
    public static Item module_glider = new GliderModule("glider", MPSModuleConstants.MODULE_GLIDER);
    public static Item module_jet_boots = new JetBootsModule("jet_boots", MPSModuleConstants.MODULE_JETBOOTS);
    public static Item module_jetpack = new JetPackModule("jetpack", MPSModuleConstants.MODULE_JETPACK);
    public static Item module_jump_assist = new JumpAssistModule("jump_assist", MPSModuleConstants.MODULE_JUMP_ASSIST);
    public static Item module_parachute = new ParachuteModule("parachute", MPSModuleConstants.MODULE_PARACHUTE);
    public static Item module_shock_absorber = new ShockAbsorberModule("shock_absorber", MPSModuleConstants.MODULE_SHOCK_ABSORBER);
    public static Item module_sprint_assist = new SprintAssistModule("sprint_assist", MPSModuleConstants.MODULE_SPRINT_ASSIST);
    public static Item module_swim_boost = new SwimAssistModule("swim_boost", MPSModuleConstants.MODULE_SWIM_BOOST);

    // Special
    public static Item module_clock = new ClockModule("clock", MPSModuleConstants.MODULE_CLOCK);
    public static Item module_compass = new CompassModule("compass", MPSModuleConstants.MODULE_COMPASS);
    public static Item module_invisibility = new InvisibilityModule("invisibility", MPSModuleConstants.MODULE_ACTIVE_CAMOUFLAGE);
    public static Item module_magnet = new MagnetModule("magnet", MPSModuleConstants.MODULE_MAGNET);

    // Vision
    public static Item module_binnoculars = new BinocularsModule("binnoculars", MPSModuleConstants.BINOCULARS_MODULE);
    public static Item module_night_vision = new NightVisionModule("night_vision", MPSModuleConstants.MODULE_NIGHT_VISION);
    public static Item module_aurameter = new ThaumGogglesModule("aurameter", MPSModuleConstants.MODULE_THAUM_GOGGLES);

    // Tools
    public static Item module_pickaxe = new PickaxeModule("pickaxe", MPSModuleConstants.MODULE_PICKAXE);
    public static Item module_diamond_pick_upgrade = new DiamondPickUpgradeModule("diamond_pick_upgrade", MPSModuleConstants.MODULE_DIAMOND_PICK_UPGRADE);
    public static Item module_aoe_pick_upgrade = new AOEPickUpgradeModule("aoe_pick_upgrade", MPSModuleConstants.MODULE_AOE_PICK_UPGRADE);
    public static Item module_axe = new AxeModule("axe", MPSModuleConstants.MODULE_AXE);
    public static Item module_shears = new ShearsModule("shears", MPSModuleConstants.MODULE_SHEARS);
    public static Item module_shovel = new ShovelModule("shovel", MPSModuleConstants.MODULE_SHOVEL);
    public static Item module_appeng_ec_wireless_fluid = new AppEngWirelessFluidModule("appeng_ec_wireless_fluid", MPSModuleConstants.MODULE_APPENG_EC_WIRELESS_FLUID);
    public static Item module_appeng_wireless = new AppEngWirelessModule("appeng_wireless", MPSModuleConstants.MODULE_APPENG_WIRELESS);
    public static Item module_aqua_affinity = new AquaAffinityModule("aqua_affinity", MPSModuleConstants.MODULE_AQUA_AFFINITY);
    public static Item module_chisel = new ChiselModule("chisel", MPSModuleConstants.MODULE_CHISEL);
    public static Item module_dim_rift_gen = new DimensionalRiftModule("dim_rift_gen", MPSModuleConstants.MODULE_DIMENSIONAL_RIFT);
    public static Item module_field_tinkerer = new FieldTinkerModule("field_tinkerer", MPSModuleConstants.MODULE_FIELD_TINKER);
    public static Item module_flint_and_steel = new FlintAndSteelModule("flint_and_steel", MPSModuleConstants.MODULE_FLINT_AND_STEEL);
    public static Item module_grafter = new GrafterModule("grafter", MPSModuleConstants.MODULE_GRAFTER);
    public static Item module_hoe = new HoeModule("hoe", MPSModuleConstants.MODULE_HOE);
    public static Item module_leafblower = new LeafBlowerModule("leafblower", MPSModuleConstants.MODULE_LEAF_BLOWER);
    public static Item module_luxcaplauncher = new LuxCapacitor("luxcaplauncher", MPSModuleConstants.MODULE_LUX_CAPACITOR);
    public static Item module_mffsfieldteleporter = new MFFSFieldTeleporterModule("mffsfieldteleporter", MPSModuleConstants.MODULE_FIELD_TELEPORTER);
    //    public static Item module_octerminal = new OCTerminalModule("octerminal", MPSModuleConstants.MODULE_OC_TERMINAL);
    public static Item module_omniprobe = new OmniProbeModule("omniprobe", MPSModuleConstants.MODULE_OMNIPROBE);
    public static Item module_omniwrench = new OmniWrenchModule("omniwrench", MPSModuleConstants.MODULE_OMNI_WRENCH);
    public static Item module_ore_scanner = new OreScannerModule("ore_scanner", MPSModuleConstants.MODULE_ORE_SCANNER);
    public static Item module_cmpsd = new PersonalShrinkingModule("cmpsd", MPSModuleConstants.MODULE_CM_PSD);
    public static Item module_portable_crafting_table = new PortableCraftingTableModule("portable_crafting_table", MPSModuleConstants.MODULE_PORTABLE_CRAFTING);
    public static Item module_refinedstoragewirelessgrid =  new RefinedStorageWirelessModule("refinedstoragewirelessgrid", MPSModuleConstants.MODULE_REF_STOR_WIRELESS);
    public static Item module_scoop = new ScoopModule("scoop", MPSModuleConstants.MODULE_SCOOP);
    public static Item module_tree_tap = new TreetapModule("tree_tap", MPSModuleConstants.MODULE_TREETAP);

    // Weapons
    public static Item module_blade_launcher = new BladeLauncherModule("blade_launcher", MPSModuleConstants.MODULE_BLADE_LAUNCHER);
    public static Item module_lightning = new LightningModule("lightning", MPSModuleConstants.MODULE_LIGHTNING);
    public static Item module_melee_assist = new MeleeAssistModule("melee_assist", MPSModuleConstants.MODULE_MELEE_ASSIST);
    public static Item module_plasma_cannon = new PlasmaCannonModule("plasma_cannon", MPSModuleConstants.MODULE_PLASMA_CANNON);
    public static Item module_railgun = new RailgunModule("railgun", MPSModuleConstants.MODULE_RAILGUN);

    @SubscribeEvent
    public static void regigisterItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                powerArmorHead,
                powerArmorTorso,
                powerArmorLegs,
                powerArmorFeet,
                powerFist,
                components,
                // Modules ------------------------------------------

                // Debug
                module_debug,

                // Armor
                module_leather_plating,
                module_iron_plating,
                module_diamond_plating,

                // Cosmetic
                module_citizen_joe,
                module_cosmetic_glow,
                module_3d_armor,
                module_tint,
                module_transparent_armor,

                // Energy
                module_basic_battery,
                module_advanced_battery,
                module_elite_battery,
                module_ultimate_battery,
                module_solar_generator,
                module_advanced_generator,
                module_coal_generator,
                module_kinetic_generator,
                module_stirling_generator,

                // Environmental
                module_autofeeder,
                module_airtight_seal,
                module_apiarist_armor,
                module_cooling_system,
                module_hazmat,
                module_mechanical_assistance,
                module_mob_repulsor,
                module_heat_sink,
                module_nitrogen_cooling_system,
                module_water_electrolyzer,
                module_water_tank,

                // Movement
                module_blink_drive,
                module_climb_assist,
                module_flight_control,
                module_glider,
                module_jet_boots,
                module_jetpack,
                module_jump_assist,
                module_parachute,
                module_shock_absorber,
                module_sprint_assist,
                module_swim_boost,

                // Special
                module_clock,
                module_compass,
                module_invisibility,
                module_magnet,

                // Vision
                module_binnoculars,
                module_night_vision,
                module_aurameter,

                // Tools
                module_pickaxe,
                module_diamond_pick_upgrade,
                module_aoe_pick_upgrade,
                module_axe,
                module_shears,
                module_shovel,
                module_appeng_ec_wireless_fluid,
                module_appeng_wireless,
                module_aqua_affinity,
                module_chisel,
                module_dim_rift_gen,
                module_field_tinkerer,
                module_flint_and_steel,
                module_grafter,
                module_hoe,
                module_leafblower,
                module_luxcaplauncher,
                module_mffsfieldteleporter,
//    module_octerminal,
                module_omniprobe,
                module_omniwrench,
                module_ore_scanner,
                module_cmpsd,
                module_portable_crafting_table,
                module_refinedstoragewirelessgrid,
                module_scoop,
                module_tree_tap,
                module_refinedstoragewirelessgrid,
                module_scoop,
                module_tree_tap,

                // Weapon
                module_blade_launcher,
                module_lightning,
                module_melee_assist,
                module_plasma_cannon,
                module_railgun

        );
        ((ItemComponent)components).populate();

    }

    private static Item itemRegister(Item item, String regName, String unlocalizedName) {
        // including the ModID in the unlocalized name helps keep the names unique so they can be used as keys for maps
        item.setUnlocalizedName(new StringBuilder(MODID).append(".").append(unlocalizedName).toString());
        item.setRegistryName(new ResourceLocation(MODID, regName));
        return item;
    }

    @SubscribeEvent
    public static void initBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(BlockTinkerTable.getInstance());
        event.getRegistry().register(BlockLuxCapacitor.getInstance());
//        event.getRegistry().register(new BlockLiquidNitrogen()); // TODO?

    }

    @SubscribeEvent
    public static void initBlockItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(ItemBlockTinkerTable.getInstance());
        event.getRegistry().register(ItemBlockLuxCapacitor.getInstance());
    }
}
