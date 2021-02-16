package team.chisel.common.integration.jei;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocus.Mode;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import team.chisel.Chisel;
import team.chisel.api.carving.CarvingUtils;

@ParametersAreNonnullByDefault
public class ChiselRecipeRegistryPlugin implements IRecipeManagerPlugin {
    
    @Nonnull
    private static final List<ResourceLocation> POSSIBLE_CATEGORIES = ImmutableList.of(VanillaRecipeCategoryUid.CRAFTING, VanillaRecipeCategoryUid.FURNACE, VanillaRecipeCategoryUid.INFORMATION);
    
    private IRecipeManager registry;
    
    private List<ItemStack> getAlternateOutputs(IFocus<?> focus) {
        Object val = focus.getValue();
        if (!(val instanceof ItemStack)) return Collections.emptyList();
        ItemStack input = (ItemStack) val;
        List<ItemStack> chiselOptions = CarvingUtils.getChiselRegistry().getItemsForChiseling((ItemStack) focus.getValue());
        return chiselOptions.stream()
                .filter(s -> !ItemStack.areItemsEqual(s, input))
                .collect(Collectors.toList());
    }
    
    private boolean preventRecursion;

    @SuppressWarnings("unchecked")
    @Override
    public <V> List<ResourceLocation> getRecipeCategoryUids(IFocus<V> focus) {
        try {
            if (focus.getMode() != Mode.OUTPUT) return Collections.emptyList();
            List<ItemStack> alternates = getAlternateOutputs(focus);
            if (alternates.isEmpty()) return Collections.emptyList();
            try {
                preventRecursion = true;
                return registry.getRecipeCategories(POSSIBLE_CATEGORIES).stream()
                        .filter(c -> alternates.stream().flatMap(s -> registry.getRecipes(c, registry.createFocus(focus.getMode(), s)).stream()).count() != 0)
                        .map(IRecipeCategory::getUid)
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
                if (!registry.getRecipes(recipeCategory, focus).isEmpty()) return Collections.emptyList();
                return getAlternateOutputs(focus).stream()
                        .map(alternate -> registry.createFocus(focus.getMode(), alternate))
                        .flatMap(f -> registry.getRecipes(recipeCategory, f).stream())
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
}
