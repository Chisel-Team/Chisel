package team.chisel.api.block;

import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import com.tterrag.registrate.providers.RegistrateLangProvider;

public interface VariantTemplate {
    
    Optional<ModelTemplate> getModelTemplate();
    
    default Optional<RecipeTemplate> getRecipeTemplate() {
        return Optional.empty();
    }
    
    String getName();
    
    default String getLocalizedName() {
        return RegistrateLangProvider.toEnglishName(getName());
    }
    
    String[] getTooltip();
}
