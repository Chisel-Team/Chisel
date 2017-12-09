package team.chisel.common.integration.jei;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Lists;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@ParametersAreNonnullByDefault
public class ChiselRecipeCategory implements IRecipeCategory<ChiselRecipeWrapper> {

    private static final ResourceLocation TEXTURE_LOC = new ResourceLocation("chisel", "textures/chiselJEI.png");
    
    private final IDrawable background;
    private final IDrawable arrowUp, arrowDown;

    private @Nullable IRecipeLayout layout;
    private @Nullable IFocus<?> focus;

    public ChiselRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE_LOC, 0, 0, 165, 126);
        this.arrowDown = guiHelper.createDrawable(TEXTURE_LOC, 166, 0, 18, 15);
        this.arrowUp = guiHelper.createDrawable(TEXTURE_LOC, 166, 15, 18, 15);
    }

    @Nonnull
    @Override
    public String getUid() {
        return "chisel.chiseling";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return I18n.format("chisel.jei.title");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        if (layout != null) {
            if (focus == null || focus.getMode() == IFocus.Mode.INPUT) {
                arrowDown.draw(minecraft, 73, 21);
            } else {
                arrowUp.draw(minecraft, 73, 21);
            }
        }
    }

    @Override
    public void drawAnimations(Minecraft minecraft) {
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ChiselRecipeWrapper recipeWrapper) {        
    }
    
    public void setRecipe(IRecipeLayout recipeLayout, ChiselRecipeWrapper recipeWrapper, IIngredients ingredients) {
        this.layout = recipeLayout;
        this.focus = layout.getFocus();
        
        recipeLayout.getItemStacks().init(0, focus == null || focus.getMode() == IFocus.Mode.INPUT, 73, 3);
        if (focus != null) {
            recipeLayout.getItemStacks().set(0, (ItemStack) focus.getValue()); 
        } else {
            recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).stream().flatMap(l -> l.stream()).collect(Collectors.toList()));
        }

        int rowWidth = 9;

        int xStart = 2;
        int yStart = 36;

        int outputs = ingredients.getOutputs(ItemStack.class).size();
        int MAX_SLOTS = 45;
        
        List<List<ItemStack>> stacks = Lists.newArrayList();
        
        for (int i = 0; i < outputs; i++) {
            int slot = i % MAX_SLOTS;
            if (stacks.size() <= slot) {
                stacks.add(Lists.newArrayList());
            }
         
            ItemStack stack = (ItemStack) ingredients.getOutputs(ItemStack.class).get(i);
            stacks.get(slot).add(stack.copy());
        }
        
        if (outputs > MAX_SLOTS) {
            int leftover = outputs % MAX_SLOTS;
            for (int i = leftover; i < MAX_SLOTS; i++) {
                stacks.get(i).add(null);
            }
        }
        
        for (int i = 0; i < stacks.size(); i++) {
            
            int x = xStart + (i % rowWidth) * 18;
            int y = yStart + (i / rowWidth) * 18;
            
            recipeLayout.getItemStacks().init(i + 1, focus != null && focus.getMode() == IFocus.Mode.OUTPUT, x, y);
            recipeLayout.getItemStacks().set(i + 1, stacks.get(i));
        }
    }

    @Nonnull
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return Collections.emptyList();
    }

    @Override
    @Nullable
    public IDrawable getIcon() {
        return null;
    }
}
