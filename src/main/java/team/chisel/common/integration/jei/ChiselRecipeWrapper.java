package team.chisel.common.integration.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class ChiselRecipeWrapper implements IRecipeWrapper {

    private ICarvingGroup group;

    public ChiselRecipeWrapper(ICarvingGroup group){
        this.group = group;
    }

    @Override
    public List getInputs(){
        List<ItemStack> inputStack = Lists.newArrayList();
        for (ICarvingVariation variation : this.group.getVariations()){
            inputStack.add(variation.getStack());
        }
        return Arrays.asList(inputStack);
    }

    @Override
    public List getOutputs(){
        List<ItemStack> outputStack = Lists.newArrayList();
        for (ICarvingVariation variation : this.group.getVariations()){
            outputStack.add(variation.getStack());
        }
        return outputStack;
    }

    @Override
    public List<FluidStack> getFluidInputs(){
        return Lists.newArrayList();
    }

    @Override
    public List<FluidStack> getFluidOutputs(){
        return Lists.newArrayList();
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight){

    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight){

    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY){
        return Lists.newArrayList();
    }
}
