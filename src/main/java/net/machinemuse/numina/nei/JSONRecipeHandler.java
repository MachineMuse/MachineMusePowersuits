//package net.machinemuse.numina.nei;
//
//import codechicken.nei.ItemList;
//import codechicken.nei.NEIServerUtils;
//import codechicken.nei.recipe.ShapedRecipeHandler;
//import cpw.mods.fml.common.registry.GameRegistry;
//import net.machinemuse_old.numina.recipe.ItemNameMappings;
//import net.machinemuse_old.numina.recipe.JSONRecipe;
//import net.machinemuse_old.numina.recipe.SimpleItemMatcher;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.crafting.CraftingManager;
//import net.minecraft.item.crafting.IRecipe;
//import net.minecraft.nbt.JsonToNBT;
//import net.minecraft.nbt.NBTException;
//import net.minecraft.nbt.NBTTagCompound;
//
//import net.minecraftforge.oredict.OreDictionary;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class JSONRecipeHandler extends ShapedRecipeHandler {
//
//    @Override
//    public String getRecipeName()
//    {
//        return I18n.format("nei.jsonShaped");
//    }
//
//    @Override
//    public void loadCraftingRecipes(String outputId, Object... results)
//    {
//        if(outputId.equals("crafting") && getClass() == JSONRecipeHandler.class)
//        {
//            @SuppressWarnings("unchecked") List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
//            for(IRecipe irecipe : allrecipes)
//            {
//                CachedShapedRecipe recipe = null;
//                if(irecipe instanceof JSONRecipe)
//                    recipe = JSONShapedRecipe((JSONRecipe) irecipe);
//
//                if(recipe == null)
//                    continue;
//
//                recipe.computeVisuals();
//                arecipes.add(recipe);
//            }
//        }
//        else
//        {
//            super.loadCraftingRecipes(outputId, results);
//        }
//    }
//
//    @Override
//    public void loadCraftingRecipes(ItemStack result)
//    {
//        @SuppressWarnings("unchecked") List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
//        for(IRecipe irecipe : allrecipes)
//        {
//            if(NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result))
//            {
//                CachedShapedRecipe recipe = null;
//                if(irecipe instanceof JSONRecipe)
//                    recipe = JSONShapedRecipe((JSONRecipe) irecipe);
//
//                if(recipe == null)
//                    continue;
//
//                recipe.computeVisuals();
//                arecipes.add(recipe);
//            }
//        }
//    }
//
//    @Override
//    public void loadUsageRecipes(ItemStack ingredient)
//    {
//        @SuppressWarnings("unchecked") List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
//        for(IRecipe irecipe : allrecipes)
//        {
//            CachedShapedRecipe recipe = null;
//            if(irecipe instanceof JSONRecipe)
//                recipe = JSONShapedRecipe((JSONRecipe) irecipe);
//
//            if(recipe == null || !recipe.contains(recipe.ingredients, ingredient))
//                continue;
//
//            recipe.computeVisuals();
//            if(recipe.contains(recipe.ingredients, ingredient))
//            {
//                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
//                arecipes.add(recipe);
//            }
//        }
//    }
//
//    private static Map<String, ArrayList<ItemStack>> itemMap;
//
//    public static ArrayList<ItemStack> getItemByUnlocalizedName(String unlocalizedName)
//    {
//        ArrayList<ItemStack> result = new ArrayList<>();
//        if (itemMap == null)
//        {
//            if (ItemList.item.isEmpty())
//            {
//                ItemStack stack = ItemNameMappings.getItem(unlocalizedName);
//                if (stack != null)
//                    result.add(stack);
//            } else {
//                itemMap = new HashMap<>();
//                for (ItemStack stack : ItemList.item)
//                {
//                    String key = stack.getItem().getUnlocalizedName(stack);
//                    if (!itemMap.containsKey(key))
//                        itemMap.put(key, new ArrayList<ItemStack>());
//                    itemMap.get(key).add(stack);
//                }
//            }
//        }
//        if (itemMap != null && itemMap.containsKey(unlocalizedName)){
//            result.addAll(itemMap.get(unlocalizedName));
//        }
//        return result;
//    }
//
//    public static List<ItemStack> getIngredient(SimpleItemMatcher cell)
//    {
//        List<ItemStack> result = null;
//        if (cell == null)
//            return null;
//
//        if (cell.oredictName != null) {
//            result = OreDictionary.getOres(cell.oredictName);
//
//            if (cell.meta != null && result != null && cell.meta != OreDictionary.WILDCARD_VALUE) {
//                ArrayList<ItemStack> t = new ArrayList<>();
//                for (ItemStack stack : result)
//                    if (cell.meta == stack.getItemDamage())
//                        t.add(stack);
//                result = t;
//            }
//        }
//
//        if (cell.itemStackName != null) {
//            String[] names = cell.itemStackName.split(":");
//            result = new ArrayList<>();
//            ItemStack stack = GameRegistry.findItemStack(names[0], names[1], 1);
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
//            String[] names = cell.registryName.split(":");
//            result = new ArrayList<>();
//            Item item = GameRegistry.findItem(names[0], names[1]);
//            if(item != null) {
//                int newMeta = cell.meta == null ? 0 : cell.meta;
//                ItemStack stack = new ItemStack(item, 1, newMeta);
//                result.add(stack);
//            }
//        }
//
//        if (cell.unlocalizedName != null)
//        {
//            if (result == null) {
//                result = getItemByUnlocalizedName(cell.unlocalizedName);
//            } else {
//                ArrayList<ItemStack> t = new ArrayList<>();
//                for (ItemStack stack : result)
//                    if (cell.unlocalizedName.equals(stack.getItem().getUnlocalizedName(stack)))
//                        t.add(stack);
//                result = t;
//            }
//        }
//
//
//
//        if (cell.nbtString != null && result != null) {
//            ArrayList<ItemStack> t = new ArrayList<>();
//            for (ItemStack stack : result) {
//                ItemStack stack2 = stack.copy();
//                try {
//                    stack2.setTagCompound((NBTTagCompound) JsonToNBT.func_150315_a(cell.nbtString));
//                } catch (NBTException e) {
//                    e.printStackTrace();
//                }
//                t.add(stack2);
//            }
//            result = t;
//        }
//
//        return result;
//    }
//
//    public CachedShapedRecipe JSONShapedRecipe(JSONRecipe recipe)
//    {
//        int height = recipe.ingredients.length;
//        int width = recipe.getWidth();
//        if (height == 0 || width == 0)
//            return null;
//
//        Object[] item = new Object[height*width];
//
//        for (int y=0; y < height; y++) {
//            if (recipe.ingredients[y] != null) {
//                for (int x=0; x < width; x++) {
//                    List<ItemStack> item;
//                    if(recipe.ingredients[y].length > x) {
//                        item = getIngredient(recipe.ingredients[y][x]);
//                    } else {
//                        item = null;
//                    }
//                    if (item != null && item.isEmpty())
//                        return null;
//                    item[y * width + x] = item;
//                }
//            }
//        }
//
//        return new CachedShapedRecipe(width, height, item, recipe.getRecipeOutput());
//    }
//}
