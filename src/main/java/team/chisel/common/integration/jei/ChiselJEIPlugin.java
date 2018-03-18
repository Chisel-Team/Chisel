package team.chisel.common.integration.jei;

import java.util.ArrayList;
import java.util.Arrays;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.common.carving.Carving;
import team.chisel.common.init.ChiselItems;

@JEIPlugin
@ParametersAreNonnullByDefault
public class ChiselJEIPlugin implements IModPlugin {
    
    @SuppressWarnings("null")
    private ChiselRecipeCategory category;

    @SuppressWarnings("null")
    @Override
    public void register(IModRegistry registry){

        registry.handleRecipes(ICarvingGroup.class, ChiselRecipeWrapper::new, "chisel.chiseling");
        registry.addRecipes(Carving.chisel.getSortedGroupNames().stream()
                            .map(s -> Carving.chisel.getGroup(s))
                            .collect(Collectors.toList()), category.getUid());
        
        Arrays.stream(new Item[] {ChiselItems.chisel_iron, ChiselItems.chisel_diamond, ChiselItems.chisel_hitech})
              .map(ItemStack::new)
              .forEach(stack -> {
                  registry.addRecipeCatalyst(stack, category.getUid());
                  registry.addIngredientInfo(stack, ItemStack.class,
                          "jei.chisel.desc.chisel_generic", 
                          "\n",
                          "jei.chisel.desc." + stack.getItem().getRegistryName().getResourcePath());
              });

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
