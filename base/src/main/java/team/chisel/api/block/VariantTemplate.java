package team.chisel.api.block;

import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import team.chisel.api.item.ItemModelTemplate;
import team.chisel.client.data.ItemModelTemplates;

public interface VariantTemplate {
    
    Optional<ModelTemplate> getModelTemplate();

    Optional<ItemModelTemplate> getItemModelTemplate();

    default Optional<RecipeTemplate> getRecipeTemplate() {
        return Optional.empty();
    }
    
    String getName();
    
    default String getLocalizedName() {
        return RegistrateLangProvider.toEnglishName(getName());
    }
    
    String[] getTooltip();
}
