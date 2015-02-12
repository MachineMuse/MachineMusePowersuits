package net.machinemuse.powersuits.common;

import net.machinemuse.numina.general.MuseLogger;
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
    		try {
    		    String recipeJarPath = "/assets/powersuits/recipes";
                if (ModCompatability.vanillaRecipesEnabled()) {
                    File vanilla = new File(path, "vanilla.recipes");
                    if (!vanilla.isFile()) {
                        FileUtils.copyURLToFile(MPSRecipeManager.class.getResource(recipeJarPath + "/vanilla.recipes"), vanilla);
                    }
                    JSONRecipeList.loadRecipesFromFile(vanilla);
                }
                // Universal Electricity/Resonant Induction/Electrodynamics reintroduction postponed until Calclavia gets his act together....
                // if (ModCompatability.isBasicComponentsLoaded()) {
                //     if (ModCompatability.UERecipesEnabled()) {
                //         File ue = new File(path, "UniversalElectricity.recipes");
                //         if (!ue.isFile()) {
                //             FileUtils.copyURLToFile(MPSRecipeManager.class.getResource(recipeJarPath + "/UniversalElectricity.recipes"), ue);
                //         }
                //         JSONRecipeList.loadRecipesFromFile(ue);
                //     }
                // }
                // EnderIO support planned...
                // if (ModCompatability.isEnderIOLoaded()) {
                //     if (ModCompatability.EnderIORecipesEnabled()) {
                //         File eio = new File(path, "EnderIO.recipes");
                //         if (!eio.isFile()) {
                //             FileUtils.copyURLToFile(MPSRecipeManager.class.getResource(recipeJarPath + "/EnderIO.recipes"), eio);
                //         }
                //         JSONRecipeList.loadRecipesFromFile(eio);
                //     }
                // }
                // Mekanism support planned...
                // if (ModCompatability.isMekanismLoaded()) {
                //     if (ModCompatability.MekanismRecipesEnabled()) {
                //         File mk = new File(path, "Mekanism.recipes");
                //         if (!mk.isFile()) {
                //             FileUtils.copyURLToFile(MPSRecipeManager.class.getResource(recipeJarPath + "/Mekanism.recipes"), mk);
                //         }
                //         JSONRecipeList.loadRecipesFromFile(mk);
                //     }
                // }
                if (ModCompatability.isIndustrialCraftLoaded()) {
    				if (ModCompatability.IC2RecipesEnabled()) {
	        		    File ic2 = new File(path, "IndustrialCraft2.recipes");
	            		if (!ic2.isFile()) {
	                		FileUtils.copyURLToFile(MPSRecipeManager.class.getResource(recipeJarPath + "/IndustrialCraft2.recipes"), ic2);
	            		}
	            		JSONRecipeList.loadRecipesFromFile(ic2);
    				}
	        		if (ModCompatability.isGregTechLoaded()) {
	        		    if (ModCompatability.GregTechRecipesEnabled()) {
		                    File gt = new File(path, "GregTech.recipes");
		            		if (!gt.isFile()) {
		                		FileUtils.copyURLToFile(MPSRecipeManager.class.getResource(recipeJarPath + "/GregTech.recipes"), gt);
		            		}
		            		JSONRecipeList.loadRecipesFromFile(gt);
	        		    }
	        		}
        		}
                if (ModCompatability.isThermalExpansionLoaded()) {
                    if (ModCompatability.ThermalExpansionRecipesEnabled()) {
                        File te = new File(path, "ThermalExpansion.recipes");
                        if (!te.isFile()) {
                            FileUtils.copyURLToFile(MPSRecipeManager.class.getResource(folder + "/ThermalExpansion.recipes"), te);
                        }
                        JSONRecipeList.loadRecipesFromFile(te);
                    }
                }
                Config.getConfig().save();
                isLoaded = true;
            } catch (IOException e) {
            	MuseLogger.logError("Unable to access and/or generate recipes!");
            	MuseLogger.logError("Please check your permissions for the following directory: " + path);
            	e.printStackTrace();
            }
    	}
    }
}