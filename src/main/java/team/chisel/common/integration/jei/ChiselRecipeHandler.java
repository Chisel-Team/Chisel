package team.chisel.common.integration.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import team.chisel.api.carving.ICarvingGroup;

import javax.annotation.Nonnull;

public class ChiselRecipeHandler implements IRecipeHandler<ICarvingGroup> {

    @Nonnull
    @Override
    public Class<ICarvingGroup> getRecipeClass()
    {
        return ICarvingGroup.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return "chisel.chiseling";
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(ICarvingGroup recipe)
    {
        return new ChiselRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(ICarvingGroup recipe)
    {
        return true;
    }
}
