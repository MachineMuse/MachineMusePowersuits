package net.machinemuse.numina.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:45 PM, 11/4/13
 */
public class JSONRecipe implements IRecipe {
    public SimpleItemMatcher[][] ingredients;
    public SimpleItemMaker result;
    public Boolean mirror;
    public Boolean unshaped; // TODO: Unshaped recipes

    static final int MAX_WIDTH = 3;
    static final int MAX_HEIGHT = 3;
//    private Object[] input = null;
//
//    public Object[] getInput(){
//        return input;
//    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        int width = getWidth();
        int height = ingredients.length;
        for (int xoffset = 0; xoffset <= MAX_WIDTH - width; xoffset++) {
            for (int yoffset = 0; yoffset <= MAX_HEIGHT - height; yoffset++) {
                if (matchSpot(inv, xoffset, yoffset, width, height, false)) {
                    return true;
                }
                if (mirror != null && mirror && matchSpot(inv, xoffset, yoffset, width, height, true)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchSpot(InventoryCrafting inv, int xoffset, int yoffset, int width, int height, boolean mirror) {
        boolean mismatch = false;
        for (int xinventory = 0; xinventory < MAX_WIDTH; xinventory++) {
            for (int yinventory = 0; yinventory < MAX_HEIGHT; yinventory++) {
                SimpleItemMatcher matcher;
                if (!mirror) {
                    matcher = getMatcher(-xoffset + xinventory, -yoffset + yinventory);
                } else {
                    matcher = getMatcher(-xoffset + width - xinventory - 1, -yoffset + yinventory);
                }
                if (matcher != null && !matcher.matchesItem(getInvStack(inv, xinventory, yinventory))) {
                    mismatch = true;
                }
                if (matcher == null && getInvStack(inv, xinventory, yinventory) != null) {
                    mismatch = true;
                }
            }
        }
        return !mismatch;
    }

    private ItemStack getInvStack(InventoryCrafting inv, int x, int y) {
        if (x < 0 || y < 0) {
            return null;
        } else {
            return inv.getStackInRowAndColumn(x, y);
        }
    }

    public SimpleItemMatcher getMatcher(int x, int y) {
        if (y >= 0 && y < ingredients.length) {
            if (x >= 0 && x < ingredients[y].length) {
                return ingredients[y][x];
            }
        }
        return null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        return result.makeItem(inventoryCrafting);
    }

    @Override
    public int getRecipeSize() {
        if (ingredients == null) return 0;
        int size = 0;
        for (IItemMatcher[] row : ingredients) {
            if (row != null) {
                for (IItemMatcher cell : row) {
                    if (cell != null) {
                        size++;
                    }
                }
            }
        }
        return size;
    }

    @Override
    public ItemStack getRecipeOutput() {
        System.out.println("Doing something here");

        return result.getRecipeOutput();
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
        for (int i = 0; i < aitemstack.length; ++i)
        {
            ItemStack itemstack = inv.getStackInSlot(i);
            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }
        return aitemstack;
        //return new ItemStack[0];
    }

    public int getWidth() {
        int size = 0;
        for (IItemMatcher[] row : ingredients) {
            if (row != null) {
                size = Math.max(row.length, size);
            }
        }
        return size;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof JSONRecipe)) {
            return false;
        }
        JSONRecipe other = (JSONRecipe) obj;
        if(ingredients.length != other.ingredients.length) return false;
        for(int i = 0; i<ingredients.length;i++) {
            if(ingredients[i].length != other.ingredients[i].length) return false;
            for(int j=0; j<ingredients[i].length;j++) {

                if(!compareIngredients(ingredients[i][j], other.ingredients[i][j])) {
                    return false;
                }
            }
        }
        if(!compareResult(result, other.result)) return false;
        return compareBoolean(mirror, other.mirror);
    }

    private boolean compareResult(SimpleItemMaker a, SimpleItemMaker b) {
        if(a == null && b == null) return true;
        if(a != null && b != null) {
            if(a.equals(b)) return true;
        }
        return false;
    }
    private boolean compareIngredients(SimpleItemMatcher a, SimpleItemMatcher b) {
        if(a == null && b == null) return true;
        if(a != null && b != null) {
            if(a.equals(b)) return true;
        }
        return false;
    }

    private boolean compareBoolean(Boolean a, Boolean b) {
        if(a == null && b == null) return true;
        if(a != null && b != null) {
            if(a.booleanValue() == b.booleanValue()) return true;
        }
        return false;
    }
}