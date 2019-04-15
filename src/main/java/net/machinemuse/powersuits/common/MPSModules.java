package net.machinemuse.powersuits.common;

import net.machinemuse.numina.basemod.MuseLogger;
import net.machinemuse.numina.misc.ModCompatibility;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPowerModule;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.powermodule.armor.DiamondPlatingModule;
import net.machinemuse.powersuits.powermodule.armor.EnergyShieldModule;
import net.machinemuse.powersuits.powermodule.armor.IronPlatingModule;
import net.machinemuse.powersuits.powermodule.armor.LeatherPlatingModule;
import net.machinemuse.powersuits.powermodule.cosmetic.TransparentArmorModule;
import net.machinemuse.powersuits.powermodule.energy_generation.AdvancedSolarGenerator;
import net.machinemuse.powersuits.powermodule.energy_generation.KineticGeneratorModule;
import net.machinemuse.powersuits.powermodule.energy_generation.SolarGeneratorModule;
import net.machinemuse.powersuits.powermodule.energy_generation.ThermalGeneratorModule;
import net.machinemuse.powersuits.powermodule.energy_storage.AdvancedBatteryModule;
import net.machinemuse.powersuits.powermodule.energy_storage.BasicBatteryModule;
import net.machinemuse.powersuits.powermodule.energy_storage.EliteBatteryModule;
import net.machinemuse.powersuits.powermodule.energy_storage.UltimateBatteryModule;
import net.machinemuse.powersuits.powermodule.environmental.*;
import net.machinemuse.powersuits.powermodule.mining_enhancement.*;
import net.machinemuse.powersuits.powermodule.movement.*;
import net.machinemuse.powersuits.powermodule.special.ClockModule;
import net.machinemuse.powersuits.powermodule.special.CompassModule;
import net.machinemuse.powersuits.powermodule.special.InvisibilityModule;
import net.machinemuse.powersuits.powermodule.special.MagnetModule;
import net.machinemuse.powersuits.powermodule.tool.*;
import net.machinemuse.powersuits.powermodule.vision.BinocularsModule;
import net.machinemuse.powersuits.powermodule.vision.NightVisionModule;
import net.machinemuse.powersuits.powermodule.vision.ThaumGogglesModule;
import net.machinemuse.powersuits.powermodule.weapon.*;

import static net.machinemuse.numina.module.EnumModuleTarget.*;

public class MPSModules {
    public static void addModule(IPowerModule module) {
        if (MPSConfig.INSTANCE.getModuleAllowedorDefault(module.getDataName(), true))
            ModuleManager.INSTANCE.addModule(module);
    }

    /**
     * Load all the modules in the config file into memory. Eventually. For now,
     * they are hardcoded.
     */
    public static void loadPowerModules() {
        // FIXME: these need to be sorted
        /* Armor -------------------------------- */
        addModule(new LeatherPlatingModule(ARMORONLY));
        addModule(new IronPlatingModule(ARMORONLY));
        addModule(new DiamondPlatingModule(ARMORONLY));
        addModule(new EnergyShieldModule(ARMORONLY));

        /* Cosmetic ----------------------------- */
        addModule(new TransparentArmorModule(ARMORONLY));


        /* Energy ------------------------------- */
        addModule(new BasicBatteryModule(ALLITEMS));
        addModule(new AdvancedBatteryModule(ALLITEMS));
        addModule(new EliteBatteryModule(ALLITEMS));
        addModule(new UltimateBatteryModule(ALLITEMS));


        /* Power Fist --------------------------- */
        addModule(new AxeModule(TOOLONLY));
        addModule(new PickaxeModule(TOOLONLY));
        addModule(new DiamondPickUpgradeModule(TOOLONLY));
        addModule(new ShovelModule(TOOLONLY));
        addModule(new ShearsModule(TOOLONLY));
        addModule(new HoeModule(TOOLONLY));
        addModule(new LuxCapacitor(TOOLONLY));
        addModule(new FieldTinkerModule(TOOLONLY));
        addModule(new MeleeAssistModule(TOOLONLY));
        addModule(new PlasmaCannonModule(TOOLONLY));
        addModule(new RailgunModule(TOOLONLY));
        addModule(new BladeLauncherModule(TOOLONLY));
        addModule(new BlinkDriveModule(TOOLONLY));
        addModule(new InPlaceAssemblerModule(TOOLONLY));
        addModule(new LeafBlowerModule(TOOLONLY));
        addModule(new FlintAndSteelModule(TOOLONLY));
        addModule(new LightningModule(TOOLONLY));
        addModule(new DimensionalRiftModule(TOOLONLY));
        // Mining Enhancements
        addModule(new AOEPickUpgradeModule(TOOLONLY));
        addModule(new AquaAffinityModule(TOOLONLY));
        addModule(new FortuneModule(TOOLONLY));
        addModule(new SilkTouchModule(TOOLONLY));


        /* Helmet ------------------------------- */
        addModule(new WaterElectrolyzerModule(HEADONLY));
        addModule(new NightVisionModule(HEADONLY));
        addModule(new BinocularsModule(HEADONLY));
        addModule(new FlightControlModule(HEADONLY));
        addModule(new SolarGeneratorModule(HEADONLY));
        addModule(new AutoFeederModule(HEADONLY));
        addModule(new ClockModule(HEADONLY));
        addModule(new CompassModule(HEADONLY));
        addModule(new AdvancedSolarGenerator(HEADONLY));


        /* Chestplate --------------------------- */
        addModule(new ParachuteModule(TORSOONLY));
        addModule(new GliderModule(TORSOONLY));
        addModule(new JetPackModule(TORSOONLY));
        addModule(new InvisibilityModule(TORSOONLY));
        addModule(new BasicCoolingSystemModule(TORSOONLY));
        addModule(new MagnetModule(TORSOONLY));
        addModule(new ThermalGeneratorModule(TORSOONLY));
        addModule(new MobRepulsorModule(TORSOONLY));
        addModule(new AdvancedCoolingSystem(TORSOONLY));
        //addModule(new CoalGenerator(TORSOONLY)); //TODO: Finish


        /* Legs --------------------------------- */
        addModule(new SprintAssistModule(LEGSONLY));
        addModule(new JumpAssistModule(LEGSONLY));
        addModule(new SwimAssistModule(LEGSONLY));
        addModule(new KineticGeneratorModule(LEGSONLY));
        addModule(new ClimbAssistModule(LEGSONLY));


        /* Feet --------------------------------- */
        addModule(new JetBootsModule(FEETONLY));
        addModule(new ShockAbsorberModule(FEETONLY));


        /** Conditional loading ------------------------------------------------------------------- */
        // Thaumcraft
        if (ModCompatibility.isThaumCraftLoaded())
            addModule(new ThaumGogglesModule(EnumModuleTarget.HEADONLY));

        // CoFHCore
        if (ModCompatibility.isCOFHCoreLoaded())
            addModule(new OmniWrenchModule(EnumModuleTarget.TOOLONLY));

        // Mekanism
        if (ModCompatibility.isMekanismLoaded())
            addModule(new MADModule(EnumModuleTarget.TOOLONLY));

        // Industrialcraft
        if (ModCompatibility.isIndustrialCraftLoaded()) {
            addModule(new HazmatModule(EnumModuleTarget.ARMORONLY));
            addModule(new TreetapModule(EnumModuleTarget.TOOLONLY));
        }

        // Galacticraft
        if (ModCompatibility.isGalacticraftLoaded())
            addModule(new AirtightSealModule(EnumModuleTarget.HEADONLY));

        // Forestry
        if (ModCompatibility.isForestryLoaded()) {
            addModule(new GrafterModule(EnumModuleTarget.TOOLONLY));
            addModule(new ScoopModule(EnumModuleTarget.TOOLONLY));
            addModule(new ApiaristArmorModule(EnumModuleTarget.ARMORONLY));
        }

        // Chisel
        if (ModCompatibility.isChiselLoaded())
            try {
                addModule(new ChiselModule(EnumModuleTarget.TOOLONLY));
            } catch (Exception e) {
                MuseLogger.logException("Couldn't add Chisel module", e);
            }

        // Applied Energistics
        if (ModCompatibility.isAppengLoaded()) {
            addModule(new AppEngWirelessModule(EnumModuleTarget.TOOLONLY));

            // Extra Cells 2
            if (ModCompatibility.isExtraCellsLoaded())
                addModule(new AppEngWirelessFluidModule(EnumModuleTarget.TOOLONLY));
        }

        // Multi-Mod Compatible OmniProbe
        if (ModCompatibility.isEnderIOLoaded() || ModCompatibility.isMFRLoaded() || ModCompatibility.isRailcraftLoaded())
            addModule(new OmniProbeModule(EnumModuleTarget.TOOLONLY));

// TODO: on hold for now. Needs a conditional fiuld tank and handler. May not be worth it.
        // Compact Machines
        if (ModCompatibility.isCompactMachinesLoaded())
            addModule(new PersonalShrinkingModule(EnumModuleTarget.TOOLONLY));

        // Refined Storage
        if (ModCompatibility.isRefinedStorageLoaded())
            addModule(new RefinedStorageWirelessModule(EnumModuleTarget.TOOLONLY));

        // Scannable
        if (ModCompatibility.isScannableLoaded())
            addModule(new OreScannerModule(EnumModuleTarget.TOOLONLY));
    }
}