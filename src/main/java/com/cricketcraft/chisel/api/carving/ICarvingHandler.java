package com.cricketcraft.chisel.api.carving;

import com.cricketcraft.chisel.common.variation.Variation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Is used by any class that implements the logic of the chiseling mechanic
 *
 * @author minecreatr
 */
public interface ICarvingHandler {


    /**
     * Get an array of CarvingVariationRepresentations from the item.
     * @param item
     * @return The Array of CarvingVariationRepresentation's
     */
    public CarvingVariationRepresentation[] getCarvingVariations(Item item);

    /**
     * Carves the item into the specified variation
     * @param stack The ItemStack
     * @param variation The Variation
     * @return The New ItemStack
     */
    public ItemStack carveItem(ItemStack stack, Variation variation);


}
