package team.chisel.common.integration.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocus.Mode;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import team.chisel.Chisel;
import team.chisel.api.carving.CarvingUtils;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChiselRecipeRegistryPlugin implements IRecipeManagerPlugin {

    @Nonnull
    private static final List<RecipeType<? extends Object>> POSSIBLE_CATEGORIES = ImmutableList.of(RecipeTypes.CRAFTING, RecipeTypes.SMELTING, RecipeTypes.INFORMATION);

    private IRecipeManager registry;
    private boolean preventRecursion;

    private List<ItemStack> getAlternateOutputs(IFocus<?> focus) {
        Object val = focus.getValue();
        if (!(val instanceof ItemStack input)) return Collections.emptyList();
        List<ItemStack> chiselOptions = CarvingUtils.getChiselRegistry().getItemsForChiseling((ItemStack) focus.getValue());
        return chiselOptions.stream()
                .filter(s -> !ItemStack.isSame(s, input))
                .collect(Collectors.toList());
    }

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public <V> List<RecipeType<?>> getRecipeTypes(IFocus<V> focus) {
        try {
            if (focus.getMode() != Mode.OUTPUT) return Collections.emptyList();
            List<ItemStack> alternates = getAlternateOutputs(focus);
            if (alternates.isEmpty()) return Collections.emptyList();
            try {
                preventRecursion = true;
                return registry.createRecipeCategoryLookup().limitTypes(POSSIBLE_CATEGORIES).get()
                        .filter(c -> alternates.stream()
                                .flatMap(s -> registry.createRecipeLookup(c.getRecipeType())
                                        .limitFocus(Collections.singletonList(registry.createFocus(focus.getMode(), s))).get()).count() != 0)
                        .map(IRecipeCategory::getRecipeType)
                        .collect(Collectors.toList());
            } finally {
                preventRecursion = false;
            }
        } catch (RuntimeException | LinkageError e) {
            Chisel.logger.error(e);
            return Collections.emptyList();
        }
    }


    @Override
    public <T, V> List<T> getRecipes(IRecipeCategory<T> recipeCategory, IFocus<V> focus) {
        try {
            if (preventRecursion) return Collections.emptyList();
            if (focus.getMode() != Mode.OUTPUT) return Collections.emptyList();
            if (!POSSIBLE_CATEGORIES.contains(recipeCategory.getUid())) return Collections.emptyList();
            try {
                preventRecursion = true;
                if (!registry.getRecipes(recipeCategory, focus, false).isEmpty()) return Collections.emptyList();
                return getAlternateOutputs(focus).stream()
                        .map(alternate -> registry.createFocus(focus.getMode(), alternate))
                        .flatMap(f -> registry.getRecipes(recipeCategory, f, false).stream())
                        .collect(Collectors.toList());
            } finally {
                preventRecursion = false;
            }
        } catch (RuntimeException | LinkageError e) {
            Chisel.logger.error(e);
            return Collections.emptyList();
        }
    }

    @Override
    public <T> List<T> getRecipes(IRecipeCategory<T> recipeCategory) {
        return Collections.emptyList();
    }

    void setRecipeRegistry(IRecipeManager recipeRegistry) {
        this.registry = recipeRegistry;
    }

    @Override
    public <V> List<ResourceLocation> getRecipeCategoryUids(IFocus<V> focus) {
        return POSSIBLE_CATEGORIES.stream().map(t -> t.getUid()).toList();
    }
}
