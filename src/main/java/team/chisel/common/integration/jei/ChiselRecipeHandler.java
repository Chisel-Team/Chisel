package team.chisel.common.integration.jei;

import javax.annotation.ParametersAreNonnullByDefault;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.common.integration.jei.ChiselRecipeHandler.CarvingGroupWrapper;

@ParametersAreNonnullByDefault
public class ChiselRecipeHandler implements IRecipeHandler<CarvingGroupWrapper> {

    @AllArgsConstructor
    public static class CarvingGroupWrapper implements ICarvingGroup {

        @Delegate
        private ICarvingGroup group;
    }

    @Override
    public Class<CarvingGroupWrapper> getRecipeClass() {
        return CarvingGroupWrapper.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return "chisel.chiseling";
    }

    @Override
    public String getRecipeCategoryUid(CarvingGroupWrapper recipe) {
        return getRecipeCategoryUid();
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(CarvingGroupWrapper recipe) {
        return new ChiselRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(CarvingGroupWrapper recipe) {
        return true;
    }
}
