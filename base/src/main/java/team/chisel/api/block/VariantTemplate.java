package team.chisel.api.block;

import com.tterrag.registrate.providers.RegistrateLangProvider;

import java.util.Optional;

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
