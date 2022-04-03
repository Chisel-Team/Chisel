package team.chisel.common.integration.jei;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import team.chisel.Chisel;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.client.util.ChiselLangKeys;
import team.chisel.common.init.ChiselItems;

public class ChiselRecipeCategory implements IRecipeCategory<ICarvingGroup> {

    private static final ResourceLocation TEXTURE_LOC = new ResourceLocation("chisel", "textures/chiseljei.png");
    
    private final IDrawable icon;
    private final IDrawable background;
    private final IDrawable arrowUp, arrowDown;

    private @Nullable IRecipeLayoutBuilder layout;
    private @Nullable IFocusGroup focus;

    public ChiselRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ChiselItems.IRON_CHISEL.get()));
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
    public Component getTitle() {
        return ChiselLangKeys.JEI_TITLE.getComponent();
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void draw(ICarvingGroup recipe, PoseStack PoseStack, double mouseX, double mouseY) {
        if (layout != null) {
            if (focus == null || focus.getFocuses(VanillaTypes.ITEM, RecipeIngredientRole.OUTPUT).count() == 0) {
                arrowDown.draw(PoseStack, 73, 21);
            } else {
                arrowUp.draw(PoseStack, 73, 21);
            }
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder recipeLayout, ICarvingGroup recipeWrapper, IFocusGroup focus) {
        this.layout = recipeLayout;
        this.focus = focus;
        
        IRecipeSlotBuilder inputSlot = recipeLayout.addSlot(RecipeIngredientRole.INPUT, 74, 4);
        if (focus.getFocuses(VanillaTypes.ITEM, RecipeIngredientRole.OUTPUT).count() > 0) {
            inputSlot.addItemStacks(focus.getFocuses(VanillaTypes.ITEM, RecipeIngredientRole.OUTPUT).map(f -> f.getTypedValue().getIngredient()).toList()); 
        } else {
            inputSlot.addIngredients(Ingredient.of(recipeWrapper.getItemTag().getKey()));
        }

        int rowWidth = 9;

        int xStart = 3;
        int yStart = 37;

        List<ItemStack> groupStacks = CarvingUtils.getChiselRegistry().getItemsForChiseling(recipeWrapper.getId());
        int MAX_SLOTS = 45;

        List<List<ItemStack>> stacks = Lists.newArrayList();
        
        for (int i = 0; i < groupStacks.size(); i++) {
            int slot = i % MAX_SLOTS;
            if (stacks.size() <= slot) {
                stacks.add(Lists.newArrayList());
            }

            ItemStack stack = groupStacks.get(i);
            stacks.get(slot).add(stack.copy());
        }
        
        if (groupStacks.size() > MAX_SLOTS) {
            int leftover = groupStacks.size() % MAX_SLOTS;
            for (int i = leftover; i < MAX_SLOTS; i++) {
                stacks.get(i).add(ItemStack.EMPTY);
            }
        }

        for (int i = 0; i < stacks.size(); i++) {
            
            int x = xStart + (i % rowWidth) * 18;
            int y = yStart + (i / rowWidth) * 18;
            
            IRecipeSlotBuilder outputSlot = recipeLayout.addSlot(RecipeIngredientRole.OUTPUT, x, y);
            outputSlot.addItemStacks(stacks.get(i));
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

//    @Override
//    public void setIngredients(ICarvingGroup group, IIngredients ingredients) {
//        List<ItemStack> variants = group.getItemTag().stream().map(ItemStack::new).collect(Collectors.toList());
//        
//        ingredients.setInputs(VanillaTypes.ITEM, variants);
//        ingredients.setOutputs(VanillaTypes.ITEM, variants);
//    }
}
