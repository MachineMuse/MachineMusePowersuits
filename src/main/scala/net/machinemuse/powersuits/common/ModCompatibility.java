package net.machinemuse.powersuits.common;

import cpw.mods.fml.common.Loader;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.powersuits.powermodule.armor.ApiaristArmorModule;
import net.machinemuse.powersuits.powermodule.armor.HazmatModule;
import net.machinemuse.powersuits.powermodule.misc.AirtightSealModule;
import net.machinemuse.powersuits.powermodule.misc.ThaumGogglesModule;
import net.machinemuse.powersuits.powermodule.tool.ChiselModule;
import net.machinemuse.powersuits.powermodule.tool.GrafterModule;
import net.minecraftforge.common.config.Configuration;

import java.util.Arrays;
import java.util.Collections;

public class ModCompatibility {

    public static boolean isGregTechLoaded() {
        return Loader.isModLoaded("gregtech_addon");
    }

    public static boolean isIndustrialCraftLoaded() {
        return Loader.isModLoaded("IC2");
    }

    public static boolean isThaumCraftLoaded() {
        return Loader.isModLoaded("Thaumcraft");
    }

    public static boolean isThermalExpansionLoaded() {
        return Loader.isModLoaded("ThermalExpansion");
    }

    public static boolean isGalacticraftLoaded() {
        return Loader.isModLoaded("GalacticraftCore");
    }

    public static boolean isCoFHCoreLoaded() {
        return Loader.isModLoaded("CoFHCore");
    }

    public static boolean isForestryLoaded() {
        return Loader.isModLoaded("Forestry");
    }

    public static boolean isChiselLoaded() {
        return Loader.isModLoaded("chisel");
    }

    public static boolean isEnderIOLoaded() {
        return Loader.isModLoaded("EnderIO");
    }

    public static boolean enableThaumGogglesModule() {
        boolean defaultval = isThaumCraftLoaded();
        return Config.getConfig().get("Special Modules", "Thaumcraft Goggles Module", defaultval).getBoolean(defaultval);
    }

    public static double getIC2Ratio() {
        return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Energy per IC2 EU", 0.4).getDouble(0.4);
    }

    public static double getRFRatio() {
        return Config.getConfig().get(Configuration.CATEGORY_GENERAL, "Energy per RF", 0.1).getDouble(0.1);
    }

    public static void registerModSpecificModules() {
        // Make the energy ratios show up in config file
        getIC2Ratio();
        getRFRatio();

        // Thaumcraft
        if (isThaumCraftLoaded() && enableThaumGogglesModule()) {
            ModuleManager.addModule(new ThaumGogglesModule(Collections.singletonList((IModularItem) MPSItems.powerArmorHead())));
        }

        //IPowerModule module = new MultimeterModule(Collections.singletonList((IModularItem) MPSItems.powerTool()));

        // Hazmat
        if (isIndustrialCraftLoaded()) {
            ModuleManager.addModule(new HazmatModule(Arrays.<IModularItem>asList(MPSItems.powerArmorHead(), MPSItems.powerArmorTorso(), MPSItems.powerArmorLegs(), MPSItems.powerArmorFeet())));
        }

        // Galacticraft
        if (isGalacticraftLoaded()) {
            ModuleManager.addModule(new AirtightSealModule(Collections.singletonList((IModularItem) MPSItems.powerArmorHead())));
        }

        // Forestry
        if (isForestryLoaded()) {
            ModuleManager.addModule(new GrafterModule(Collections.singletonList((IModularItem) MPSItems.powerTool())));
            ModuleManager.addModule(new ApiaristArmorModule(Arrays.<IModularItem>asList(MPSItems.powerArmorHead(), MPSItems.powerArmorTorso(), MPSItems.powerArmorLegs(), MPSItems.powerArmorFeet())));
        }

        // Chisel
        if(isChiselLoaded()) {
            try {
                ModuleManager.addModule(new ChiselModule(Collections.singletonList((IModularItem) MPSItems.powerTool())));
            } catch(Exception e) {
                MuseLogger.logException("Couldn't add Chisel module", e);
            }
        }
    }

}
