package team.chisel.common.integration.jei;

import mezz.jei.api.IItemRegistry;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class ChiselJEIPlugin implements IModPlugin {

    private IJeiHelpers jeiHelpers;

    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers){
        this.jeiHelpers = jeiHelpers;
    }

    public void onItemRegistryAvailable(IItemRegistry itemRegistry){
    }

    public void register(IModRegistry registry){
        registry.addRecipeCategories(new ChiselRecipeCategory(jeiHelpers.getGuiHelper()));
        registry.addRecipeHandlers(new ChiselRecipeHandler());
    }

    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry){
    }
}
