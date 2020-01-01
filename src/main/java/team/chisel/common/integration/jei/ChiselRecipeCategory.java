package team.chisel.common.integration.jei;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Lists;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import team.chisel.Chisel;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.common.init.ChiselItems;

@ParametersAreNonnullByDefault
public class ChiselRecipeCategory implements IRecipeCategory<ICarvingGroup> {

    private static final ResourceLocation TEXTURE_LOC = new ResourceLocation("chisel", "textures/chisel_jei.png");
    
    private final IDrawable icon;
    private final IDrawable background;
    private final IDrawable arrowUp, arrowDown;

    private @Nullable IRecipeLayout layout;
    private @Nullable IFocus<?> focus;

    public ChiselRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(ChiselItems.CHISEL_IRON);
        this.background = guiHelper.createDrawable(TEXTURE_LOC, 0, 0, 165, 126);
        this.arrowDown = guiHelper.createDrawable(TEXTURE_LOC, 166, 0, 18, 15);
        this.arrowUp = guiHelper.createDrawable(TEXTURE_LOC, 166, 15, 18, 15);
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(Chisel.MOD_ID, "chiseling");
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
    public void draw(ICarvingGroup recipe, double mouseX, double mouseY) {
        if (layout != null) {
            if (focus == null || focus.getMode() == IFocus.Mode.INPUT) {
                arrowDown.draw(73, 21);
            } else {
                arrowUp.draw(73, 21);
            }
        }
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ICarvingGroup recipeWrapper, IIngredients ingredients) {
        this.layout = recipeLayout;
        IFocus<?> focus = (this.focus = recipeLayout.getFocus());
        
        recipeLayout.getItemStacks().init(0, focus == null || focus.getMode() == IFocus.Mode.INPUT, 73, 3);
        if (focus != null) {
            recipeLayout.getItemStacks().set(0, (ItemStack) focus.getValue()); 
        } else {
            recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).stream().flatMap(l -> l.stream()).collect(Collectors.toList()));
        }

        int rowWidth = 9;

        int xStart = 2;
        int yStart = 36;

        int outputs = ingredients.getOutputs(VanillaTypes.ITEM).size();
        int MAX_SLOTS = 45;
        
        List<List<ItemStack>> stacks = Lists.newArrayList();
        
        for (int i = 0; i < outputs; i++) {
            int slot = i % MAX_SLOTS;
            if (stacks.size() <= slot) {
                stacks.add(Lists.newArrayList());
            }
         
            ItemStack stack = (ItemStack) ingredients.getOutputs(VanillaTypes.ITEM).get(i).get(0);
            stacks.get(slot).add(stack.copy());
        }
        
        if (outputs > MAX_SLOTS) {
            int leftover = outputs % MAX_SLOTS;
            for (int i = leftover; i < MAX_SLOTS; i++) {
                stacks.get(i).add(ItemStack.EMPTY);
            }
        }
        
        for (int i = 0; i < stacks.size(); i++) {
            
            int x = xStart + (i % rowWidth) * 18;
            int y = yStart + (i / rowWidth) * 18;
            
            recipeLayout.getItemStacks().init(i + 1, focus != null && focus.getMode() == IFocus.Mode.OUTPUT, x, y);
            recipeLayout.getItemStacks().set(i + 1, stacks.get(i));
        }
    }

    @Override
    public Class<? extends ICarvingGroup> getRecipeClass() {
        return ICarvingGroup.class;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(ICarvingGroup group, IIngredients ingredients) {
        List<ItemStack> variants = group.getItemTag().getAllElements().stream().map(ItemStack::new).collect(Collectors.toList());
        
        ingredients.setInputs(VanillaTypes.ITEM, variants);
        ingredients.setOutputs(VanillaTypes.ITEM, variants);
    }
}
