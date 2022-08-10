package team.chisel.common.integration.jei;

import com.google.common.collect.ImmutableMap;
import com.tterrag.registrate.util.entry.ItemEntry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IRegistryDelegate;
import team.chisel.Chisel;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.client.util.ChiselLangKeys;
import team.chisel.common.init.ChiselItems;
import team.chisel.common.item.ItemChisel;

import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings({"unused", "removal", "CommentedOutCode"})
@JeiPlugin
public class ChiselJEIPlugin implements IModPlugin {

    private final ChiselRecipeRegistryPlugin plugin = new ChiselRecipeRegistryPlugin();
    private final Map<IRegistryDelegate<Item>, ChiselLangKeys> descriptions = ImmutableMap.of(
            ChiselItems.IRON_CHISEL.get().delegate, ChiselLangKeys.JEI_DESC_CHISEL_IRON,
            ChiselItems.DIAMOND_CHISEL.get().delegate, ChiselLangKeys.JEI_DESC_CHISEL_DIAMOND,
            ChiselItems.HITECH_CHISEL.get().delegate, ChiselLangKeys.JEI_DESC_CHISEL_HITECH);
    private ChiselRecipeCategory category;

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        assert CarvingUtils.getChiselRegistry() != null;
        registry.addRecipes(new ArrayList<>(CarvingUtils.getChiselRegistry().getGroups()), category.getUid());

        for (ItemEntry<ItemChisel> chisel : ChiselItems.CHISELS) {
            ItemStack stack = new ItemStack(chisel.get());
            registry.addIngredientInfo(stack, VanillaTypes.ITEM, ChiselLangKeys.JEI_DESC_CHISEL_GENERIC.getComponent(), descriptions.get(chisel.get().delegate).getComponent());
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        for (ItemEntry<ItemChisel> chisel : ChiselItems.CHISELS) {
            registry.addRecipeCatalyst(new ItemStack(chisel.get()), category.getUid());
        }
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registry) {
        registry.addRecipeManagerPlugin(plugin); // Add our plugin the normal way as a fallback in case JEI internals change
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        // TODO Overhaul
        //try {
        //    List<IRecipeManagerPlugin> plugins = ObfuscationReflectionHelper.getPrivateValue(RecipeManager.class, (RecipeManager) jeiRuntime.getRecipeManager(), "plugins");
        //    // If reflection succeeds, put our plugin at the beginning of the list
        //    plugins.remove(plugin);
        //    Iterator<IRecipeManagerPlugin> iter = plugins.iterator();
        //    while (iter.hasNext()) {
        //        // Unwrap wrappers and remove ours
        //        IRecipeManagerPlugin p = iter.next();
        //        if (p instanceof RecipeManagerPluginSafeWrapper) {
        //            IRecipeManagerPlugin wrapped = ObfuscationReflectionHelper.getPrivateValue(RecipeManagerPluginSafeWrapper.class, (RecipeManagerPluginSafeWrapper) p, "plugin");
        //            if (wrapped == plugin) {
        //                iter.remove();
        //            }
        //        }
        //    }
        //    plugins.add(0, plugin);
        //} catch (Exception e) {
        //    Chisel.logger.error("Failed to inject recipe registry plugin at beginning of list, cannot guarantee vanilla recipes will show first", e);
        //}
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
