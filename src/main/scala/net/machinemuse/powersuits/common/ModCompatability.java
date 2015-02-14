package net.machinemuse.powersuits.common;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.powersuits.powermodule.armor.ApiaristArmorModule;
import net.machinemuse.powersuits.powermodule.armor.HazmatModule;
import net.machinemuse.powersuits.powermodule.misc.AirtightSealModule;
import net.machinemuse.powersuits.powermodule.misc.ThaumGogglesModule;
import net.machinemuse.powersuits.powermodule.tool.GrafterModule;
import net.machinemuse.powersuits.powermodule.tool.MFFSFieldTeleporterModule;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.util.Arrays;
import java.util.Collections;

public class ModCompatability {

public static boolean isGregTechLoaded() {
        return Loader.isModLoaded("gregtech");
}

public static boolean isBasicComponentsLoaded() {
        return Loader.isModLoaded("BasicComponents");
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

public static boolean isEnderIOLoaded() {
        return Loader.isModLoaded("EnderIO");
}

public static boolean isMekanismLoaded(){
        return Loader.isModLoaded("Mekanism");
}

public static boolean isForestryLoaded() {
        return Loader.isModLoaded("Forestry");
}

public static boolean isOmniToolsLoaded() {
        return Loader.isModLoaded("OmniTools");
}

public static boolean isSmartMovingLoaded() {
        return Loader.isModLoaded("SmartMoving");
}

public static boolean isRenderPlayerAPILoaded() {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
                return Loader.isModLoaded("RenderPlayerAPI");
        } else {
            // We return true on the server, as RenderPlayerAPI is never necessary on the server (and this should always be called within an isSmartMovingLoaded conditional)
                return true;
        }
}

public static boolean enableThaumGogglesModule() {
        boolean defaultval = isThaumCraftLoaded();
        return Config.getConfig().get("Special Modules", "Thaumcraft Goggles Module", defaultval).getBoolean(defaultval);
}

public static boolean vanillaRecipesEnabled() {
//        boolean defaultval = ((!isBasicComponentsLoaded()) && (!isIndustrialCraftLoaded()) && (!isThermalExpansionLoaded()));
        boolean defaultval = ((! isThermalExpansionLoaded()) && (! isIndustrialCraftLoaded()));
        return Config.getConfig().get("Recipes", "Vanilla Recipes", defaultval).getBoolean(defaultval);
}

private static boolean isAtomicScienceLoaded() {
        return Loader.isModLoaded("AtomicScience");
}

// public static boolean UERecipesEnabled() {
//         boolean defaultval = isBasicComponentsLoaded();
//         return Config.getConfig().get("Recipes", "Universal Electricity Recipes", defaultval).getBoolean(defaultval);
// }

public static boolean IC2RecipesEnabled() {
        boolean defaultval = (isIndustrialCraftLoaded() && (! isGregTechLoaded()) && (! isThermalExpansionLoaded()));
        return Config.getConfig().get("Recipes", "IndustrialCraft Recipes", defaultval).getBoolean(defaultval);
}

public static boolean GregTechRecipesEnabled() {
        boolean defaultval = isGregTechLoaded();
        return Config.getConfig().get("Recipes", "Gregtech Recipes", defaultval).getBoolean(defaultval);
}

public static boolean ThermalExpansionRecipesEnabled() {
        boolean defaultval = (isThermalExpansionLoaded() && (! isGregTechLoaded()));
        return Config.getConfig().get("Recipes", "Thermal Expansion Recipes", defaultval).getBoolean(defaultval);
}

// public static boolean EnderIORecipesEnabled() {
//         boolean defaultval = isEnderIOLoaded();
//         return config.getConfig().get("Recipes", "EnderIO Recipes", defaultval).getBoolean(defaultval);
// }

// public static boolean MekanismRecipesEnabled() {
//         boolean defaultval = isMekanismLoaded();
//         return config.getConfig().get("Recipes", "Mekanism Recipes", defaultval).getBoolean(defaultval);
// }

public static double getUERatio() {
        return Config.getConfig().get("Recipes", "Energy per UEJ", 10.0).getDouble(10.0);
}

public static double getIC2Ratio() {
        return Config.getConfig().get("Recipes", "Energy per IC2 EU", 4.0).getDouble(4.0);
}

public static double getRFRatio() {
        return Config.getConfig().get("Recipes", "Energy per RF", 1.0).getDouble(1.0);
}

// These 2 elements are basically copied from IC2 api
private static Class Ic2Items;

public static ItemStack getIC2Item(String name) {
        try {
                if (Ic2Items == null)
                        Ic2Items = Class.forName("ic2.core.Ic2Items");

                Object ret = Ic2Items.getField(name).get(null);

                if (ret instanceof ItemStack) {
                        return ((ItemStack) ret).copy();
                } else {
                        return null;
                }
        } catch (Exception e) {
                MuseLogger.logError("IC2 API: Call getItem failed for " + name);

                return null;
        }
}

public static ItemStack getGregtechItem(int aIndex, int aAmount, int aMeta) {
        try {
                return (ItemStack) Class.forName("gregtechmod.api.GregTech_API")
                       .getMethod("getGregTechItem", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE})
                       .invoke(null, Integer.valueOf(aIndex), Integer.valueOf(aAmount), Integer.valueOf(aMeta));
        } catch (Exception e) {
        }
        return null;
}

public static void registerModSpecificModules() {
        // Make the IC2 energy ratio show up in config file
        getIC2Ratio();
        getRFRatio();
        getUERatio();

        // Thaumcraft
        if (isThaumCraftLoaded() && enableThaumGogglesModule()) {
                ModuleManager.addModule(new ThaumGogglesModule(Collections.singletonList((IModularItem) MPSItems.powerArmorHead())));
        }

        //IPowerModule module = new MultimeterModule(Collections.singletonList((IModularItem) MPSItems.powerTool()));

        // Atomic Science
        if (isAtomicScienceLoaded()) {
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

        try {
                ModuleManager.addModule(new MFFSFieldTeleporterModule(Collections.singletonList((IModularItem) MPSItems.powerTool())));
        } catch (Throwable e) {
                MuseLogger.logError("Failed to get MFFS item!");
        }
}

//
//    public static ItemStack getForestryItem(String name, int quantity) {
//        try {
//            ItemStack item = forestry.api.core.ItemInterface.getItem(name);
//            if (item != null) {
//                item.stackSize = quantity;
//                return item;
//            }
//        } catch (Exception e) {
//        }
//        MuseLogger.logError("Failed to get Forestry item " + name);
//        return null;
//    }

public static ItemStack getMFFSItem(String name, int quantity) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Object obj = Class.forName("mods.mffs.common.ModularForceFieldSystem").getField("MFFSitemFieldTeleporter").get(null);
        ItemStack stack = new ItemStack((Item) obj, quantity);
        return stack;
}
}
