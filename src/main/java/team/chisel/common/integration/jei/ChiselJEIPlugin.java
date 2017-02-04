package team.chisel.common.integration.jei;

import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import team.chisel.common.carving.Carving;
import team.chisel.common.integration.jei.ChiselRecipeHandler.CarvingGroupWrapper;

@JEIPlugin
@ParametersAreNonnullByDefault
public class ChiselJEIPlugin implements IModPlugin {

    @Override
    public void register(IModRegistry registry){
        registry.addRecipeCategories(new ChiselRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeHandlers(new ChiselRecipeHandler());
        
        registry.addRecipes(Carving.chisel.getSortedGroupNames().stream().map(s -> Carving.chisel.getGroup(s)).map(g -> new CarvingGroupWrapper(g)).collect(Collectors.toList()));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {
    }
}
