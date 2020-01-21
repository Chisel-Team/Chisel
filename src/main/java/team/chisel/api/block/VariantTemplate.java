package team.chisel.api.block;

import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.StringUtils;

import mcp.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface VariantTemplate {
    
    ModelTemplate getModelTemplate();
    
    String getName();
    
    default String getLocalizedName() {
        return StringUtils.capitalize(getName());
    }
    
    String[] getTooltip();

}
