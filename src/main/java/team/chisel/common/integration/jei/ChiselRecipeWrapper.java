package team.chisel.common.integration.jei;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;

@ParametersAreNonnullByDefault
public class ChiselRecipeWrapper implements IRecipeWrapper {

    private ICarvingGroup group;

    public ChiselRecipeWrapper(ICarvingGroup group){
        this.group = group;
    }
    
    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        List<ItemStack> variants = CarvingUtils.getChiselRegistry().getItemsForChiseling(group);
        
        ingredients.setInputs(ItemStack.class, variants);
        ingredients.setOutputs(ItemStack.class, variants);
    }
    
    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }

    @SuppressWarnings("null")
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY){
        return Collections.emptyList();
    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }
}
