package net.machinemuse.powersuits;

import net.machinemuse.numina.common.ModCompatibility;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.common.MPSCreativeTab;
import net.machinemuse.powersuits.config.MPSSettings;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorBoots;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorChestplate;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorHelmet;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorLeggings;
import net.machinemuse.powersuits.item.component.ItemComponent;
import net.machinemuse.powersuits.item.module.armor.ItemModulePlatingDiamond;
import net.machinemuse.powersuits.item.module.armor.ItemModulePlatingEnergyShield;
import net.machinemuse.powersuits.item.module.armor.ItemModulePlatingIron;
import net.machinemuse.powersuits.item.module.armor.ItemModulePlatingLeather;
import net.machinemuse.powersuits.item.module.cosmetic.ItemModuleTransparentArmor;
import net.machinemuse.powersuits.item.module.energy.generation.*;
import net.machinemuse.powersuits.item.module.energy.storage.ItemModuleBatteryAdvanced;
import net.machinemuse.powersuits.item.module.energy.storage.ItemModuleBatteryBasic;
import net.machinemuse.powersuits.item.module.energy.storage.ItemModuleBatteryElite;
import net.machinemuse.powersuits.item.module.energy.storage.ItemModuleBatteryUltimate;
import net.machinemuse.powersuits.item.module.environmental.*;
import net.machinemuse.powersuits.item.module.miningenhancement.*;
import net.machinemuse.powersuits.item.module.movement.*;
import net.machinemuse.powersuits.item.module.special.ItemModuleClock;
import net.machinemuse.powersuits.item.module.special.ItemModuleCompass;
import net.machinemuse.powersuits.item.module.special.ItemModuleInvisibility;
import net.machinemuse.powersuits.item.module.special.ItemModuleMagnet;
import net.machinemuse.powersuits.item.module.tool.*;
import net.machinemuse.powersuits.item.module.vision.ItemModuleBinoculars;
import net.machinemuse.powersuits.item.module.vision.ItemModuleNightVision;
import net.machinemuse.powersuits.item.module.vision.ItemModuleThaumicGoggles;
import net.machinemuse.powersuits.item.module.weapon.*;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.machinemuse.powersuits.itemblock.ItemBlockLuxCapacitor;
import net.machinemuse.powersuits.itemblock.ItemBlockTinkerTable;
import net.machinemuse.powersuits.tileentity.TileEntityLuxCapacitor;
import net.machinemuse.powersuits.tileentity.TileEntityTinkerTable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ObjectHolder;

import static net.machinemuse.powersuits.ModularPowersuits.MODID;

//@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public enum MPSItems {



    /**
     * Module RegistryNames -----------------------------------------------------------------------
     */
    // Debug --------------------------------------------------------------------------------------
    public static final ResourceLocation MODULE_DEBUG__REG_NAME = toLoc("module.debug");

    // Armor --------------------------------------------------------------------------------------
    public static final ResourceLocation MODULE_PLATING_LEATHER__REG_NAME = toLoc("module.plating.leather");
    public static final ResourceLocation MODULE_PLATING_IRON__REG_NAME = toLoc("module.plating.iron");
    public static final ResourceLocation MODULE_PLATING_DIAMOND__REG_NAME = toLoc("module.plating.diamond");
    public static final ResourceLocation MODULE_ENERGY_SHIELD__REG_NAME = toLoc("module.plating.energyshield");

    // Cosmetic -----------------------------------------------------------------------------------
    public static final ResourceLocation MODULE_TRANSPARENT_ARMOR__REG_NAME = toLoc("module.transparentarmor");

    // Energy Storage -----------------------------------------------------------------------------
    public static final ResourceLocation MODULE_BATTERY_BASIC__REG_NAME = toLoc("module.battery.basic");
    public static final ResourceLocation MODULE_BATTERY_ADVANCED__REG_NAME = toLoc("module.battery.advanced");
    public static final ResourceLocation MODULE_BATTERY_ELITE__REG_NAME = toLoc("module.battery.elite");
    public static final ResourceLocation MODULE_BATTERY_ULTIMATE__REG_NAME = toLoc("module.battery.ultimate");

    // Energy Generation --------------------------------------------------------------------------
    public static final ResourceLocation MODULE_GENERATOR_SOLAR_BASIC__REG_NAME = toLoc("module.generator.solar.basic");
    public static final ResourceLocation MODULE_GENERATOR_SOLAR_ADVANCED__REG_NAME = toLoc("module.generator.solar.advanced");
    public static final ResourceLocation MODULE_GENERATOR_COAL__REG_NAME = toLoc("module.generator.coal");
    public static final ResourceLocation MODULE_GENERATOR_KINETIC__REG_NAME = toLoc("module.generator.kinetic");
    public static final ResourceLocation MODULE_GENERATOR_THERMAL__REG_NAME = toLoc("module.generator.thermal");

    // Environmental ------------------------------------------------------------------------------
    public static final ResourceLocation MODULE_COOLING_SYSTEM_BASIC__REG_NAME = toLoc("module.environmental.coolingsystem.basic");
    public static final ResourceLocation MODULE_COOLING_SYSTEM_ADVANCED__REG_NAME = toLoc("coolingsystem.advanced");
    public static final ResourceLocation MODULE_AIRTIGHT_SEAL__REG_NAME = toLoc("airtightSeal");
    public static final ResourceLocation MODULE_APIARIST_ARMOR__REG_NAME = toLoc("apiaristArmor");
    public static final ResourceLocation MODULE_HAZMAT__REG_NAME = toLoc("hazmat");
    public static final ResourceLocation MODULE_AUTO_FEEDER__REG_NAME = toLoc("autoFeeder");
    public static final ResourceLocation MODULE_MOB_REPULSOR__REG_NAME = toLoc("mobRepulsor");
    public static final ResourceLocation MODULE_WATER_ELECTROLYZER__REG_NAME = toLoc("waterElectrolyzer");

    // Mining Enhancements ------------------------------------------------------------------------
    public static final ResourceLocation MODULE_AOE_PICK_UPGRADE__REG_NAME = toLoc("module.miningEnhancement.aoePickUpgrade");
    public static final ResourceLocation MODULE_AQUA_AFFINITY__REG_NAME = toLoc("module.miningEnhancement.aquaAffinity");
    public static final ResourceLocation MODULE_SILK_TOUCH__REG_NAME = toLoc("module.miningEnhancement.silk_touch");
    public static final ResourceLocation MODULE_MAD__REG_NAME = toLoc("module.miningEnhancement.madModule");
    public static final ResourceLocation MODULE_FORTUNE_REG_NAME= toLoc("module.miningEnhancement.fortuneModule");

    // Movement -----------------------------------------------------------------------------------
    public static final ResourceLocation MODULE_BLINK_DRIVE__REG_NAME = toLoc("module.movement.blinkDrive");
    public static final ResourceLocation MODULE_CLIMB_ASSIST__REG_NAME = toLoc("module.movement.climbAssist");
    public static final ResourceLocation MODULE_DIMENSIONAL_RIFT__REG_NAME = toLoc("module.movement.dimRiftGen");
    public static final ResourceLocation MODULE_FLIGHT_CONTROL__REG_NAME = toLoc("module.movement.flightControl");
    public static final ResourceLocation MODULE_GLIDER__REG_NAME = toLoc("module.movement.glider");
    public static final ResourceLocation MODULE_JETBOOTS__REG_NAME = toLoc("module.movement.jetBoots");
    public static final ResourceLocation MODULE_JETPACK__REG_NAME = toLoc("module.movement.jetpack");
    public static final ResourceLocation MODULE_JUMP_ASSIST__REG_NAME = toLoc("module.movement.jumpAssist");
    public static final ResourceLocation MODULE_PARACHUTE__REG_NAME = toLoc("module.movement.parachute");
    public static final ResourceLocation MODULE_SHOCK_ABSORBER__REG_NAME = toLoc("module.movement.shockAbsorber");
    public static final ResourceLocation MODULE_SPRINT_ASSIST__REG_NAME = toLoc("module.movement.sprintAssist");
    public static final ResourceLocation MODULE_SWIM_BOOST__REG_NAME = toLoc("module.movement.swimAssist");

    // Special ------------------------------------------------------------------------------------
    public static final ResourceLocation MODULE_CLOCK__REG_NAME = toLoc("module.special.clock");
    public static final ResourceLocation MODULE_COMPASS__REG_NAME = toLoc("module.special.compass");
    public static final ResourceLocation MODULE_ACTIVE_CAMOUFLAGE__REG_NAME = toLoc("module.special.invisibility");
    public static final ResourceLocation MODULE_MAGNET__REG_NAME = toLoc("module.special.magnet");

    // Vision -------------------------------------------------------------------------------------
    public static final ResourceLocation MODULE_BINOCULARS__REG_NAME = toLoc("module.vision.binoculars");
    public static final ResourceLocation MODULE_NIGHT_VISION__REG_NAME = toLoc("module.vision.nightVision");
    public static final ResourceLocation MODULE_THAUM_GOGGLES__REG_NAME = toLoc("module.vision.aurameter");

    // Tools --------------------------------------------------------------------------------------
    public static final ResourceLocation MODULE_APPENG_EC_WIRELESS_FLUID__REG_NAME = toLoc("module.tool.appengECWirelessFluid");
    public static final ResourceLocation MODULE_APPENG_WIRELESS__REG_NAME = toLoc("module.tool.appengWireless");
    public static final ResourceLocation MODULE_AXE__REG_NAME = toLoc("module.tool.axe");
    public static final ResourceLocation MODULE_CHISEL__REG_NAME = toLoc("module.tool.chisel");
    public static final ResourceLocation MODULE_DIAMOND_PICK_UPGRADE__REG_NAME = toLoc("module.tool.diamondPickUpgrade");
    public static final ResourceLocation MODULE_FIELD_TINKER__REG_NAME = toLoc("module.tool.fieldTinkerer");
    public static final ResourceLocation MODULE_FLINT_AND_STEEL__REG_NAME = toLoc("module.tool.flintAndSteel");
    public static final ResourceLocation MODULE_GRAFTER__REG_NAME = toLoc("module.tool.grafter");
    public static final ResourceLocation MODULE_HOE__REG_NAME = toLoc("module.tool.hoe");
    public static final ResourceLocation MODULE_LEAF_BLOWER__REG_NAME = toLoc("module.tool.leafBlower");
    public static final ResourceLocation MODULE_LUX_CAPACITOR__REG_NAME = toLoc("module.tool.luxCapacitor");
    public static final ResourceLocation MODULE_FIELD_TELEPORTER__REG_NAME = toLoc("module.tool.mffsFieldTeleporter");
    public static final ResourceLocation MODULE_OC_TERMINAL__REG_NAME = toLoc("module.tool.ocTerminal");
    public static final ResourceLocation MODULE_PORTABLE_CRAFTING__REG_NAME = toLoc("module.tool.portableCraftingTable");
    public static final ResourceLocation MODULE_OMNIPROBE__REG_NAME = toLoc("module.tool.omniProbe");
    public static final ResourceLocation MODULE_OMNI_WRENCH__REG_NAME = toLoc("module.tool.omniwrench");
    public static final ResourceLocation MODULE_ORE_SCANNER__REG_NAME = toLoc("module.tool.oreScanner");
    public static final ResourceLocation MODULE_CM_PSD__REG_NAME = toLoc("module.tool.cmPSD");//"Personal Shrinking Device");
    public static final ResourceLocation MODULE_PICKAXE__REG_NAME = toLoc("module.tool.pickaxe");
    public static final ResourceLocation MODULE_REF_STOR_WIRELESS__REG_NAME = toLoc("module.tool.refinedStorageWirelessGrid");//"Refined Storage Wireless Grid");
    public static final ResourceLocation MODULE_SCOOP__REG_NAME = toLoc("module.tool.scoop");
    public static final ResourceLocation MODULE_SHEARS__REG_NAME = toLoc("module.tool.shears");
    public static final ResourceLocation MODULE_SHOVEL__REG_NAME = toLoc("module.tool.shovel");
    public static final ResourceLocation MODULE_TREETAP__REG_NAME = toLoc("module.tool.treetap");

    // Weapons ------------------------------------------------------------------------------------
    public static final ResourceLocation MODULE_WEAPON__BLADE_LAUNCHER__REG_NAME = toLoc("module.weapon.bladeLauncher");
    public static final ResourceLocation MODULE_WEAPON__LIGHTNING__REG_NAME = toLoc("module.weapon.lightningSummoner");
    public static final ResourceLocation MODULE_WEAPON__MELEE_ASSIST__REG_NAME = toLoc("module.weapon.meleeAssist");
    public static final ResourceLocation MODULE_WEAPON__PLASMA_CANNON__REG_NAME = toLoc("module.weapon.plasmaCannon");
    public static final ResourceLocation MODULE_WEAPON__RAILGUN__REG_NAME = toLoc("module.weapon.railgun");














    @SubscribeEvent
    public void registerItems(final RegistryEvent.Register<Item> itemRegistryEvent) {
        itemRegistryEvent.getRegistry().registerAll(
                // HandHeld --------------------------------------------------------------------------------------
                new ItemPowerFist().setRegistryName(POWER_FIST__REG_NAME),







        MuseLogger.logInfo("Finished registering MPS Items");

        // Modules ---------------------------------------------------------------------------------------

        // Debug ----------------------------------------------------------------------------------

        // Armor ----------------------------------------------------------------------------------
        registerModule(itemRegistryEvent, new ItemModulePlatingLeather(), MODULE_PLATING_LEATHER__REG_NAME);
        registerModule(itemRegistryEvent, new ItemModulePlatingIron(), MODULE_PLATING_IRON__REG_NAME);
        registerModule(itemRegistryEvent, new ItemModulePlatingDiamond(), MODULE_PLATING_DIAMOND__REG_NAME);
        registerModule(itemRegistryEvent, new ItemModulePlatingEnergyShield(), MODULE_ENERGY_SHIELD__REG_NAME);

        // Cosmetic -------------------------------------------------------------------------------
        registerModule(itemRegistryEvent,  new ItemModuleTransparentArmor(),  MODULE_TRANSPARENT_ARMOR__REG_NAME);

        // Energy Storage -------------------------------------------------------------------------
        registerModule(itemRegistryEvent,  new ItemModuleBatteryBasic(),  MODULE_BATTERY_BASIC__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleBatteryAdvanced(),  MODULE_BATTERY_ADVANCED__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleBatteryElite(),  MODULE_BATTERY_ELITE__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleBatteryUltimate(),  MODULE_BATTERY_ULTIMATE__REG_NAME);

        // Energy Generation ----------------------------------------------------------------------
        registerModule(itemRegistryEvent,  new ItemModuleGeneratorSolarBasic(),  MODULE_GENERATOR_SOLAR_BASIC__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleGeneratorSolarAdvanced(),  MODULE_GENERATOR_SOLAR_ADVANCED__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleGeneratorCoal(),  MODULE_GENERATOR_COAL__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleGeneratorKinetic(),  MODULE_GENERATOR_KINETIC__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleGeneratorThermal(),  MODULE_GENERATOR_THERMAL__REG_NAME);

        // Mining Enhancements
        registerModule(itemRegistryEvent,  new ItemModuleAOEPickUpgrade(),  MODULE_AOE_PICK_UPGRADE__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleAquaAffinity(),  MODULE_AQUA_AFFINITY__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleSilkTouch(),  MODULE_SILK_TOUCH__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleMAD(),  MODULE_MAD__REG_NAME, ModCompatibility.isMekanismLoaded());
        registerModule(itemRegistryEvent,  new ItemModuleFortune(),  MODULE_FORTUNE_REG_NAME);

        // Movement -------------------------------------------------------------------------------
        registerModule(itemRegistryEvent,  new ItemModuleBlinkDrive(),  MODULE_BLINK_DRIVE__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleClimbAssist(),  MODULE_CLIMB_ASSIST__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleDimensionalRift(),  MODULE_DIMENSIONAL_RIFT__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleFlightControl(),  MODULE_FLIGHT_CONTROL__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleGlider(),  MODULE_GLIDER__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleJetBoots(),  MODULE_JETBOOTS__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleJetPack(),  MODULE_JETPACK__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleJumpAssist(),  MODULE_JUMP_ASSIST__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleParachute(),  MODULE_PARACHUTE__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleShockAbsorber(),  MODULE_SHOCK_ABSORBER__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleSprintAssist(),  MODULE_SPRINT_ASSIST__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleSwimAssist(),  MODULE_SWIM_BOOST__REG_NAME);

        // Special --------------------------------------------------------------------------------
        registerModule(itemRegistryEvent,  new ItemModuleClock(),  MODULE_CLOCK__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleCompass(),  MODULE_COMPASS__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleInvisibility(),  MODULE_ACTIVE_CAMOUFLAGE__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleMagnet(),  MODULE_MAGNET__REG_NAME);

        // Tools ----------------------------------------------------------------------------------
        registerModule(itemRegistryEvent,  new ItemModuleAppEngWirelessFluid(),  MODULE_APPENG_EC_WIRELESS_FLUID__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleAppEngWireless(),  MODULE_APPENG_WIRELESS__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleAxe(),  MODULE_AXE__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleChisel(),  MODULE_CHISEL__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleDiamondPickUpgrade(),  MODULE_DIAMOND_PICK_UPGRADE__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleFieldTinker(),  MODULE_FIELD_TINKER__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleFlintAndSteel(),  MODULE_FLINT_AND_STEEL__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleGrafter(),  MODULE_GRAFTER__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleHoe(),  MODULE_HOE__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleLeafBlower(),  MODULE_LEAF_BLOWER__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleLuxCapacitor(),  MODULE_LUX_CAPACITOR__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleMFFSFieldTeleporter(),  MODULE_FIELD_TELEPORTER__REG_NAME);
//                registerModule(itemRegistryEvent,  new ,  MODULE_OC_TERMINAL__REG_NAME);  // fixme: not finished
        registerModule(itemRegistryEvent,  new ItemModuleInPlaceAssembler(),  MODULE_PORTABLE_CRAFTING__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleOmniProbe(),  MODULE_OMNIPROBE__REG_NAME);
//        registerModule(itemRegistryEvent,  new ItemModuleOmniWrench(),  MODULE_OMNI_WRENCH__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleOreScanner(),  MODULE_ORE_SCANNER__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModulePersonalShrinking(),  MODULE_CM_PSD__REG_NAME); //"Personal Shrinking Device");
        registerModule(itemRegistryEvent,  new ItemModulePickaxe(),  MODULE_PICKAXE__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleRefinedStorageWireless(),  MODULE_REF_STOR_WIRELESS__REG_NAME); //"Refined Storage Wireless Grid");
        registerModule(itemRegistryEvent,  new ItemModuleScoop(),  MODULE_SCOOP__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleShears(),  MODULE_SHEARS__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleShovel(),  MODULE_SHOVEL__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleTreetap(),  MODULE_TREETAP__REG_NAME);

        // Environmental --------------------------------------------------------------------------
        registerModule(itemRegistryEvent,  new ItemModuleBasicCoolingSystem(),  MODULE_COOLING_SYSTEM_BASIC__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleAdvancedCoolingSystem(),  MODULE_COOLING_SYSTEM_ADVANCED__REG_NAME);
        if (ModCompatibility.isGalacticraftLoaded())
            registerModule(itemRegistryEvent,  new ItemModuleAirtightSeal(),  MODULE_AIRTIGHT_SEAL__REG_NAME);  // TODO: conditional module registration
        registerModule(itemRegistryEvent,  new ItemModuleApiaristArmor(),  MODULE_APIARIST_ARMOR__REG_NAME); // TODO: conditional module registration
        if (ModCompatibility.isIndustrialCraftLoaded())
            registerModule(itemRegistryEvent,  new ItemModuleHazmat(),  MODULE_HAZMAT__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleAutoFeeder(),  MODULE_AUTO_FEEDER__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleMobRepulsor(),  MODULE_MOB_REPULSOR__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleWaterElectrolyzer(),  MODULE_WATER_ELECTROLYZER__REG_NAME);

        // Vision ---------------------------------------------------------------------------------
        registerModule(itemRegistryEvent,  new ItemModuleBinoculars(), MODULE_BINOCULARS__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleNightVision(),  MODULE_NIGHT_VISION__REG_NAME);
        if (ModCompatibility.isThaumCraftLoaded())
            registerModule(itemRegistryEvent,  new ItemModuleThaumicGoggles(),  MODULE_THAUM_GOGGLES__REG_NAME);

        // Weapons --------------------------------------------------------------------------------
        registerModule(itemRegistryEvent,  new ItemModuleLauncher(),  MODULE_WEAPON__BLADE_LAUNCHER__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleLightning(),  MODULE_WEAPON__LIGHTNING__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleMeleeAssist(),  MODULE_WEAPON__MELEE_ASSIST__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModulePlasmaCannon(),  MODULE_WEAPON__PLASMA_CANNON__REG_NAME);
        registerModule(itemRegistryEvent,  new ItemModuleRailgun(),  MODULE_WEAPON__RAILGUN__REG_NAME);

        // register a new block here
        MuseLogger.logInfo("Finished registering MPS Modules");
    }

