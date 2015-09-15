package team.chisel.utils;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;

/**
 * Utility for crafting/recipe things
 */
public class RecipeUtil {

    @SuppressWarnings("unchecked")
	public static boolean isCraftable(ItemStack stack){
        for (IRecipe recipe : (List<IRecipe>)CraftingManager.getInstance().getRecipeList()){
            if (recipe.getRecipeOutput()==null){
                continue;
            }
            if (stack.isItemEqual(recipe.getRecipeOutput())){
                return true;
            }
        }
        return false;
    }

    public static boolean isSmeltable(ItemStack stack){
        return FurnaceRecipes.smelting().getSmeltingList().containsKey(stack);
    }

    public static boolean isCreatable(ItemStack stack){
        return isCraftable(stack) || isSmeltable(stack);
    }

}
