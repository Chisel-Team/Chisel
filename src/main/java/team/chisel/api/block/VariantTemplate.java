package team.chisel.api.block;

import javax.annotation.ParametersAreNonnullByDefault;

import com.tterrag.registrate.providers.RegistrateLangProvider;

import mcp.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface VariantTemplate {
    
    ModelTemplate getModelTemplate();
    
    String getName();
    
    default String getLocalizedName() {
        return RegistrateLangProvider.toEnglishName(getName());
    }
    
    String[] getTooltip();

}
