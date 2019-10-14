package team.chisel.common.integration.jei;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocus.Mode;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeRegistryPlugin;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import team.chisel.Chisel;
import team.chisel.api.carving.CarvingUtils;

@ParametersAreNonnullByDefault
public class ChiselRecipeRegistryPlugin implements IRecipeRegistryPlugin {
    
    @Nonnull
    private static final List<String> POSSIBLE_CATEGORIES = ImmutableList.of(VanillaRecipeCategoryUid.CRAFTING, VanillaRecipeCategoryUid.SMELTING, VanillaRecipeCategoryUid.INFORMATION);
    
    private IRecipeRegistry registry;
    
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
    public <V> List<String> getRecipeCategoryUids(IFocus<V> focus) {
        try {
            if (focus.getMode() != Mode.OUTPUT) return Collections.emptyList();
            List<ItemStack> alternates = getAlternateOutputs(focus);
            if (alternates.isEmpty()) return Collections.emptyList();
            try {
                preventRecursion = true;
                return registry.getRecipeCategories(POSSIBLE_CATEGORIES).stream()
                        .filter(c -> alternates.stream().flatMap(s -> registry.getRecipeWrappers(c, registry.createFocus(focus.getMode(), s)).stream()).count() != 0)
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
    public <T extends IRecipeWrapper, V> List<T> getRecipeWrappers(IRecipeCategory<T> recipeCategory, IFocus<V> focus) {
        try {
            if (preventRecursion) return Collections.emptyList();
            if (focus.getMode() != Mode.OUTPUT) return Collections.emptyList();
            if (!POSSIBLE_CATEGORIES.contains(recipeCategory.getUid())) return Collections.emptyList();
            try {
                preventRecursion = true;
                if (!registry.getRecipeWrappers(recipeCategory, focus).isEmpty()) return Collections.emptyList();
                return getAlternateOutputs(focus).stream()
                        .map(alternate -> registry.createFocus(focus.getMode(), alternate))
                        .flatMap(f -> registry.getRecipeWrappers(recipeCategory, f).stream())
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
    public <T extends IRecipeWrapper> List<T> getRecipeWrappers(IRecipeCategory<T> recipeCategory) {
        return Collections.emptyList();
    }

    void setRecipeRegistry(IRecipeRegistry recipeRegistry) {
        this.registry = recipeRegistry;
    }
}
