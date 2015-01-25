package com.cricketcraft.chisel.api;

import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.CarvableVariation;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

/**
 * To be implemented on blocks that can be chiseled and need advanced metadata to variation mapping. Currently not very usable without internal classes.
 */
public interface ICarvable {

	/**
	 * Gets a {@link CarvableVariation} from this block, based on metadata.
	 * <p>
	 * Typically you can refer this method to {@link CarvableHelper#getVariation(int)} but this method is provided for more complex metadata handling.
	 * 
	 * @param metadata
	 *            The metadata of the block
	 * @param world
	 *            {@link IBlockAccess} object, usually a world. Use of {@link IBlockAccess} Is necessary due to this method's use in rendering.
	 * @param x
	 *            X coord of the block
	 * @param y
	 *            Y coord of the block
	 * @param z
	 *            Z coord of the block
	 * @param metadata
	 *            The metadata of the block
	 * @return The {@link CarvableVariation} that represents this block in the world.
	 */
	public CarvableVariation getVariation(IBlockAccess world, int x, int y, int z, int metadata);

	/**
	 * Gets the {@link CarvableVariation} for this block when it is in item form.
	 * 
	 * @param stack
	 *            The {@link ItemStack} representing this block;
	 * @return A {@link CarvableVariation}
	 */
	public CarvableVariation getVariation(ItemStack stack);
}
