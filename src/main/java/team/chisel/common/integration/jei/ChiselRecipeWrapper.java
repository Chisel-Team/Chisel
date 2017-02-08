package team.chisel.common.integration.jei;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;

@ParametersAreNonnullByDefault
public class ChiselRecipeWrapper implements IRecipeWrapper {

    private ICarvingGroup group;

    public ChiselRecipeWrapper(ICarvingGroup group){
        this.group = group;
    }
    
    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        List<ItemStack> inputs = Lists.newArrayList();
        for (ICarvingVariation variation : this.group.getVariations()){
            inputs.add(variation.getStack());
        }
        ingredients.setInputs(ItemStack.class, inputs);
        
        List<ItemStack> outputs = Lists.newArrayList();
        for (ICarvingVariation variation : this.group.getVariations()){
            outputs.add(variation.getStack());
        }
        ingredients.setOutputs(ItemStack.class, outputs);
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY){
        return Collections.emptyList();
    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }
}
