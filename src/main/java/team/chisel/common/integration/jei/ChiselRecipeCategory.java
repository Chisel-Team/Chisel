package team.chisel.common.integration.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import javax.annotation.Nonnull;

public class ChiselRecipeCategory implements IRecipeCategory {

    private String cachedTitle;

    private IDrawable background;

    public ChiselRecipeCategory(IGuiHelper guiHelper){
        ResourceLocation location = new ResourceLocation("chisel", "textures/chisel2Gui.png");
        this.cachedTitle = StatCollector.translateToLocal("chisel.jeiCrafting");
        this.background = guiHelper.createDrawable(location, 0, 0, 176, 166);
    }

    @Nonnull
    @Override
    public String getUid(){
        return "chisel.chiseling";
    }

    @Nonnull
    @Override
    public String getTitle(){
        return cachedTitle;
    }

    @Nonnull
    @Override
    public IDrawable getBackground(){
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft)
    {

    }

    @Override
    public void drawAnimations(Minecraft minecraft)
    {

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
        recipeLayout.getItemStacks().init(0, true, 10, 10);
        recipeLayout.getItemStacks().set(0, recipeWrapper.getInputs());

        int rowWidth = 11;

        int startX = 40;
        int startY = 5;

        for (int i = 0 ; i < recipeWrapper.getOutputs().size() ; i ++){
            int x = startX;
            int y = startY;

            int down = (i/rowWidth);
            for (int j = 0 ; j < down ; j++){
                y+=5;
            }
            int right = i%rowWidth;
            for (int j = 0 ; j < right ; j++){
                x+=5;
            }
            recipeLayout.getItemStacks().init(i+1, false, x, y);
            recipeLayout.getItemStacks().set(i+1, (ItemStack) recipeWrapper.getOutputs().get(i));
        }

    }
}
