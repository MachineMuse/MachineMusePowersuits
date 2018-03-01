package net.machinemuse.powersuits.common;

import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.common.config.NuminaConfig;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.item.module.environmental.AirtightSealModule;
import net.machinemuse.powersuits.item.module.environmental.ApiaristArmorModule;
import net.machinemuse.powersuits.item.module.environmental.HazmatModule;
import net.machinemuse.powersuits.item.module.tool.*;
import net.machinemuse.powersuits.item.module.vision.NightVisionModule;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModAPIManager;
import net.minecraftforge.fml.common.ModContainer;

import java.util.List;

public class ModCompatibility {
    public static boolean isTechRebornLoaded() {
        return Loader.isModLoaded("techreborn");
    }

    public static boolean isGregTechLoaded() {
        return Loader.isModLoaded("gregtech");
    }

    // Industrialcraft common
    public static boolean isIndustrialCraftLoaded() {
        return Loader.isModLoaded("IC2");
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
        return Loader.isModLoaded("Thaumcraft");
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
        return Loader.isModLoaded("EnderIO");
    }

    public static boolean isAppengLoaded() {
        return Loader.isModLoaded("appliedenergistics2");
    }

    public static boolean isExtraCellsLoaded() {
        return Loader.isModLoaded("extracells");
    }

    public static boolean isMFRLoaded() {
        return Loader.isModLoaded("MineFactoryReloaded");
    }

    public static boolean isRailcraftLoaded() {
        return Loader.isModLoaded("Railcraft");
    }

    public static boolean isCompactMachinesLoaded() {
        return Loader.isModLoaded("cm2");
    }

    public static boolean isRenderPlayerAPILoaded() {
        return Loader.isModLoaded("RenderPlayerAPI");
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
        return Loader.isModLoaded("Mekanism");
    }

    public static boolean enableThaumGogglesModule() {
        boolean defaultval = isThaumCraftLoaded();
        return MPSConfig.getInstance().getModuleAllowedorDefault(MPSModuleConstants.MODULE_THAUM_GOGGLES, defaultval);
    }

    // 1MJ (MPS) = 1 MJ (Mekanism)
    public static double getMekRatio() {
        return NuminaConfig.getMekRatio();
    }

    // 1 MJ = 2.5 EU
    // 1 EU = 0.4 MJ
    public static double getIC2Ratio() {
        return NuminaConfig.getIC2Ratio();
    }

    // 1 MJ = 10 RF
    // 1 RF = 0.1 MJ
    public static double getRFRatio() {
        return 1;
    }

    // (Refined Storage) 1 RS = 1 RF
    public static double getRSRatio() {
        return NuminaConfig.getRSRatio();
    }

    // 1 MJ = 5 AE
    // 1 AE = 0.2 MJ
    public static double getAE2Ratio() {
        return NuminaConfig.getAE2Ratio();
    }

    public static void registerModSpecificModules() {
        // Make the energy ratios show up in config file
        getIC2Ratio();
        getRFRatio();
        getRSRatio();

        // CoFH Lib - CoFHLib is included in CoFHCore
        if (isCOFHCoreLoaded()) {
            ModuleManager.getInstance().addModule(new OmniWrenchModule("omniwrench", MPSModuleConstants.MODULE_OMNI_WRENCH));
        }

        // Thaumcraft
        if (isThaumCraftLoaded() && enableThaumGogglesModule()) {
            ModuleManager.getInstance().addModule(new NightVisionModule("night_vision", MPSModuleConstants.MODULE_NIGHT_VISION));
        }

        //IModule module = new MultimeterModule(Collections.singletonList((IMuseItem) MPSItems.powerfist()));

        // Industrialcraft
        if (isIndustrialCraftLoaded()) {
            ModuleManager.getInstance().addModule(new HazmatModule("hazmat", MPSModuleConstants.MODULE_HAZMAT));
            ModuleManager.getInstance().addModule(new TreetapModule("tree_tap", MPSModuleConstants.MODULE_TREETAP));
        }

        // Galacticraft
        if (isGalacticraftLoaded()) {
            ModuleManager.getInstance().addModule(new AirtightSealModule("airtight_seal", MPSModuleConstants.AIRTIGHT_SEAL_MODULE));
        }

        // Forestry
        if (isForestryLoaded()) {
            ModuleManager.getInstance().addModule(new GrafterModule("grafter", MPSModuleConstants.MODULE_GRAFTER));
            ModuleManager.getInstance().addModule(new ScoopModule("scoop", MPSModuleConstants.MODULE_SCOOP));
            ModuleManager.getInstance().addModule(new ApiaristArmorModule("apiarist_armor", MPSModuleConstants.MODULE_APIARIST_ARMOR));
        }

        // Chisel
        if(isChiselLoaded()) {
            try {
                ModuleManager.getInstance().addModule(new ChiselModule("chisel", MPSModuleConstants.MODULE_CHISEL));
            } catch(Exception e) {
                MuseLogger.logException("Couldn't add Chisel module", e);
            }
        }

        // Applied Energistics
        if (isAppengLoaded()) {
            ModuleManager.getInstance().addModule(new AppEngWirelessModule("appeng_wireless", MPSModuleConstants.MODULE_APPENG_WIRELESS));


//            // Extra Cells 2
//            if (isExtraCellsLoaded())
//                ModuleManager.addModule(new AppEngWirelessFluidModule(Collections.singletonList((IMuseItem) MPSItems.powerfist)));
//        addModule(new AppEngWirelessFluidModule("appeng_ec_wireless_fluid", MPSModuleConstants.MODULE_APPENG_EC_WIRELESS_FLUID));
//
        }

        // Multi-Mod Compatible OmniProbe
        if (isEnderIOLoaded() || isMFRLoaded() || isRailcraftLoaded()) {
            ModuleManager.getInstance().addModule(new OmniProbeModule("omniprobe", MPSModuleConstants.MODULE_OMNIPROBE));
        }

        // TODO: on hold for now. Needs a conditional fiuld tank and handler. May not be worth it.
//        // Compact Machines Personal Shrinking Device
//        if (isCompactMachinesLoaded()) {
//            ModuleManager.addModule(new PersonalShrinkingModule(Collections.singletonList((IMuseItem) MPSItems.powerfist)));
//        }


        if (isRefinedStorageLoaded()) {
            ModuleManager.getInstance().addModule(new RefinedStorageWirelessModule("refinedstoragewirelessgrid", MPSModuleConstants.MODULE_REF_STOR_WIRELESS));
        }

//        if (isScannableLoaded()) {
//            ModuleManager.addModule(new ScannableModule(Collections.singletonList((IMuseItem) MPSItems.powerfist)));
//        }
    }
}
