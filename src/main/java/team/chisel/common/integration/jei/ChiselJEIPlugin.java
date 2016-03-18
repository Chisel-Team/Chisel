package team.chisel.common.integration.jei;

import java.util.stream.Collectors;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import team.chisel.common.carving.Carving;
import team.chisel.common.integration.jei.ChiselRecipeHandler.CarvingGroupWrapper;

@JEIPlugin
public class ChiselJEIPlugin implements IModPlugin {

    private IJeiHelpers jeiHelpers;

    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers){
        this.jeiHelpers = jeiHelpers;
    }

    @Override
    public void register(IModRegistry registry){
        registry.addRecipeCategories(new ChiselRecipeCategory(jeiHelpers.getGuiHelper()));
        registry.addRecipeHandlers(new ChiselRecipeHandler());
        
        registry.addRecipes(Carving.chisel.getSortedGroupNames().stream().map(s -> Carving.chisel.getGroup(s)).map(g -> new CarvingGroupWrapper(g)).collect(Collectors.toList()));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
    }
}
