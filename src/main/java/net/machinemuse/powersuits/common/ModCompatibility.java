package net.machinemuse.powersuits.common;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.powersuits.powermodule.armor.ApiaristArmorModule;
import net.machinemuse.powersuits.powermodule.armor.HazmatModule;
import net.machinemuse.powersuits.powermodule.misc.AirtightSealModule;
import net.machinemuse.powersuits.powermodule.misc.ThaumGogglesModule;
import net.machinemuse.powersuits.powermodule.tool.*;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModAPIManager;

import java.util.Arrays;
import java.util.Collections;

public class ModCompatibility {
    public static boolean isTechRebornLoaded() {
        return Loader.isModLoaded("techreborn");
    }

    public static boolean isGregTechLoaded() {
        return Loader.isModLoaded("gregtech");
    }

    public static boolean isIndustrialCraftLoaded() {
        return Loader.isModLoaded("IC2");
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
        return Config.getConfig().get("Special Modules", "Thaumcraft Goggles Module", defaultval).getBoolean(defaultval);
    }

    // 1 MJ = 2.5 EU
    // 1 EU = 0.4 MJ
    public static double getIC2Ratio() {
        return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Energy per IC2 EU", 0.4).getDouble(0.4);
    }

    // 1 MJ = 10 RF
    // 1 RF = 0.1 MJ
    public static double getRFRatio() {
        return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Energy per RF", 0.1).getDouble(0.1);
    }

    // (Refined Storage) 1 RS = 1 RF
    public static double getRSRatio() {
        return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Energy per RS", 0.1).getDouble(0.1);
    }

    // 1 MJ = 5 AE
    // 1 AE = 0.2 MJ
    public static double getAE2Ratio() {
        return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Energy per AE", 0.2).getDouble(0.2);
    }

    public static void registerModSpecificModules() {
        // Make the energy ratios show up in config file
        getIC2Ratio();
        getRFRatio();
        getRSRatio();

        // CoFH Lib - CoFHLib is included in CoFHCore
        if (isCOFHCoreLoaded()) {
            ModuleManager.addModule(new OmniWrenchModule(Collections.singletonList((IModularItem) MPSItems.powerTool)));
        }

        // Thaumcraft
        if (isThaumCraftLoaded() && enableThaumGogglesModule()) {
            ModuleManager.addModule(new ThaumGogglesModule(Collections.singletonList((IModularItem) MPSItems.powerArmorHead)));
        }

        //IPowerModule module = new MultimeterModule(Collections.singletonList((IModularItem) MPSItems.powerTool()));

        // Hazmat
        if (isIndustrialCraftLoaded()) {
            ModuleManager.addModule(new HazmatModule(Arrays.<IModularItem>asList((IModularItem)MPSItems.powerArmorHead, (IModularItem)MPSItems.powerArmorTorso, (IModularItem)MPSItems.powerArmorLegs, (IModularItem)MPSItems.powerArmorFeet)));
            ModuleManager.addModule(new TreetapModule(Collections.singletonList((IModularItem)MPSItems.powerTool)));
        }

        // Galacticraft
        if (isGalacticraftLoaded()) {
            ModuleManager.addModule(new AirtightSealModule(Collections.singletonList((IModularItem) MPSItems.powerArmorHead)));
        }

        // Forestry
        if (isForestryLoaded()) {
            ModuleManager.addModule(new GrafterModule(Collections.singletonList((IModularItem) MPSItems.powerTool)));
            ModuleManager.addModule(new ScoopModule(Collections.singletonList((IModularItem) MPSItems.powerTool)));
            ModuleManager.addModule(new ApiaristArmorModule(Arrays.<IModularItem>asList((IModularItem)MPSItems.powerArmorHead, (IModularItem)MPSItems.powerArmorTorso, (IModularItem)MPSItems.powerArmorLegs, (IModularItem)MPSItems.powerArmorFeet)));
        }

        // Chisel
        if(isChiselLoaded()) {
            try {
                ModuleManager.addModule(new ChiselModule(Collections.singletonList((IModularItem) MPSItems.powerTool)));
            } catch(Exception e) {
                MuseLogger.logException("Couldn't add Chisel module", e);
            }
        }

        // Applied Energistics
        if (isAppengLoaded()) {
            ModuleManager.addModule(new AppEngWirelessModule(Collections.singletonList((IModularItem) MPSItems.powerTool)));

//            // Extra Cells 2
//            if (isExtraCellsLoaded())
//                ModuleManager.addModule(new AppEngWirelessFluidModule(Collections.singletonList((IModularItem) MPSItems.powerTool)));
        }

        // Multi-Mod Compatible OmniProbe
        if (isEnderIOLoaded() || isMFRLoaded() || isRailcraftLoaded()) {
            ModuleManager.addModule(new OmniProbeModule(Collections.singletonList((IModularItem) MPSItems.powerTool)));
        }

        // TODO: on hold for now. Needs a conditional fiuld tank and handler. May not be worth it.
//        // Compact Machines Personal Shrinking Device
//        if (isCompactMachinesLoaded()) {
//            ModuleManager.addModule(new PersonalShrinkingModule(Collections.singletonList((IModularItem) MPSItems.powerTool)));
//        }


        if (isRefinedStorageLoaded()) {
            ModuleManager.addModule(new RefinedStorageWirelessModule(Collections.singletonList((IModularItem) MPSItems.powerTool)));
        }

//        if (isScannableLoaded()) {
//            ModuleManager.addModule(new ScannableModule(Collections.singletonList((IModularItem) MPSItems.powerTool)));
//        }
    }
}
