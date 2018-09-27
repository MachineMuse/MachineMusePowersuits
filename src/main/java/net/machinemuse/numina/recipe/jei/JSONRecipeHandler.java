package net.machinemuse.numina.recipe.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.numina.recipe.JSONRecipe;

import javax.annotation.Nonnull;

/**
 * Numina by MachineMuse
 * Created by lehjr on 3/3/17.
 */
public class JSONRecipeHandler implements IRecipeHandler<JSONRecipe> {
    @Override
    public Class<JSONRecipe> getRecipeClass() {
        return JSONRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid(JSONRecipe recipe) {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull JSONRecipe recipe) {
        return new JEINuminaWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull JSONRecipe recipe) {
        recipe.validate();
        if (!recipe.getIsValid())
            MuseLogger.logError("recipe is NOT valid for " + (!recipe.getRecipeOutput().isEmpty() ? recipe.getRecipeOutput().getDisplayName() : " EMPTY OUTPUT"));
        return recipe.getIsValid();
    }
}
