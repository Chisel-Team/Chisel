package com.cricketcraft.chisel.api;

import com.cricketcraft.chisel.carving.CarvableVariation;

public interface ICarvable {

	/**
	 * This grabs the variations possible
	 * 
	 * @param metadata
	 *            This is the metadata that a block will use and the basis of
	 *            chisel Traditionally you make an instance of CarvableHelper
	 *            ex. CarvableHelper carverHelper = new CarvableHelper(); In
	 *            this method you return carverHelper.getVariation(metadata) and
	 *            that should be all you need if you register it correctly.
	 */
	public CarvableVariation getVariation(int metadata);
}
