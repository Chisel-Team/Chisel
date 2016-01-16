package team.chisel.common.integration.jei;

import javax.annotation.Nonnull;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.common.integration.jei.ChiselRecipeHandler.CarvingGroupWrapper;

public class ChiselRecipeHandler implements IRecipeHandler<CarvingGroupWrapper> {

    @AllArgsConstructor
    public static class CarvingGroupWrapper implements ICarvingGroup {

        @Delegate
        private ICarvingGroup group;
    }

    @Nonnull
    @Override
    public Class<CarvingGroupWrapper> getRecipeClass() {
        return CarvingGroupWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return "chisel.chiseling";
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(CarvingGroupWrapper recipe) {
        return new ChiselRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(CarvingGroupWrapper recipe) {
        return true;
    }
}
