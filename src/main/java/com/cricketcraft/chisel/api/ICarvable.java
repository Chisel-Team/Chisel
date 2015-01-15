package com.cricketcraft.chisel.api;

import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.CarvableVariation;

public interface ICarvable {

	/**
	 * Gets a {@link CarvableVariation} from this block, based on metadata.
	 * <p>
	 * Typically you can refer this method to {@link CarvableHelper#getVariation(int)} but this method is provided for more complex metadata handling.
	 * 
	 * @param metadata
	 *            The metadata of the block
	 */
	public CarvableVariation getVariation(int metadata);
}
