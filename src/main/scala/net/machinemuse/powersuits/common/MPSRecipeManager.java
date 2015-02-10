package net.machinemuse.powersuits.common;

import net.machinemuse.numina.recipe.JSONRecipeList;
import net.machinemuse.powersuits.common.ModCompatability;
import org.apache.commons.io.FileUtils;
import java.io.*;

public class MPSRecipeManager {
    static private boolean isLoaded;
    
    static {
    	isLoaded = false;
    }
    
    public static void loadOrPutRecipesFromJar(String path) {
    	if (!isLoaded) {
            if (ModCompatability.vanillaRecipesEnabled()) {
                File vanilla = new File(path, "vanilla.recipes");
                if (!vanilla.isFile()) {
                    FileUtils.copyURLToFile(MPSRecipeManager.class.getResource("/vanilla.recipes"), vanilla);
                }
                JSONRecipeList.loadRecipesFromFile(vanilla);
            }
            // Universal Electricity/Resonant Induction/Electrodynamics reintroduction postponed until Calclavia gets his act together....
            // if (ModCompatability.UERecipesEnabled() && ModCompatability.isBasicComponentsLoaded()) {
            //     File ue = new File(path, "UniversalElectricity.recipes");
            //     if (!ue.isFile()) {
            //         FileUtils.copyURLToFile(getClass().getResource("/UniversalElectricity.recipes"), ue);
            //     }
            //     JSONRecipeList.loadRecipesFromFile(ue);
            // }
            // EnderIO support planned...
            // if (ModCompatability.EnderIORecipesEnabled() && ModCompatability.isEnderIOLoaded()) {
            //     File eio = new File(path, "EnderIO.recipes");
            //     if (!eio.isFile()) {
            //         FileUtils.copyURLToFile(getClass().getResource("/EnderIO.recipes"), eio);
            //     }
            //     JSONRecipeList.loadRecipesFromFile(eio);
            // }
            // Mekanism support planned...
            // if (ModCompatability.MekanismRecipesEnabled() && ModCompatability.isMekanismLoaded()) {
            //     File mk = new File(path, "Mekanism.recipes");
            //     if (!mk.isFile()) {
            //         FileUtils.copyURLToFile(getClass().getResource("/Mekanism.recipes"), mk);
            //     }
            //     JSONRecipeList.loadRecipesFromFile(mk);
            // }
            if (ModCompatability.isIndustrialCraftLoaded()) {
        		if (ModCompatability.IC2RecipesEnabled()) {
		            File ic2 = new File(path, "IndustrialCraft2.recipes");
		            if (!ic2.isFile()) {
		                FileUtils.copyURLToFile(MPSRecipeManager.class.getResource("/IndustrialCraft2.recipes"), ic2);
		            }
		            JSONRecipeList.loadRecipesFromFile(ic2);
        		}
		        if (ModCompatability.GregTechRecipesEnabled() && ModCompatability.isGregTechLoaded()) {
		            File gt = new File(path, "GregTech.recipes");
		            if (!gt.isFile()) {
		                FileUtils.copyURLToFile(MPSRecipeManager.class.getResource("/GregTech.recipes"), gt);
		            }
		            JSONRecipeList.loadRecipesFromFile(gt);
		        }
        }
            if (ModCompatability.ThermalExpansionRecipesEnabled() && ModCompatability.isThermalExpansionLoaded()) {
                File te = new File(path, "ThermalExpansion.recipes");
                if (!te.isFile()) {
                    FileUtils.copyURLToFile(MPSRecipeManager.class.getResource("/ThermalExpansion.recipes"), te);
                }
                JSONRecipeList.loadRecipesFromFile(te);
            }
            isLoaded = true;
    	}
    }
}