package com.cricketcraft.chisel.api;

import team.chisel.ctmlib.ICTMBlock;
import team.chisel.ctmlib.ISubmapManager;
import net.minecraft.world.IBlockAccess;

import com.cricketcraft.chisel.api.carving.CarvableHelper;
import com.cricketcraft.chisel.api.carving.IVariationInfo;

/**
 * To be implemented on blocks that can be chiseled and need advanced metadata to variation mapping. Currently not very usable without internal classes.
 */
public interface ICarvable extends ICTMBlock<IVariationInfo> {

	/**
	 * Gets a {@link ISubmapManager} from this block, based on metadata.
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
	 * @return The {@link ISubmapManager} that represents this block in the world.
	 */
	@Override
	public IVariationInfo getManager(IBlockAccess world, int x, int y, int z, int metadata);

	/**
	 * Gets the {@link ISubmapManager} for this block when it is in item form.
	 * 
	 * @return A {@link ISubmapManager}
	 */
	@Override
	public IVariationInfo getManager(int meta);
}
