package net.machinemuse.powersuits.recipe;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import net.machinemuse.numina.utils.MuseLogger;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

/**
 * Provides a workaround for nbt data types other than strings and integers
 */
@SuppressWarnings("unused")
public class MPSShapedRecipe extends ShapedRecipes {

    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    static boolean isRecipeValid = true;


    public MPSShapedRecipe(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
        super(group, width, height, ingredients, result);
    }

    @NotNull
    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
        }
        return nonnulllist;
    }

    @Override
    public boolean canFit(int width, int height) {
        return super.canFit(width, height);
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        for (int i = 0; i <= inv.getWidth() - this.recipeWidth; ++i) {
            for (int j = 0; j <= inv.getHeight() - this.recipeHeight; ++j) {
                if (this.checkMatch(inv, i, j, true))
                    return true;

                if (this.checkMatch(inv, i, j, false))
                    return true;
            }
        }
        return false;
    }

    /**
     * Checks if the region of a crafting inventory is match for the recipe.
     */
    private boolean checkMatch(InventoryCrafting craftingInventory, int startX, int startY, boolean mirror) {
        for (int i = 0; i < craftingInventory.getWidth(); ++i) {
            for (int j = 0; j < craftingInventory.getHeight(); ++j) {
                int k = i - startX;
                int l = j - startY;
                Ingredient ingredient = Ingredient.EMPTY;

                if (k >= 0 && l >= 0 && k < this.recipeWidth && l < this.recipeHeight) {
                    if (mirror)
                        ingredient = this.recipeItems.get(this.recipeWidth - k - 1 + l * this.recipeWidth);
                    else
                        ingredient = this.recipeItems.get(k + l * this.recipeWidth);
                }

                if (!ingredient.apply(craftingInventory.getStackInRowAndColumn(i, j)))
                    return false;
            }
        }
        return true;
    }

    public static MPSShapedRecipe deserialize(JsonObject json, JsonContext context) {
        String group = JsonUtils.getString(json, "group", "");
        Map<String, Ingredient> map = deserializeKey(JsonUtils.getJsonObject(json, "key"), context);
        String[] astring = shrink(patternFromJson(JsonUtils.getJsonArray(json, "pattern")));
        int width = astring[0].length();
        int height = astring.length;
        NonNullList<Ingredient> nonnulllist = deserializeIngredients(astring, map, width, height);
        ItemStack itemstack;

        JsonObject obj = JsonUtils.getJsonObject(json, "result");

        String type = context.appendModId(JsonUtils.getString(obj, "type", "minecraft:item"));
        if (type.isEmpty() || !type.equals("forge:ore_dict"))
            itemstack = deserializeItemFixed(obj, context);
        else
            itemstack = deserializeOreAsResult(obj, context);

        return new MPSShapedRecipe(group, width, height, nonnulllist, isRecipeValid ? itemstack : ItemStack.EMPTY );
    }

    /**
     * Use an Oredict entry as a result
     */
    public static ItemStack deserializeOreAsResult(JsonObject json, JsonContext context) {
        String oreName = JsonUtils.getString(json, "ore");

        NonNullList<ItemStack> itemStackList = OreDictionary.getOres(oreName);
        ItemStack itemStack = ItemStack.EMPTY;
        if (itemStackList.size() > 0) {
            itemStack = itemStackList.get(0);
            itemStack.setCount(JsonUtils.getInt(json, "count", 1));
            setRecipeToInvalid();

        } else
            MuseLogger.logException("no ore dictionary entry found for " + oreName, new JsonSyntaxException("missing oredict entry"));
        return itemStack;
    }

    static void setRecipeToInvalid() {
        isRecipeValid = false;
    }

    private static Map<String, Ingredient> deserializeKey(JsonObject json, JsonContext context) {
        Map<String, Ingredient> map = Maps.newHashMap();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getKey().length() != 1)
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");

            if (" ".equals(entry.getKey()))
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

            map.put(entry.getKey(), deserializeIngredientFixed(entry.getValue(), context));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    @VisibleForTesting
    static String[] shrink(String... toShrink) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for (int i1 = 0; i1 < toShrink.length; ++i1) {
            String s = toShrink[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);

            if (j1 < 0) {
                if (k == i1)
                    ++k;

                ++l;
            } else
                l = 0;
        }

        if (toShrink.length == l)
            return new String[0];
        else {
            String[] astring = new String[toShrink.length - l - k];

            for (int k1 = 0; k1 < astring.length; ++k1)
                astring[k1] = toShrink[k1 + k].substring(i, j + 1);

            return astring;
        }
    }

    private static int firstNonSpace(String str) {
        int i;

        for (i = 0; i < str.length() && str.charAt(i) == ' '; ++i) {
        }
        return i;
    }

    private static int lastNonSpace(String str) {
        int i;

        for (i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; --i) {

        }
        return i;
    }

    private static String[] patternFromJson(JsonArray jsonArr) {
        String[] astring = new String[jsonArr.size()];

        if (astring.length > 3)
            throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
        else if (astring.length == 0)
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        else {
            for (int i = 0; i < astring.length; ++i) {
                String s = JsonUtils.getString(jsonArr.get(i), "pattern[" + i + "]");

                if (s.length() > 3)
                    throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");

                if (i > 0 && astring[0].length() != s.length())
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");

                astring[i] = s;
            }
            return astring;
        }
    }

    private static NonNullList<Ingredient> deserializeIngredients(String[] pattern, Map<String, Ingredient> ingredeintMap, int patternWidth, int patternHeight) {
        NonNullList<Ingredient> nonnulllist = NonNullList.<Ingredient>withSize(patternWidth * patternHeight, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(ingredeintMap.keySet());
        set.remove(" ");

        for (int i = 0; i < pattern.length; ++i) {
            for (int j = 0; j < pattern[i].length(); ++j) {
                String s = pattern[i].substring(j, j + 1);
                Ingredient ingredient = ingredeintMap.get(s);

                if (ingredient == null)
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");

                set.remove(s);
                nonnulllist.set(j + patternWidth * i, ingredient);
            }
        }

        if (!set.isEmpty())
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        else
            return nonnulllist;
    }


    public static Ingredient deserializeIngredientFixed(@Nullable JsonElement json, JsonContext context) {
        if (json == null || json.isJsonNull())
            throw new JsonSyntaxException("Item cannot be null");

        // start with JsonArray
        if (json.isJsonArray()) {
            JsonArray jsonarray = json.getAsJsonArray();

            if (jsonarray.size() == 0)
                throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
            else {
                ItemStack[] aitemstack = new ItemStack[jsonarray.size()];

                for (int i = 0; i < jsonarray.size(); ++i)
                    aitemstack[i] = deserializeItemFixed(JsonUtils.getJsonObject(jsonarray.get(i), "item"), context);

                return Ingredient.fromStacks(aitemstack);
            }
        }

        // fall back on an object
        if (!json.isJsonObject())
            throw new JsonSyntaxException("Expected ingredient to be a object or array of objects");

        JsonObject obj = (JsonObject) json;

        String type = context.appendModId(JsonUtils.getString(obj, "type", "minecraft:item"));
        if (type.isEmpty())
            throw new JsonSyntaxException("Ingredient type can not be an empty string");

        if(type.equals("forge:ore_dict")) {
            return new OreIngredient(JsonUtils.getString(obj, "ore"));
        }

        if (type.equals("minecraft:item") || type.equals("minecraft:item_nbt")) {
            String item = JsonUtils.getString(obj, "item");
            if (item.startsWith("#")) {
                Ingredient constant = context.getConstant(item.substring(1));
                if (constant == null)
                    throw new JsonSyntaxException("Ingredient referenced invalid constant: " + item);
                return constant;
            }
        }

        if (json.isJsonObject())
            return Ingredient.fromStacks(deserializeItemFixed(json.getAsJsonObject(), context));
        else if (!json.isJsonArray())
            throw new JsonSyntaxException("Expected item to be object or array of objects");

        return Ingredient.EMPTY;
    }

    /**
     * Copied from net.minecraftforge.common.crafting.CraftingHelper#getItemStack
     */
    public static ItemStack deserializeItemFixed(JsonObject json, JsonContext context) {
        String itemName = context.appendModId(JsonUtils.getString(json, "item"));

        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));

        if (item == null)
            throw new JsonSyntaxException("Unknown item '" + itemName + "'");

        if (item.getHasSubtypes() && !json.has("data"))
            throw new JsonParseException("Missing data for item '" + itemName + "'");

        if (json.has("nbt")) {
            // Lets hope this works? Needs test
            try {
                JsonElement element = json.get("nbt");
                NBTTagCompound nbt;
                if (element.isJsonObject())
                    nbt = JsonToNBTFixed.getTagFromJson(GSON.toJson(element));
                else
                    nbt = JsonToNBTFixed.getTagFromJson(element.getAsString());

                NBTTagCompound tmp = new NBTTagCompound();
                if (nbt.hasKey("ForgeCaps")) {
                    tmp.setTag("ForgeCaps", nbt.getTag("ForgeCaps"));
                    nbt.removeTag("ForgeCaps");
                }

                tmp.setTag("tag", nbt);
                tmp.setString("id", itemName);
                tmp.setInteger("Count", JsonUtils.getInt(json, "count", 1));
                tmp.setInteger("Damage", JsonUtils.getInt(json, "data", 0));

                return new ItemStack(tmp);
            } catch (NBTException e) {
                throw new JsonSyntaxException("Invalid NBT Entry: " + e.toString());
            }
        }
        return new ItemStack(item, JsonUtils.getInt(json, "count", 1), JsonUtils.getInt(json, "data", 0));
    }
}