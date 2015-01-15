package com.cricketcraft.chisel.api.carving;

import net.minecraft.item.ItemStack;

public class CarvingUtils {

	/**
	 * The default implementation of {@link ICarvingVariation#compareTo(ICarvingVariation)}
	 * 
	 * @param v1
	 *            The first {@link ICarvingVariation variation}. Usually 'this'.
	 * @param v2
	 *            The second {@link ICarvingVariation variation}. Usually the parameter to the {@code comapreTo} method.
	 * @return A positive integer if the first's order is greater, a negative integer if the second's is greater, and 0 if they are equal.
	 */
	public static int compare(ICarvingVariation v1, ICarvingVariation v2) {
		return v1.getOrder() - v2.getOrder();
	}

	/**
	 * Gets an {@link ItemStack} representing the passed {@link ICarvingVariation}.
	 * 
	 * @param variation
	 *            An {@link ICarvingVariation}
	 * @return An {@link ItemStack}
	 */
	public static ItemStack getStack(ICarvingVariation variation) {
		return new ItemStack(variation.getBlock(), 1, variation.getItemMeta());
	}
}
