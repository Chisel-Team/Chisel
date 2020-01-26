package team.chisel.api.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.text.TranslationTextComponent;
import team.chisel.api.carving.ICarvingVariation;

/**
 * Represents data about a specific variation
 */
@MethodsReturnNonnullByDefault
public interface VariationData extends ICarvingVariation {
    
    String getName();
    
    TranslationTextComponent getDisplayName();
}