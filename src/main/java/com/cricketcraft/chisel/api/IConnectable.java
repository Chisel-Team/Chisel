package com.cricketcraft.chisel.api;

import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * This extension of {@link IFacade} allows the block to say whether or not OTHER CTM blocks can connect to IT.
 */
public interface IConnectable extends IFacade {

	/**
	 * Determines whether other CTM blocks can connect to this one.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param from
	 *            The direction that the block is connecting from. This differes from the {@link IFacade} methods in that it is NOT the side which is being rendered.
	 * @return True if a block can connect to this one from the given direction. False otherwise.
	 */
	boolean canConnectCTM(IBlockAccess world, int x, int y, int z, ForgeDirection from);

}
