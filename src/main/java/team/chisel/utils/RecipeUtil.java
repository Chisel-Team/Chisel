package team.chisel.utils;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

/**
 * Utility for crafting/recipe things
 */
public class RecipeUtil {

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
