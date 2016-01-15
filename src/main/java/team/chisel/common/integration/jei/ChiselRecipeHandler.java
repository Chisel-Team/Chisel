package team.chisel.common.integration.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;

import javax.annotation.Nonnull;

public class ChiselRecipeHandler implements IRecipeHandler<CarvingUtils.SimpleCarvingGroup> {

    @Nonnull
    @Override
    public Class<CarvingUtils.SimpleCarvingGroup> getRecipeClass()
    {
        return CarvingUtils.SimpleCarvingGroup.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return "chisel.chiseling";
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(CarvingUtils.SimpleCarvingGroup recipe)
    {
        return new ChiselRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(CarvingUtils.SimpleCarvingGroup recipe)
    {
        return true;
    }
}
