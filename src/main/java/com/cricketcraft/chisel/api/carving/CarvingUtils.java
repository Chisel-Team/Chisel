package com.cricketcraft.chisel.api.carving;

import java.lang.reflect.Field;

import net.minecraft.item.ItemStack;

public class CarvingUtils {

	/**
	 * A simple way to compare two {@link ICarvingVariation} objects based on the {@link ICarvingVariation#getOrder() getOrder()} method.
	 * 
	 * @param v1
	 *            The first {@link ICarvingVariation variation}.
	 * @param v2
	 *            The second {@link ICarvingVariation variation}.
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

	private static Field _chisel;
	private static boolean errored = false;
	private static final String CARVING_CLASS = "com.cricketcraft.chisel.carving.Carving";

	/**
	 * @return The instance of the chisel carving registry from the chisel mod.
	 *         <p>
	 *         If chisel is not installed or some other error occurs this will return null.
	 */
	public static ICarvingRegistry getChiselRegistry() {
		if (errored) {
			return null;
		}

		try {
			if (_chisel == null) {
				_chisel = Class.forName(CARVING_CLASS).getDeclaredField("chisel");
			}
			return (ICarvingRegistry) _chisel.get(null);
		} catch (Exception e) {
			e.printStackTrace();
			errored = true;
			return getChiselRegistry();
		}
	}
}
