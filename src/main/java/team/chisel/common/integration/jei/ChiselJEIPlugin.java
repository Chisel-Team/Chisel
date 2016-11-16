package team.chisel.common.integration.jei;

import java.util.stream.Collectors;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;
import team.chisel.Chisel;
import team.chisel.common.carving.Carving;
import team.chisel.common.integration.jei.ChiselRecipeHandler.CarvingGroupWrapper;

@JEIPlugin
public class ChiselJEIPlugin implements IModPlugin {

    @Override
    public void register(IModRegistry registry){
        ChiselRecipeCategory category = new ChiselRecipeCategory(registry.getJeiHelpers().getGuiHelper()); 
        registry.addRecipeCategories(category);
        
        registry.addRecipeHandlers(new ChiselRecipeHandler());
        registry.addRecipes(Carving.chisel.getSortedGroupNames().stream().map(s -> Carving.chisel.getGroup(s)).map(g -> new CarvingGroupWrapper(g)).collect(Collectors.toList()));
        
        registry.addRecipeCategoryCraftingItem(new ItemStack(Chisel.itemChiselIron), category.getUid());
        registry.addRecipeCategoryCraftingItem(new ItemStack(Chisel.itemChiselDiamond), category.getUid());
        registry.addRecipeCategoryCraftingItem(new ItemStack(Chisel.itemChiselHitech), category.getUid());
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
