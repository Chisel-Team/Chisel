package team.chisel.common.integration.jei;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.recipes.RecipeManager;
import mezz.jei.recipes.RecipeManagerPluginSafeWrapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.GenerationStage.Carving;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import team.chisel.Chisel;
import team.chisel.common.init.ChiselItems;

@JeiPlugin
@ParametersAreNonnullByDefault
public class ChiselJEIPlugin implements IModPlugin {
    
    private ChiselRecipeCategory category;
    
    private final ChiselRecipeRegistryPlugin plugin = new ChiselRecipeRegistryPlugin();

    private final Item[] chisels = new Item[] { ChiselItems.chisel_iron, ChiselItems.chisel_diamond, ChiselItems.chisel_hitech };

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        registry.addRecipes(Carving.chisel.getSortedGroupNames().stream()
                            .map(s -> Carving.chisel.getGroup(s))
                            .collect(Collectors.toList()), category.getUid());

        for (Item chisel : chisels) {
            ItemStack stack = new ItemStack(chisel);
            registry.addIngredientInfo(stack, VanillaTypes.ITEM, "jei.chisel.desc.chisel_generic", "\n", "jei.chisel.desc." + stack.getItem().getRegistryName().getPath());
        }
    }
    
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        for (Item chisel : chisels) {
            registry.addRecipeCatalyst(new ItemStack(chisel), category.getUid());
        }
    }
    
    @Override
    public void registerAdvanced(IAdvancedRegistration registry) {   
        registry.addRecipeManagerPlugin(plugin); // Add our plugin the normal way as a fallback in case JEI internals change
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        try {
            List<IRecipeManagerPlugin> plugins = ObfuscationReflectionHelper.getPrivateValue(RecipeManager.class, (RecipeManager) jeiRuntime.getRecipeManager(), "plugins");
            // If reflection succeeds, put our plugin at the beginning of the list
            plugins.remove(plugin);
            Iterator<IRecipeManagerPlugin> iter = plugins.iterator();
            while (iter.hasNext()) {
                // Unwrap wrappers and remove ours
                IRecipeManagerPlugin p = iter.next();
                if (p instanceof RecipeManagerPluginSafeWrapper) {
                    IRecipeManagerPlugin wrapped = ObfuscationReflectionHelper.getPrivateValue(RecipeManagerPluginSafeWrapper.class, (RecipeManagerPluginSafeWrapper) p, "plugin");
                    if (wrapped == plugin) {
                        iter.remove();
                    }
                }
            }
            plugins.add(0, plugin);
        } catch (Exception e) {
            Chisel.logger.error("Failed to inject recipe registry plugin at beginning of list, cannot guarantee vanilla recipes will show first", e);
        }
        plugin.setRecipeRegistry(jeiRuntime.getRecipeManager());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        category = new ChiselRecipeCategory(registry.getJeiHelpers().getGuiHelper()); 
        registry.addRecipeCategories(category);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Chisel.MOD_ID, Chisel.MOD_ID);
    }
}
