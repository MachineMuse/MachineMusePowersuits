package net.machinemuse.powersuits.common;

import net.machinemuse.numina.api.module.IPowerModule;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.powermodule.armor.DiamondPlatingModule;
import net.machinemuse.powersuits.powermodule.armor.EnergyShieldModule;
import net.machinemuse.powersuits.powermodule.armor.IronPlatingModule;
import net.machinemuse.powersuits.powermodule.armor.LeatherPlatingModule;
import net.machinemuse.powersuits.powermodule.cosmetic.*;
import net.machinemuse.powersuits.powermodule.energy.*;
import net.machinemuse.powersuits.powermodule.environmental.HeatSinkModule;
import net.machinemuse.powersuits.powermodule.environmental.NitrogenCoolingSystem;
import net.machinemuse.powersuits.powermodule.environmental.WaterTankModule;
import net.machinemuse.powersuits.powermodule.misc.*;
import net.machinemuse.powersuits.powermodule.movement.*;
import net.machinemuse.powersuits.powermodule.tool.*;
import net.machinemuse.powersuits.powermodule.weapon.*;

import static net.machinemuse.numina.api.module.EnumModuleTarget.*;
import static net.machinemuse.numina.api.module.EnumModuleTarget.FEETONLY;
import static net.machinemuse.numina.api.module.EnumModuleTarget.LEGSONLY;

public class MPSModules {

    public static void addModule(IPowerModule module) {
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
        addModule(new HeatSinkModule(ARMORONLY));


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
//        addModule(new OreScannerModule(TOOLONLY));
        addModule(new LeafBlowerModule(TOOLONLY));
        addModule(new FlintAndSteelModule(TOOLONLY));
        addModule(new LightningModule(TOOLONLY));
        addModule(new DimensionalRiftModule(TOOLONLY));


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
        addModule(new CoolingSystemModule(TORSOONLY));
        addModule(new MagnetModule(TORSOONLY));
        addModule(new ThermalGeneratorModule(TORSOONLY));
        addModule(new MobRepulsorModule(TORSOONLY));
        addModule(new WaterTankModule(TORSOONLY));
        addModule(new NitrogenCoolingSystem(TORSOONLY));
        addModule(new MechanicalAssistance(TORSOONLY));
        //addModule(new CoalGenerator(TORSOONLY)); //doesn't seem to be working


        /* Legs --------------------------------- */
        addModule(new SprintAssistModule(LEGSONLY));
        addModule(new JumpAssistModule(LEGSONLY));
        addModule(new SwimAssistModule(LEGSONLY));
        addModule(new KineticGeneratorModule(LEGSONLY));
        addModule(new ClimbAssistModule(LEGSONLY));


        /* Feet --------------------------------- */
        addModule(new JetBootsModule(FEETONLY));
        addModule(new ShockAbsorberModule(FEETONLY));
    }
}
