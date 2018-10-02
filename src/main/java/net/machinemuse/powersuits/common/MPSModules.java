package net.machinemuse.powersuits.common;

import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPowerModule;
import net.machinemuse.numina.common.ModCompatibility;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.powermodule.armor.DiamondPlatingModule;
import net.machinemuse.powersuits.powermodule.armor.EnergyShieldModule;
import net.machinemuse.powersuits.powermodule.armor.IronPlatingModule;
import net.machinemuse.powersuits.powermodule.armor.LeatherPlatingModule;
import net.machinemuse.powersuits.powermodule.cosmetic.TransparentArmorModule;
import net.machinemuse.powersuits.powermodule.energy.*;
import net.machinemuse.powersuits.powermodule.environmental.*;
import net.machinemuse.powersuits.powermodule.movement.*;
import net.machinemuse.powersuits.powermodule.special.*;
import net.machinemuse.powersuits.powermodule.tool.*;
import net.machinemuse.powersuits.powermodule.vision.BinocularsModule;
import net.machinemuse.powersuits.powermodule.vision.NightVisionModule;
import net.machinemuse.powersuits.powermodule.vision.ThaumGogglesModule;
import net.machinemuse.powersuits.powermodule.weapon.*;

import static net.machinemuse.numina.api.module.EnumModuleTarget.*;

public class MPSModules {

    public static void addModule(IPowerModule module) {
        addModuleConditionally(module, true);
    }

    public static void addModuleConditionally(IPowerModule module, boolean condition) {
        if (condition && MPSConfig.INSTANCE.getModuleAllowedorDefault(module.getDataName(), condition))
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
        addModule(new AquaAffinityModule(TOOLONLY));
        addModule(new InPlaceAssemblerModule(TOOLONLY));
        addModule(new LeafBlowerModule(TOOLONLY));
        addModule(new FlintAndSteelModule(TOOLONLY));
        addModule(new LightningModule(TOOLONLY));
        addModule(new DimensionalRiftModule(TOOLONLY));
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
        addModuleConditionally(new ThaumGogglesModule(EnumModuleTarget.HEADONLY),  ModCompatibility.isThaumCraftLoaded());

        // CoFHCore
        addModuleConditionally(new OmniWrenchModule(EnumModuleTarget.TOOLONLY), ModCompatibility.isCOFHCoreLoaded());

        // Mekanism
        addModuleConditionally(new MADModule(EnumModuleTarget.TOOLONLY), ModCompatibility.isMekanismLoaded());

        // Industrialcraft
        addModuleConditionally(new HazmatModule(EnumModuleTarget.ARMORONLY), ModCompatibility.isIndustrialCraftLoaded());
        addModuleConditionally(new TreetapModule(EnumModuleTarget.TOOLONLY), ModCompatibility.isIndustrialCraftLoaded());

        // Galacticraft
        addModuleConditionally(new AirtightSealModule(EnumModuleTarget.HEADONLY), ModCompatibility.isGalacticraftLoaded());

        // Forestry
        addModuleConditionally(new GrafterModule(EnumModuleTarget.TOOLONLY), ModCompatibility.isForestryLoaded());
        addModuleConditionally(new ScoopModule(EnumModuleTarget.TOOLONLY), ModCompatibility.isForestryLoaded());
        addModuleConditionally(new ApiaristArmorModule(EnumModuleTarget.ARMORONLY), ModCompatibility.isForestryLoaded());

        // Chisel
        try {
            addModuleConditionally(new ChiselModule(EnumModuleTarget.TOOLONLY), ModCompatibility.isChiselLoaded());
        } catch(Exception e) {
            MuseLogger.logException("Couldn't add Chisel module", e);
        }

        // Applied Energistics
        addModuleConditionally(new AppEngWirelessModule(EnumModuleTarget.TOOLONLY), ModCompatibility.isAppengLoaded());

        // Extra Cells 2
        addModuleConditionally(new AppEngWirelessFluidModule(EnumModuleTarget.TOOLONLY),
                ModCompatibility.isAppengLoaded() &&
                        ModCompatibility.isExtraCellsLoaded());

        // Multi-Mod Compatible OmniProbe
        addModuleConditionally(new OmniProbeModule(EnumModuleTarget.TOOLONLY),
                ModCompatibility.isEnderIOLoaded() ||
                        ModCompatibility.isMFRLoaded() ||
                        ModCompatibility.isRailcraftLoaded());

// TODO: on hold for now. Needs a conditional fiuld tank and handler. May not be worth it.
        // Compact Machines
        addModuleConditionally(new PersonalShrinkingModule(EnumModuleTarget.TOOLONLY), ModCompatibility.isCompactMachinesLoaded());

        // Refined Storage
        addModuleConditionally(new RefinedStorageWirelessModule(EnumModuleTarget.TOOLONLY), ModCompatibility.isRefinedStorageLoaded());

        // Scannable
        addModuleConditionally(new OreScannerModule(EnumModuleTarget.TOOLONLY),  ModCompatibility.isScannableLoaded());
    }
}