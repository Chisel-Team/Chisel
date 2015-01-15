package com.cricketcraft.chisel.api.carving;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/**
 * Represents a variation of a chiselable block.
 * <p>
 * Do note that while this interface extends {@link Comparable}, a default implementation of {@link Comparable#compareTo(Object)} is provided in
 * {@link CarvingUtils#compare(ICarvingVariation, ICarvingVariation)}
 */
public interface ICarvingVariation extends Comparable<ICarvingVariation> {

	Block getBlock();

	int getBlockMeta();

	int getItemMeta();

	/**
	 * Gets an {@link ItemStack} representing this variation.
	 * 
	 * @return An {@link ItemStack} to represent this variation
	 */
	ItemStack getStack();

	/**
	 * The "order" of this variation. Represents its position in the list of variations.
	 * 
	 * @return An integer to represent the position of this variation in the list of all variations in the group
	 */
	int getOrder();

}
