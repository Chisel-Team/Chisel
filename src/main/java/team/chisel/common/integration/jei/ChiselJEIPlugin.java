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
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import team.chisel.Features;
import team.chisel.common.carving.Carving;
import team.chisel.common.init.ChiselItems;
import team.chisel.common.integration.jei.ChiselRecipeHandler.CarvingGroupWrapper;

@JEIPlugin
@ParametersAreNonnullByDefault
public class ChiselJEIPlugin implements IModPlugin {
    
    private ChiselRecipeCategory category;

    @Override
    public void register(IModRegistry registry){

        registry.addRecipeHandlers(new ChiselRecipeHandler());
        registry.addRecipes(Carving.chisel.getSortedGroupNames().stream().map(s -> Carving.chisel.getGroup(s)).map(g -> new CarvingGroupWrapper(g)).collect(Collectors.toList()), category.getUid());

        if (Features.CHISEL.enabled()) {
            registry.addRecipeCatalyst(new ItemStack(ChiselItems.chisel_iron), category.getUid());
            registry.addRecipeCatalyst(new ItemStack(ChiselItems.chisel_diamond), category.getUid());
            registry.addRecipeCatalyst(new ItemStack(ChiselItems.chisel_hitech), category.getUid());
        }

        ArrayList<ItemStack> itemStacks = new ArrayList<ItemStack>();

        for(int i = 0; i < 16; i++)
        {
            itemStacks.add(new ItemStack(Blocks.CONCRETE_POWDER, 1, i));
            itemStacks.add(new ItemStack(Blocks.CONCRETE, 1, i));
        }

        registry.addIngredientInfo(itemStacks, ItemStack.class, "jei.chisel.desc.concrete_making");
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
