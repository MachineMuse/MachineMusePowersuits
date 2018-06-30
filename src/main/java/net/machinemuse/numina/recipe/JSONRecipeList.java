//package net.machinemuse.numina.recipe;
//
//import com.google.common.base.Charsets;
//import com.google.common.io.Resources;
//import com.google.gson.Gson;
//import net.machinemuse.numina.general.MuseLogger;
//import net.minecraft.item.crafting.CraftingManager;
//import net.minecraft.item.crafting.IRecipe;
//
//import java.io.*;
//import java.net.URL;
//import java.nio.ByteBuffer;
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 5:54 PM, 11/4/13
// */
//public class JSONRecipeList {
//    static List<JSONRecipe> recipesList = new ArrayList<>();
//    public static final Gson gson = new Gson();
//
//    private static FilenameFilter filter = new FilenameFilter() {
//        @Override
//        public boolean accept(File dir, String name) {
//            return name.endsWith(".recipe") || name.endsWith(".recipes");
//        }
//    };
//
//    public static void loadRecipesFromDir(String dir) {
//        MuseLogger.logDebug("loading recipes");
//        try {
//            File file = new File(dir);
//            if (file.exists()) {
//                if (file.isDirectory()) {
//                    String[] filenames = file.list(filter);
//                    assert filenames != null;
//                    for (String filename : filenames) {
//                        String json = readFile(dir + "/" + filename, Charsets.UTF_8);
//                        MuseLogger.logDebug("Loading recipes from " + filename);
//                        loadRecipesFromString(json);
//                    }
//                } else {
//                    String json = readFile(dir, Charsets.UTF_8);
//                    MuseLogger.logDebug("Loading recipes from " + dir);
//                    loadRecipesFromString(json);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void loadRecipesFromResource(URL resource) {
//        try {
//            String json = Resources.toString(resource, Charsets.UTF_8);
//            loadRecipesFromString(json);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void loadRecipesFromString(String json) {
//        JSONRecipe[] newrecipes = gson.fromJson(json, JSONRecipe[].class);
//        for (JSONRecipe recipe : newrecipes) {
//            if (recipe != null && !recipesList.contains(recipe)) {
//                recipesList.add(recipe);
//                getCraftingRecipeList().add(recipe);
//                MuseLogger.logDebug("Recipe received:" + recipe.result.oredictName + " / " + recipe.result.registryName);
//            } else {
//                assert recipe != null;
//                MuseLogger.logDebug("Recipe duplicate, not added:" + recipe.result.oredictName + " / " + recipe.result.registryName);
//            }
//        }
//    }
//
//    public static List<IRecipe> getCraftingRecipeList() {
//        //noinspection unchecked
//        return CraftingManager.getInstance().getRecipeList();
//    }
//
//    public static List<JSONRecipe> getJSONRecipesList() {
//        return recipesList;
//    }
//
//    static String readFile(String path, Charset encoding)
//            throws IOException {
//        File file = new File(path);
//        DataInputStream is = new DataInputStream(new FileInputStream(file));
//        byte[] bytes = new byte[(int) file.length()];
//        is.readFully(bytes);
//        return encoding.decode(ByteBuffer.wrap(bytes)).toString();
//    }
//}
