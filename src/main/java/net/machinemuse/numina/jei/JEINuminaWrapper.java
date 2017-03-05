package net.machinemuse.numina.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.recipe.ItemNameMappings;
import net.machinemuse.numina.recipe.JSONRecipe;
import net.machinemuse.numina.recipe.SimpleItemMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Numina by MachineMuse
 * Created by lehjr on 3/3/17.
 */
public class JEINuminaWrapper extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper {
    @Nonnull
    private final JSONRecipe recipe;

    public JEINuminaWrapper(@Nonnull JSONRecipe recipeIn) {
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
    public void getIngredients(IIngredients ingredients) {
        List<ItemStack> otherList = new ArrayList<>();
        int height = recipe.ingredients.length;
        int width = recipe.getWidth();

        if (height == 0 || width == 0)
            return;
        if (this.recipe.result != null)
            MuseLogger.logDebug("Recipe for " + this.recipe.result.getRecipeOutput().getUnlocalizedName());
        else
            MuseLogger.logDebug("Recipe output ItemStack is NULL!!");
        for (int y=0; y < height; y++) {
            if (recipe.ingredients[y] != null) {
                for (int x=0; x < width; x++) {
                    List<ItemStack> itemStacks = new ArrayList<>();
                    if(recipe.ingredients[y].length > x) itemStacks = getIngredient(recipe.ingredients[y][x]);

                    if (itemStacks != null && !itemStacks.isEmpty()) {
                        otherList.add(itemStacks.get(0));
                        MuseLogger.logDebug("cell " + x + ", " + y + " is: " + itemStacks.get(0).getDisplayName());
                    } else {
                        otherList.add(null);
                        MuseLogger.logDebug("cell " + x + ", " + y + " is NULL");
                    }
                }
            }
        }

        ingredients.setInputs( ItemStack.class, otherList);
        ingredients.setOutput( ItemStack.class, this.recipe.getRecipeOutput() );
    }

    public static List<ItemStack> getIngredient(SimpleItemMatcher cell) {
        Boolean shouldbenull = true;
        List<ItemStack> result = null;
        if (cell == null) {
            return null;
        }

        if (cell.oredictName != null) {
            shouldbenull = false;
            result = OreDictionary.getOres(cell.oredictName);

            if (cell.meta != null && result != null && cell.meta != OreDictionary.WILDCARD_VALUE) {
                ArrayList<ItemStack> t = new ArrayList<ItemStack>();
                for (ItemStack stack : result)
                    if (cell.meta == stack.getItemDamage())
                        t.add(stack);
                result = t;
            }
        }

        if (cell.itemStackName != null) {
            shouldbenull = false;
            result = new ArrayList<>();

            ItemStack stack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(cell.itemStackName)), 1);
            if(stack != null) {
                stack = stack.copy();
                if(cell.meta != null) {
                    stack.setItemDamage(cell.meta);
                }
                result.add(stack);
            }
        }

        if(cell.registryName != null) {
            shouldbenull = false;
            result = new ArrayList<>();
            Item item = Item.REGISTRY.getObject(new ResourceLocation(cell.registryName));
            if(item != null) {
                int newMeta = cell.meta == null ? 0 : cell.meta;
                ItemStack stack = new ItemStack(item, 1, newMeta);
                result.add(stack);
            }
        }

        if (cell.unlocalizedName != null)
        {
            shouldbenull = false;
            if (result == null) {
                result = getItemByUnlocalizedName(cell.unlocalizedName);
            } else {
                ArrayList<ItemStack> t = new ArrayList<ItemStack>();
                for (ItemStack stack : result)
                    if (cell.unlocalizedName.equals(stack.getItem().getUnlocalizedName(stack)))
                        t.add(stack);
                result = t;
            }
        }

        if (cell.nbtString != null && result != null) {
            shouldbenull = false;
            ArrayList<ItemStack> t = new ArrayList<ItemStack>();
            for (ItemStack stack : result) {
                ItemStack stack2 = stack.copy();
                try {
                    stack2.setTagCompound(JsonToNBT.getTagFromJson(cell.nbtString));
                } catch (NBTException e) {
                    e.printStackTrace();
                }
                t.add(stack2);
            }
            result = t;
        }
        // this provides some basic info on an invalid recipe cell
        if (!shouldbenull && (result == null || result.size() == 0)) {
            MuseLogger.logDebug("cell should not be null but it is");
            MuseLogger.logDebug("cell.oredictName: " + cell.oredictName);
            MuseLogger.logDebug("cell.itemStackName: " + cell.itemStackName);
            MuseLogger.logDebug("cell.registryName: "+ cell.registryName);
            MuseLogger.logDebug("cell.unlocalizedName: " + cell.unlocalizedName);
            MuseLogger.logDebug("cell.nbtString: " + cell.nbtString);
        }
        return result;
    }



    private static Map<String, ArrayList<ItemStack>> itemMap;
    public static ArrayList<ItemStack> getItemByUnlocalizedName(String unlocalizedName) {
        ArrayList<ItemStack> result = new ArrayList<>();
        if (itemMap == null)
        {
            ItemStack stack = ItemNameMappings.getItem(unlocalizedName);
            if (stack != null)
                result.add(stack);
        }
        if (itemMap != null && itemMap.containsKey(unlocalizedName)){
            result.addAll(itemMap.get(unlocalizedName));
        }
        return result;
    }

    @Override
    public int getWidth() {
        return recipe.getWidth();
    }

    @Override
    public int getHeight() {
        return recipe.ingredients.length;
    }
}
