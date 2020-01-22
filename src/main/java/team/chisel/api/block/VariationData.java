package team.chisel.api.block;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.Value;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Represents data about a specific variation
 */
@ParametersAreNonnullByDefault
@Value
public class VariationData {

    /**
     * The Name of this variation
     */
    String name;
    
    TranslationTextComponent displayName;
    
    /**
     * The carving group of this variation
     */
    @Nullable
    ResourceLocation group;
    
    /**
     * The Index of this variation in the blocks total variations
     */
    int index;
    
    /**
     * If this variation is opaque, it will block light. Set this to false on any variation which has a see-through texture.
     */
    boolean opaque;
    
    ModelTemplate template;
}