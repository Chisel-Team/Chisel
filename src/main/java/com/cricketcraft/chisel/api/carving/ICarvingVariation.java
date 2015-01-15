package com.cricketcraft.chisel.api.carving;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

/**
 * Represents a variation of a chiselable block.
 * <p>
 * Do note that while this interface extends {@link Comparable}, a default implementation of {@link Comparable#compareTo(Object)} is provided in
 * {@link CarvingUtils#compare(ICarvingVariation, ICarvingVariation)}
 */
public interface ICarvingVariation extends Comparable<ICarvingVariation> {

	/**
	 * The base block of this variation.
	 * 
	 * @return A {@link Block} that is the base of this variation
	 */
	Block getBlock();

	/**
	 * The metadata of this variation
	 * 
	 * @return An int representing the metadata of this variation's {@link Block} in the world.
	 */
	int getBlockMeta();

	/**
	 * The item metadata of this variation
	 * 
	 * @return An int representing the metadata of this variations's {@link Item}.
	 */
	int getItemMeta();

	/**
	 * The "order" of this variation. Represents its position in the list of variations.
	 * 
	 * @return An integer to represent the position of this variation in the list of all variations in the group
	 */
	int getOrder();

}
