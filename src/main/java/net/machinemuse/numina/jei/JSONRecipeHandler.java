package net.machinemuse.numina.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.machinemuse.numina.recipe.JSONRecipe;

import javax.annotation.Nonnull;

/**
 * Created by leon on 3/31/16.
 */
public class JSONRecipeHandler implements IRecipeHandler<JSONRecipe> {
    @Nonnull
    @Override
    public Class<JSONRecipe> getRecipeClass() {
        return JSONRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return "minecraft.crafting";
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull JSONRecipe recipe) {
        return "minecraft.crafting";
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull JSONRecipe recipe) {
        return new JEINuminaWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull JSONRecipe recipe) {
        //todo: actual real recipe validation
//        if (recipe.getRecipeSize() < 2)
//            return false;

        return recipe.getRecipeOutput() != null;
    }

}