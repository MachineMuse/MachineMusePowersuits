//package net.machinemuse.numina.recipe.jei;
//
//import mezz.jei.api.ingredients.IIngredients;
//import mezz.jei.api.recipe.BlankRecipeWrapper;
//import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
//import net.machinemuse.numina.utils.MuseLogger;
//import net.machinemuse.numina.recipe.JSONRecipe;
//import net.minecraft.item.ItemStack;
//
//import javax.annotation.Nonnull;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Numina by MachineMuse
// * Created by lehjr on 3/3/17.
// */
//public class JEINuminaWrapper extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper {
//    @Nonnull
//    private JSONRecipe recipe;
//
//    public JEINuminaWrapper(@Nonnull JSONRecipe recipeIn) {
//        this.recipe = recipeIn;
//        for(Object input : recipe.ingredients) {
//            if(input instanceof ItemStack) {
//                ItemStack itemStack = (ItemStack)input;
//                if(itemStack.stackSize != 1)
//                {
//                    itemStack.stackSize = 1;
//                }
//            }
//        }
//    }
//
//    @Override
//    public void getIngredients(IIngredients ingredients) {
//        List<ItemStack> inputs = new ArrayList<>();
//        int height = recipe.ingredients.length;
//        int width = recipe.getWidth();
//
//        if (height == 0 || width == 0)
//            return;
//        if (this.recipe.result != null)
//            MuseLogger.logDebug("Recipe for " + this.recipe.result.getRecipeOutput().getUnlocalizedName());
//        else
//            MuseLogger.logDebug("Recipe output ItemStack is NULL!!");
//        for (int getY=0; getY < height; getY++) {
//            if (recipe.ingredients[getY] != null) {
//                for (int getX=0; getX < width; getX++) {
//                    List<ItemStack> itemStacks = new ArrayList<>();
//                    if(recipe.ingredients[getY].length > getX) itemStacks = recipe.getIngredient(recipe.ingredients[getY][getX]);
//                    if (itemStacks == null) {
//                        MuseLogger.logDebug("cell " + getX + ", " + getY + " is NULL");
//                        inputs.add(null);
//                    } else if (itemStacks.isEmpty()) {
//                        recipe.setIsValid(false);
//                    } else {
//                        inputs.add(itemStacks.get(0));
//                        MuseLogger.logDebug("cell " + getX + ", " + getY + " is: " + itemStacks.get(0).getDisplayName());
//                    }
//                }
//            }
//        }
//
//        ingredients.setInputs( ItemStack.class, inputs);
//        ingredients.setOutput( ItemStack.class, this.recipe.getRecipeOutput() );
//    }
//
////    private static Map<String, ArrayList<ItemStack>> itemMap;
////    public static ArrayList<ItemStack> getItemByUnlocalizedName(String unlocalizedName) {
////        ArrayList<ItemStack> result = new ArrayList<>();
////        if (itemMap == null)
////        {
////            ItemStack stack = ItemNameMappings.getItem(unlocalizedName);
////            if (stack != null)
////                result.add(stack);
////        }
////        if (itemMap != null && itemMap.containsKey(unlocalizedName)){
////            result.addAll(itemMap.get(unlocalizedName));
////        }
////        return result;
////    }
//
//    @Override
//    public int getWidth() {
//        return recipe.getWidth();
//    }
//
//    @Override
//    public int getHeight() {
//        return recipe.ingredients.length;
//    }
//}
