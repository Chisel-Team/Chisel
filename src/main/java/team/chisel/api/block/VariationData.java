package team.chisel.api.block;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import lombok.AllArgsConstructor;
import net.minecraft.item.ItemStack;

/**
 * Represents data about a specific variation
 */
@AllArgsConstructor
@ParametersAreNonnullByDefault
public class VariationData {

    /**
     * The Name of this variation
     */
    public String name;
    
    /**
     * The path for the blockstate/model information
     */
    public String path;

    /**
     * The carving group of this variation
     */
    @Nullable
    public String group;

    /**
     * The Itemstack that is smelted into this variation, if null it cant be smelted
     */
    public ItemStack smeltedFrom;

    /**
     * Gets the amount of this block produced through smelting
     */
    public int amountSmelted;
    
    /**
     * The Index of this variation in the blocks total variations
     */
    public int index;
    
    /**
     * If this variation is opaque, it will block light. Set this to false on any variation which has a see-through texture.
     */
    public boolean opaque;
}