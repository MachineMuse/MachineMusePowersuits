package net.machinemuse.numina.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.machinemuse.numina.common.recipe.JSONRecipe;
import net.machinemuse.numina.general.MuseLogger;

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

//    @Override
//    public String getRecipeCategoryUid() {
//        return VanillaRecipeCategoryUid.CRAFTING;
//    }

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
            MuseLogger.logError("recipe is NOT valid for " + ((recipe.getRecipeOutput() != null) ? recipe.getRecipeOutput().getDisplayName() : " NULL OUTPUT"));
        return recipe.getIsValid();
    }
}
