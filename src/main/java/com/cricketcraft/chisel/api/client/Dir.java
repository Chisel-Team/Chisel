package com.cricketcraft.chisel.api.client;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import static net.minecraftforge.common.util.ForgeDirection.*;

public enum Dir {
	
	TOP(UP), 
	TOP_RIGHT(UP, EAST), 
	RIGHT(EAST), 
	BOTTOM_RIGHT(DOWN, EAST), 
	BOTTOM(DOWN), 
	BOTTOM_LEFT(DOWN, WEST), 
	LEFT(WEST), 
	TOP_LEFT(UP, WEST);
	// @formatter:on

	private static final ForgeDirection NORMAL = SOUTH;
	public static final Dir[] VALUES = values();

	private ForgeDirection[] dirs;

	private Dir(ForgeDirection... dirs) {
		this.dirs = dirs;
	}

	public boolean isConnected(CTM inst, IBlockAccess world, int x, int y, int z, int sideIdx, Block block, int meta) {
		ForgeDirection side = getOrientation(sideIdx);
		ForgeDirection[] dirs = getNormalizedDirs(side);
		for (ForgeDirection dir : dirs) {
			x += dir.offsetX;
			y += dir.offsetY;
			z += dir.offsetZ;
		}
		return inst.isConnected(world, x, y, z, side, block, meta);
	}

	private ForgeDirection[] getNormalizedDirs(ForgeDirection normal) {
		if (normal == NORMAL) {
			return dirs;
		} else if (normal == NORMAL.getOpposite()) {
			// If this is the opposite direction of the default normal, we
			// need to mirror the dirs
			// A mirror version does not affect y+ and y- so we ignore those
			ForgeDirection[] ret = new ForgeDirection[dirs.length];
			for (int i = 0; i < ret.length; i++) {
				ret[i] = dirs[i].offsetY != 0 ? dirs[i] : dirs[i].getOpposite();
			}
			return ret;
		} else {
			ForgeDirection axis = null;
			// Next, we need different a different rotation axis depending
			// on if this is up/down or not
			if (normal.offsetY == 0) {
				// If it is not up/down, pick either the left or right-hand
				// rotation
				axis = normal == NORMAL.getRotation(UP) ? UP : DOWN;
			} else {
				// If it is up/down, pick either the up or down rotation.
				axis = normal == UP ? NORMAL.getRotation(DOWN) : NORMAL.getRotation(UP);
			}
			ForgeDirection[] ret = new ForgeDirection[dirs.length];
			// Finally apply all the rotations
			for (int i = 0; i < ret.length; i++) {
				ret[i] = dirs[i].getRotation(axis);
			}
			return ret;
		}
	}
}