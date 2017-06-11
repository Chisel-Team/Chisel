package team.chisel.common.integration.jei;

import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import team.chisel.Chisel;
import team.chisel.common.carving.Carving;
import team.chisel.common.init.ChiselBlocks;
import team.chisel.common.integration.jei.ChiselRecipeHandler.CarvingGroupWrapper;

@JEIPlugin
@ParametersAreNonnullByDefault
public class ChiselJEIPlugin implements IModPlugin {
    
    private ChiselRecipeCategory category;

    @Override
    public void register(IModRegistry registry){

        registry.addRecipeHandlers(new ChiselRecipeHandler());
        registry.addRecipes(Carving.chisel.getSortedGroupNames().stream().map(s -> Carving.chisel.getGroup(s)).map(g -> new CarvingGroupWrapper(g)).collect(Collectors.toList()), category.getUid());
        
        registry.addRecipeCatalyst(new ItemStack(Chisel.itemChiselIron), category.getUid());
        registry.addRecipeCatalyst(new ItemStack(Chisel.itemChiselDiamond), category.getUid());
        registry.addRecipeCatalyst(new ItemStack(Chisel.itemChiselHitech), category.getUid());

        ArrayList<ItemStack> itemStacks = new ArrayList<ItemStack>();

        for(int i = 0; i < 16; i++)
        {
            itemStacks.add(new ItemStack(ChiselBlocks.concrete_powder, 1, i));
            itemStacks.add(new ItemStack(ChiselBlocks.concrete, 1, i));
        }

        registry.addDescription(itemStacks, "jei.chisel.desc.concrete_making");
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
    
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        category = new ChiselRecipeCategory(registry.getJeiHelpers().getGuiHelper()); 
        registry.addRecipeCategories(category);
    }
}
