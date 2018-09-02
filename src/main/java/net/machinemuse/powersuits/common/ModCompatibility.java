package net.machinemuse.powersuits.common;

import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.powermodule.environmental.AirtightSealModule;
import net.machinemuse.powersuits.powermodule.environmental.ApiaristArmorModule;
import net.machinemuse.powersuits.powermodule.environmental.HazmatModule;
import net.machinemuse.powersuits.powermodule.tool.*;
import net.machinemuse.powersuits.powermodule.vision.ThaumGogglesModule;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModAPIManager;
import net.minecraftforge.fml.common.ModContainer;

import java.util.List;

public class ModCompatibility {
    public static boolean isTeslaLoaded() {
        return Loader.isModLoaded("tesla");
    }

    public static boolean isTechRebornLoaded() {
        return Loader.isModLoaded("techreborn");
    }

    public static boolean isGregTechLoaded() {
        return Loader.isModLoaded("gregtech");
    }

    // Industrialcraft common
    public static boolean isIndustrialCraftLoaded() {
        return Loader.isModLoaded("ic2");
    }


    public static final boolean isIndustrialCraftExpLoaded() {
        if (!isIndustrialCraftLoaded())
            return false;

        List<ModContainer> list = Loader.instance().getModList();
        for (ModContainer container : list) {
            if (container.getModId().toLowerCase().equals("ic2")) {
                if (container.getName().equals("IndustrialCraft 2"))
                    return true;
                return false;
            }
        }
        return false;
    }

    // Industrialcraft 2 classic (note redundant code is intentional for "just in case")
    public static final boolean isIndustrialCraftClassicLoaded() {
        if (!isIndustrialCraftLoaded())
            return false;

        List<ModContainer> list = Loader.instance().getModList();
        for (ModContainer container : list) {
            if (container.getModId().toLowerCase().equals("ic2")) {
                if (container.getName().equals("Industrial Craft Classic"))
                    return true;
                return false;
            }
        }
        return false;
    }

    public static boolean isThaumCraftLoaded() {
        return Loader.isModLoaded("thaumcraft");
    }

    public static boolean isThermalExpansionLoaded() {
        return Loader.isModLoaded("thermalexpansion") && Loader.isModLoaded("thermalfoundation");
    }

    public static boolean isGalacticraftLoaded() {
        return Loader.isModLoaded("galacticraftcore");
    }

    public static boolean isRFAPILoaded() {
        return ModAPIManager.INSTANCE.hasAPI("cofhapi|energy");
    }

    public static boolean isCOFHLibLoaded() {
        return ModAPIManager.INSTANCE.hasAPI("cofhlib");
    }

    public static boolean isCOFHCoreLoaded() {
//        return ModAPIManager.INSTANCE.hasAPI("cofhcore");
        return Loader.isModLoaded("cofhcore");
    }

    public static boolean isForestryLoaded() {
        return Loader.isModLoaded("forestry");
    }

    public static boolean isChiselLoaded() {
        return Loader.isModLoaded("chisel");
    }

    public static boolean isEnderIOLoaded() {
        return Loader.isModLoaded("enderio");
    }

    public static boolean isAppengLoaded() {
        return Loader.isModLoaded("appliedenergistics2");
    }

    public static boolean isExtraCellsLoaded() {
        return Loader.isModLoaded("extracells");
    }

    public static boolean isMFRLoaded() {
        return Loader.isModLoaded("mineFactoryreloaded");
    }

    public static boolean isRailcraftLoaded() {
        return Loader.isModLoaded("railcraft");
    }

    public static boolean isCompactMachinesLoaded() {
        return Loader.isModLoaded("cm2");
    }

    public static boolean isRenderPlayerAPILoaded() {
        return Loader.isModLoaded("RenderPlayerAPIPlugin");
    }

    public static boolean isRefinedStorageLoaded() {
        return Loader.isModLoaded("refinedstorage");
    }

    public static boolean isScannableLoaded() {
        return Loader.isModLoaded("scannable");
    }

    public static boolean isWirelessCraftingGridLoaded() {
        return Loader.isModLoaded("wcg");
    }

    public static boolean isMekanismLoaded() {
        return Loader.isModLoaded("mekanism");
    }

    public static boolean enableThaumGogglesModule() {
        return MPSConfig.INSTANCE.getModuleAllowedorDefault(MPSModuleConstants.MODULE_THAUM_GOGGLES__DATANAME, isThaumCraftLoaded());
    }

    public static void registerModSpecificModules() {

        // CoFH Lib - CoFHLib is included in CoFHCore
        if (isCOFHCoreLoaded()) {
            ModuleManager.INSTANCE.addModule(new OmniWrenchModule(EnumModuleTarget.TOOLONLY));
        }

        // Mekanism
        if(isMekanismLoaded()) {
            ModuleManager.INSTANCE.addModule(new MADModule(EnumModuleTarget.TOOLONLY));
        }


        // Thaumcraft
        if (isThaumCraftLoaded() && enableThaumGogglesModule()) {
            ModuleManager.INSTANCE.addModule(new ThaumGogglesModule(EnumModuleTarget.HEADONLY));
        }

        //IPowerModule module = new MultimeterModule(Collections.singletonList((IModularItem) MPSItems.INSTANCE.powerFist()));

        // Industrialcraft
        if (isIndustrialCraftLoaded()) {
            ModuleManager.INSTANCE.addModule(new HazmatModule(EnumModuleTarget.ARMORONLY));
            ModuleManager.INSTANCE.addModule(new TreetapModule(EnumModuleTarget.TOOLONLY));
        }

        // Galacticraft
        if (isGalacticraftLoaded()) {
            ModuleManager.INSTANCE.addModule(new AirtightSealModule(EnumModuleTarget.HEADONLY));
        }

        // Forestry
        if (isForestryLoaded()) {
            ModuleManager.INSTANCE.addModule(new GrafterModule(EnumModuleTarget.TOOLONLY));
            ModuleManager.INSTANCE.addModule(new ScoopModule(EnumModuleTarget.TOOLONLY));
            ModuleManager.INSTANCE.addModule(new ApiaristArmorModule(EnumModuleTarget.ARMORONLY));
        }

        // Chisel
        if(isChiselLoaded()) {
            try {
                ModuleManager.INSTANCE.addModule(new ChiselModule(EnumModuleTarget.TOOLONLY));
            } catch(Exception e) {
                MuseLogger.logException("Couldn't add Chisel module", e);
            }
        }

        // Applied Energistics
        if (isAppengLoaded()) {
            ModuleManager.INSTANCE.addModule(new AppEngWirelessModule(EnumModuleTarget.TOOLONLY));

            // Extra Cells 2
            if (isExtraCellsLoaded())
                ModuleManager.INSTANCE.addModule(new AppEngWirelessFluidModule(EnumModuleTarget.TOOLONLY));
        }

        // Multi-Mod Compatible OmniProbe
        if (isEnderIOLoaded() || isMFRLoaded() || isRailcraftLoaded()) {
            ModuleManager.INSTANCE.addModule(new OmniProbeModule(EnumModuleTarget.TOOLONLY));
        }

        // TODO: on hold for now. Needs a conditional fiuld tank and handler. May not be worth it.
//        // Compact Machines Personal Shrinking Device
//        if (isCompactMachinesLoaded()) {
//            ModuleManager.INSTANCE.addModule(new PersonalShrinkingModule(Collections.singletonList((IModularItem) MPSItems.INSTANCE.powerFist)));
//        }


        if (isRefinedStorageLoaded()) {
            ModuleManager.INSTANCE.addModule(new RefinedStorageWirelessModule(EnumModuleTarget.TOOLONLY));
        }

        if (isScannableLoaded()) {
            ModuleManager.INSTANCE.addModule(new OreScannerModule(EnumModuleTarget.TOOLONLY));
        }
    }
}
