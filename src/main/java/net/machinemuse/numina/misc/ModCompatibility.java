package net.machinemuse.numina.misc;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModAPIManager;
import net.minecraftforge.fml.common.ModContainer;

import java.util.List;

public class ModCompatibility {
    public static boolean isRFAPILoaded() {
        return ModAPIManager.INSTANCE.hasAPI("redstoneflux");
    }

    public static boolean isCOFHCoreLoaded() {
//        return ModAPIManager.INSTANCE.hasAPI("cofhcore");
        return Loader.isModLoaded("cofhcore");
    }

    public static boolean isCOFHLibLoaded() {
        return ModAPIManager.INSTANCE.hasAPI("cofhlib");
    }

    public static boolean isThermalExpansionLoaded() {
        return Loader.isModLoaded("thermalexpansion") && Loader.isModLoaded("thermalfoundation");
    }

    public static boolean isEnderCoreLoaded() {
        return Loader.isModLoaded("endercore");
    }

    public static boolean isEnderIOLoaded() {
        return Loader.isModLoaded("enderio");
    }

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

    public static boolean isGalacticraftLoaded() {
        return Loader.isModLoaded("galacticraftcore");
    }

    public static boolean isForestryLoaded() {
        return Loader.isModLoaded("forestry");
    }

    public static boolean isChiselLoaded() {
        return Loader.isModLoaded("chisel");
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
        return Loader.isModLoaded("mekanism");
    }
}
