package net.machinemuse.numina.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.machinemuse.numina.recipe.JSONRecipe;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by leon on 3/31/16.
 */
public class JEINuminaWrapper extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper {
    @Nonnull
    private final JSONRecipe recipe;

    public JEINuminaWrapper(@Nonnull JSONRecipe recipeIn) {
        System.out.println("Doing something here");
        this.recipe = recipeIn;
        for(Object input : recipe.ingredients) {
            if(input instanceof ItemStack) {
                ItemStack itemStack = (ItemStack)input;
                if(itemStack.stackSize != 1)
                {
                    itemStack.stackSize = 1;
                }
            }
        }
    }

    @Override
    public int getWidth() {
        System.out.println("Doing something here");
        return recipe.getWidth();
    }

    @Override
    public int getHeight() {
        System.out.println("Doing something here");
        return recipe.ingredients.length;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        System.out.println("Doing something here");
    }



//
//    @Override
//    public void getIngredients(IIngredients ingredients) {
//        System.out.println("Doing something here");
//
//
//        List<List<ItemStack>> inputs = new ArrayList<>( recipe.ingredients.length );
//
//        int height = recipe.ingredients.length;
//        int width = recipe.getWidth();
//
//        if (height == 0 || width == 0)
//            return;
//
////        List ret = new ArrayList<>();
//        System.out.println("=================================================================");
//        System.out.println("Recipe for " + this.recipe.result.getRecipeOutput().getUnlocalizedName());
//        for (int y=0; y < height; y++) {
//            if (recipe.ingredients[y] != null) {
//                for (int x=0; x < width; x++) {
//                    List<ItemStack> item;
//                    if(recipe.ingredients[y].length > x) {
//                        item = getIngredient(recipe.ingredients[y][x]);
//                    } else {
////                        item = null;
//                        item = new ArrayList<>();
//                    }
//
//                    if (item == null)
//                        System.out.println("cell " + x + ", " + y + " is NULL");
//                    else if (item.isEmpty())
//                        System.out.println("cell " + x + ", " + y + " is empty");
//                    else
//                        System.out.println("cell " + x + ", " + y + " is " +
//                                item.get(0).getDisplayName());
//
//
//                    if (item != null && item.isEmpty())
//                        return;
//
//
//
//                    inputs.add(item);
//                }
//            }
//        }
//
//        ingredients.setInputLists( ItemStack.class, inputs );
//        ingredients.setOutput( ItemStack.class, this.recipe.getRecipeOutput() );
//    }
//
////    @Nonnull
////    @Override
////    public List<ItemStack> getOutputs()
////    {
////        return Collections.singletonList(recipe.getRecipeOutput());
////    }
//

//
////    @Nonnull
////    @Override
////    public List getInputs() {
////        int height = recipe.ingredients.length;
////        int width = recipe.getWidth();
////
////        if (height == 0 || width == 0)
////            return null;
////
////        List ret = new ArrayList<>();
////        for (int y=0; y < height; y++) {
////            if (recipe.ingredients[y] != null) {
////                for (int x=0; x < width; x++) {
////                    List<ItemStack> item;
////                    if(recipe.ingredients[y].length > x) {
////                        item = getIngredient(recipe.ingredients[y][x]);
////                    } else {
////                        item = null;
////                    }
////                    if (item != null && item.isEmpty())
////                        return null;
////                    ret.add(item);
////                }
////            }
////        }
////        return ret;
////    }
//
//    public static List<ItemStack> getIngredient(SimpleItemMatcher cell)
//    {
//        Boolean shouldbenull = true;
//        List<ItemStack> result = null;
//        if (cell == null) {
//            return null;
//        }
//
//        if (cell.oredictName != null) {
//            shouldbenull = false;
//            result = OreDictionary.getOres(cell.oredictName);
//
//            if (cell.meta != null && result != null && cell.meta != OreDictionary.WILDCARD_VALUE) {
//                ArrayList<ItemStack> t = new ArrayList<ItemStack>();
//                for (ItemStack stack : result)
//                    if (cell.meta == stack.getItemDamage())
//                        t.add(stack);
//                result = t;
//            }
//        }
//
//        if (cell.itemStackName != null) {
//            shouldbenull = false;
//            result = new ArrayList<>();
//
//            ItemStack stack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(cell.itemStackName)), 1);
//            if(stack != null) {
//                stack = stack.copy();
//                if(cell.meta != null) {
//                    stack.setItemDamage(cell.meta);
//                }
//                result.add(stack);
//            }
//        }
//
//        if(cell.registryName != null) {
//            shouldbenull = false;
//            result = new ArrayList<>();
//            Item item = Item.REGISTRY.getObject(new ResourceLocation(cell.registryName));
//            if(item != null) {
//                int newMeta = cell.meta == null ? 0 : cell.meta;
//                ItemStack stack = new ItemStack(item, 1, newMeta);
//                result.add(stack);
//            }
//        }
//
//        if (cell.unlocalizedName != null)
//        {
//            shouldbenull = false;
//            if (result == null) {
//                result = getItemByUnlocalizedName(cell.unlocalizedName);
//            } else {
//                ArrayList<ItemStack> t = new ArrayList<ItemStack>();
//                for (ItemStack stack : result)
//                    if (cell.unlocalizedName.equals(stack.getItem().getUnlocalizedName(stack)))
//                        t.add(stack);
//                result = t;
//            }
//        }
//
//        if (cell.nbtString != null && result != null) {
//            shouldbenull = false;
//            ArrayList<ItemStack> t = new ArrayList<ItemStack>();
//            for (ItemStack stack : result) {
//                ItemStack stack2 = stack.copy();
//                try {
//                    stack2.setTagCompound(JsonToNBT.getTagFromJson(cell.nbtString));
//                } catch (NBTException e) {
//                    e.printStackTrace();
//                }
//                t.add(stack2);
//            }
//            result = t;
//        }
//        // this provides some basic info on an invalid recipe cell
//        if (!shouldbenull && (result == null || result.size() == 0)) {
//            System.out.println("cell should not be null but it is");
//            System.out.println("cell.oredictName: " + cell.oredictName);
//            System.out.println("cell.itemStackName: " + cell.itemStackName);
//            System.out.println("cell.registryName: "+ cell.registryName);
//            System.out.println("cell.unlocalizedName: " + cell.unlocalizedName);
//            System.out.println("cell.nbtString: " + cell.nbtString);
//        }
//        return result;
//    }
//
//    private static Map<String, ArrayList<ItemStack>> itemMap;
//    public static ArrayList<ItemStack> getItemByUnlocalizedName(String unlocalizedName)
//    {
//        ArrayList<ItemStack> result = new ArrayList<>();
//        if (itemMap == null)
//        {
//            ItemStack stack = ItemNameMappings.getItem(unlocalizedName);
//            if (stack != null)
//                result.add(stack);
//        }
//        if (itemMap != null && itemMap.containsKey(unlocalizedName)){
//            result.addAll(itemMap.get(unlocalizedName));
//        }
//        return result;
//    }
}