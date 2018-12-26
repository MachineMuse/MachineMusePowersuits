package net.machinemuse.numina.recipe;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
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
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

/**
 * Provides a workaround for nbt data types other than strings and integers
 */
@SuppressWarnings("unused")
public class NuminaShapedRecipe extends ShapedRecipes {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public NuminaShapedRecipe(final String group, final int width, final int height, final NonNullList<Ingredient> ingredients, final ItemStack result) {
        super(group, width, height, ingredients, result);
    }

    public static NuminaShapedRecipe deserialize(JsonContext context, JsonObject json) {
        String s = JsonUtils.getString(json, "group", "");
        Map<String, Ingredient> map = deserializeKey(JsonUtils.getJsonObject(json, "key"));
        String[] astring = shrink(patternFromJson(JsonUtils.getJsonArray(json, "pattern")));
        int i = astring[0].length();
        int j = astring.length;
        NonNullList<Ingredient> nonnulllist = deserializeIngredients(astring, map, i, j);
        ItemStack itemstack = deserializeItem(JsonUtils.getJsonObject(json, "result"), true);
        return new NuminaShapedRecipe(s, i, j, nonnulllist, itemstack);
    }

    private static NonNullList<Ingredient> deserializeIngredients(String[] strings, Map<String, Ingredient> ingredientMap, int patternWidth, int patternHeight) {
        NonNullList<Ingredient> nonnulllist = NonNullList.<Ingredient>withSize(patternWidth * patternHeight, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(ingredientMap.keySet());
        set.remove(" ");

        for (int i = 0; i < strings.length; ++i) {
            for (int j = 0; j < strings[i].length(); ++j) {
                String s = strings[i].substring(j, j + 1);
                Ingredient ingredient = ingredientMap.get(s);
                if (ingredient == null)
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                set.remove(s);
                nonnulllist.set(j + patternWidth * i, ingredient);
            }
        }
        if (!set.isEmpty())
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        return nonnulllist;
    }

    @VisibleForTesting
    static String[] shrink(String... strings) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for (int i1 = 0; i1 < strings.length; ++i1) {
            String s = strings[i1];
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

        if (strings.length == l)
            return new String[0];

        String[] astring = new String[strings.length - l - k];
        for (int k1 = 0; k1 < astring.length; ++k1)
            astring[k1] = strings[k1 + k].substring(i, j + 1);

        return astring;
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

    private static String[] patternFromJson(JsonArray p_192407_0_) {
        String[] astring = new String[p_192407_0_.size()];

        if (astring.length > 3)
            throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
        else if (astring.length == 0)
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");

        for (int i = 0; i < astring.length; ++i) {
            String s = JsonUtils.getString(p_192407_0_.get(i), "pattern[" + i + "]");
            if (s.length() > 3)
                throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
            if (i > 0 && astring[0].length() != s.length())
                throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
            astring[i] = s;
        }
        return astring;
    }

    private static Map<String, Ingredient> deserializeKey(JsonObject jsonObj) {
        Map<String, Ingredient> map = Maps.<String, Ingredient>newHashMap();
        for (Map.Entry<String, JsonElement> entry : jsonObj.entrySet()) {
            if (entry.getKey().length() != 1)
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            if (" ".equals(entry.getKey()))
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            map.put(entry.getKey(), deserializeIngredient(entry.getValue()));
        }
        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    public static Ingredient deserializeIngredient(@Nullable JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull())
            throw new JsonSyntaxException("Item cannot be null");

        if (jsonElement.isJsonObject()) {
            if (jsonElement.getAsJsonObject().has("item"))
                return Ingredient.fromStacks(deserializeItem(jsonElement.getAsJsonObject(), false));
            if (jsonElement.getAsJsonObject().has("ore"))
                return new OreIngredient(JsonUtils.getString(jsonElement.getAsJsonObject(), "ore"));
        } else if (!jsonElement.isJsonArray())
            throw new JsonSyntaxException("Expected item to be object or array of objects");

        JsonArray jsonarray = jsonElement.getAsJsonArray();
        if (jsonarray.size() == 0)
            throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");

        ItemStack[] aitemstack = new ItemStack[jsonarray.size()];
        for (int i = 0; i < jsonarray.size(); ++i) {
            if (jsonarray.get(i).getAsJsonObject().has("item"))
                aitemstack[i] = deserializeItem(JsonUtils.getJsonObject(jsonarray.get(i), "item"), false);
            else
                aitemstack[i] = deserializeItem(JsonUtils.getJsonObject(jsonarray.get(i), "ore"), false);
        }
        return Ingredient.fromStacks(aitemstack);
    }

    public static ItemStack deserializeItem(JsonObject jsonObj, boolean useCount) {
        int count = useCount ? JsonUtils.getInt(jsonObj, "count", 1) : 1;
        String name;

        if (jsonObj.has("item")) {
            name = JsonUtils.getString(jsonObj, "item");

            Item item = Item.REGISTRY.getObject(new ResourceLocation(name));
            if (item == null)
                throw new JsonSyntaxException("Unknown item '" + name + "'");
            if (item.getHasSubtypes() && !jsonObj.has("data"))
                throw new JsonParseException("Missing data for item '" + name + "'");

            if (jsonObj.has("nbt")) {
                // Lets hope this works? Needs lang
                try {
                    JsonElement element = jsonObj.get("nbt");
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
                    tmp.setString("id", name);
                    tmp.setInteger("Count", JsonUtils.getInt(jsonObj, "count", 1));
                    tmp.setInteger("Damage", JsonUtils.getInt(jsonObj, "data", 0));

                    return new ItemStack(tmp);
                } catch (NBTException e) {
                    throw new JsonSyntaxException("Invalid NBT Entry: " + e.toString());
                }
            }

            if (item.getHasSubtypes()) {
                int meta = JsonUtils.getInt(jsonObj, "data", 0);
                return new ItemStack(item, count, meta);
            }
            return new ItemStack(item, count);

        } else if (jsonObj.has("ore")) {
            ItemStack itemStack = ItemStack.EMPTY;
            name = JsonUtils.getString(jsonObj, "ore");

            NonNullList<ItemStack> itemStackList = OreDictionary.getOres(name);
            if (itemStackList.size() > 0) {
                itemStack = itemStackList.get(0).copy(); // copying here is important.
                itemStack.setCount(count);
            } else
                throw new JsonSyntaxException("no such ore found: " + name);
            return itemStack;
        }

        throw new JsonSyntaxException("no such or item found");
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
        }
        return nonnulllist;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width >= this.recipeWidth && height >= this.recipeHeight;
    }

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

    @Override
    public int getRecipeWidth() {
        return this.recipeWidth;
    }

    @Override
    public int getRecipeHeight() {
        return this.recipeHeight;
    }
}