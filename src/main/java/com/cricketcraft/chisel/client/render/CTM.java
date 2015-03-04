package com.cricketcraft.chisel.client.render;

import com.cricketcraft.chisel.config.Configurations;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.cricketcraft.chisel.api.IFacade;

/**
 * The CTM renderer will draw the block's FACE using by assembling 4 quadrants from the 5 available block
 * textures.  The normal Texture.png is the blocks "unconnected" texture, and is used when CTM is disabled or the block
 * has nothing to connect to.  This texture has all of the outside corner quadrants  The texture-ctm.png contains the
 * rest of the quadrants.
 *
 * ┌─────────────────┐ ┌────────────────────────────────┐
 * │ texture.png     │ │ texture-ctm.png                │
 * │ ╔══════╤══════╗ │ │  ──────┼────── ║ ─────┼───── ║ │
 * │ ║      │      ║ │ │ │      │      │║      │      ║ │
 * │ ║ 16   │ 17   ║ │ │ │ 0    │ 1    │║ 2    │ 3    ║ │
 * │ ╟──────┼──────╢ │ │ ┼──────┼──────┼╟──────┼──────╢ │
 * │ ║      │      ║ │ │ │      │      │║      │      ║ │
 * │ ║ 18   │ 19   ║ │ │ │ 4    │ 5    │║ 6    │ 7    ║ │
 * │ ╚══════╧══════╝ │ │  ──────┼────── ║ ─────┼───── ║ │
 * └─────────────────┘ │ ═══════╤═══════╝ ─────┼───── ╚ │
 *                     │ │      │      ││      │      │ │
 *                     │ │ 8    │ 9    ││ 10   │ 11   │ │
 *                     │ ┼──────┼──────┼┼──────┼──────┼ │
 *                     │ │      │      ││      │      │ │
 *                     │ │ 12   │ 13   ││ 14   │ 15   │ │
 *                     │ ═══════╧═══════╗ ─────┼───── ╔ │
 *                     └────────────────────────────────┘
 *
 * combining { 18, 13,  9, 16 }, we can generate a texture connected to the right!
 *
 * ╔══════╤═══════
 * ║      │      │
 * ║ 16   │ 9    │
 * ╟──────┼──────┼
 * ║      │      │
 * ║ 18   │ 13   │
 * ╚══════╧═══════
 *
 *
 * combining { 18, 13, 11,  2 }, we can generate a texture, in the shape of an L (connected to the right, and up
 *
 * ║ ─────┼───── ╚
 * ║      │      │
 * ║ 2    │ 11   │
 * ╟──────┼──────┼
 * ║      │      │
 * ║ 18   │ 13   │
 * ╚══════╧═══════
 *
 *
 * HAVE FUN!
 * -CptRageToaster-
 */

public class CTM {
	public static int[] getSubmapIndices(IBlockAccess world, int x, int y, int z, int side) {
		if (world == null)
			return new int[] { 18, 19, 17, 16 };

		Block block = world.getBlock(x, y, z);
		int blockMetadata = world.getBlockMetadata(x, y, z);

		boolean b[] = new boolean[8];
		/**
		 * b[0]    b[1]    b[2]
		 *
		 *
		 *
		 * b[3]    FACE    b[4]
		 *
		 *
		 *
		 * b[5]    b[6]    b[7]
		 */
		if (side == 0) {
			b[0] = isConnected(world, x - 1, y, z + 1, side, block, blockMetadata);
			b[1] = isConnected(world, x, y, z + 1, side, block, blockMetadata);
			b[2] = isConnected(world, x + 1, y, z + 1, side, block, blockMetadata);
			b[3] = isConnected(world, x - 1, y, z, side, block, blockMetadata);
			b[4] = isConnected(world, x + 1, y, z, side, block, blockMetadata);
			b[5] = isConnected(world, x - 1, y, z - 1, side, block, blockMetadata);
			b[6] = isConnected(world, x, y, z - 1, side, block, blockMetadata);
			b[7] = isConnected(world, x + 1, y, z - 1, side, block, blockMetadata);
		} else if (side == 1) {
			b[0] = isConnected(world, x - 1, y, z - 1, side, block, blockMetadata);
			b[1] = isConnected(world, x, y, z - 1, side, block, blockMetadata);
			b[2] = isConnected(world, x + 1, y, z - 1, side, block, blockMetadata);
			b[3] = isConnected(world, x - 1, y, z, side, block, blockMetadata);
			b[4] = isConnected(world, x + 1, y, z, side, block, blockMetadata);
			b[5] = isConnected(world, x - 1, y, z + 1, side, block, blockMetadata);
			b[6] = isConnected(world, x, y, z + 1, side, block, blockMetadata);
			b[7] = isConnected(world, x + 1, y, z + 1, side, block, blockMetadata);
		} else if (side == 2) {
			b[0] = isConnected(world, x + 1, y + 1, z, side, block, blockMetadata);
			b[1] = isConnected(world, x, y + 1, z, side, block, blockMetadata);
			b[2] = isConnected(world, x - 1, y + 1, z, side, block, blockMetadata);
			b[3] = isConnected(world, x + 1, y, z, side, block, blockMetadata);
			b[4] = isConnected(world, x - 1, y, z, side, block, blockMetadata);
			b[5] = isConnected(world, x + 1, y - 1, z, side, block, blockMetadata);
			b[6] = isConnected(world, x, y - 1, z, side, block, blockMetadata);
			b[7] = isConnected(world, x - 1, y - 1, z, side, block, blockMetadata);
		} else if (side == 3) {
			b[0] = isConnected(world, x - 1, y + 1, z, side, block, blockMetadata);
			b[1] = isConnected(world, x, y + 1, z, side, block, blockMetadata);
			b[2] = isConnected(world, x + 1, y + 1, z, side, block, blockMetadata);
			b[3] = isConnected(world, x - 1, y, z, side, block, blockMetadata);
			b[4] = isConnected(world, x + 1, y, z, side, block, blockMetadata);
			b[5] = isConnected(world, x - 1, y - 1, z, side, block, blockMetadata);
			b[6] = isConnected(world, x, y - 1, z, side, block, blockMetadata);
			b[7] = isConnected(world, x + 1, y - 1, z, side, block, blockMetadata);
		} else if (side == 4) {
			b[0] = isConnected(world, x, y + 1, z - 1, side, block, blockMetadata);
			b[1] = isConnected(world, x, y + 1, z, side, block, blockMetadata);
			b[2] = isConnected(world, x, y + 1, z + 1, side, block, blockMetadata);
			b[3] = isConnected(world, x, y, z - 1, side, block, blockMetadata);
			b[4] = isConnected(world, x, y, z + 1, side, block, blockMetadata);
			b[5] = isConnected(world, x, y - 1, z - 1, side, block, blockMetadata);
			b[6] = isConnected(world, x, y - 1, z, side, block, blockMetadata);
			b[7] = isConnected(world, x, y - 1, z + 1, side, block, blockMetadata);
		} else if (side == 5) {
			b[0] = isConnected(world, x, y + 1, z + 1, side, block, blockMetadata);
			b[1] = isConnected(world, x, y + 1, z, side, block, blockMetadata);
			b[2] = isConnected(world, x, y + 1, z - 1, side, block, blockMetadata);
			b[3] = isConnected(world, x, y, z + 1, side, block, blockMetadata);
			b[4] = isConnected(world, x, y, z - 1, side, block, blockMetadata);
			b[5] = isConnected(world, x, y - 1, z + 1, side, block, blockMetadata);
			b[6] = isConnected(world, x, y - 1, z, side, block, blockMetadata);
			b[7] = isConnected(world, x, y - 1, z - 1, side, block, blockMetadata);
		}

		int[] ret = new int[] { 18, 19, 17, 16 };

		// Bottom Left
		if (b[3] || b[6]) {
			ret[0] = 4 + (b[6] ? 2 : 0) + (b[3] ? 8 : 0);
			if (b[3] && b[5] && b[6]) ret[0] = 4;
		}

		// Bottom Right
		if (b[6] || b[4]) {
			ret[1] = 5 + (b[6] ? 2 : 0) + (b[4] ? 8 : 0);
			if (b[4] && b[6] && b[7]) ret[1] = 5;
		}

		// Top Right
		if (b[4] || b[1]) {
			ret[2] = 1 + (b[1] ? 2 : 0) + (b[4] ? 8 : 0);
			if (b[1] && b[2] && b[4]) ret[2] = 1;
		}

		// Top Left
		if (b[1] || b[3]) {
			ret[3] = (b[1] ? 2 : 0) + (b[3] ? 8 : 0);
			if (b[0] && b[1] && b[3]) ret[3] = 0;
		}

		return ret;
	}

	public static boolean isConnected(IBlockAccess world, int x, int y, int z, int side, Block block, int meta) {

		ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[side];

		int x2 = x + dir.offsetX;
		int y2 = y + dir.offsetY;
		int z2 = z + dir.offsetZ;

		Block con = getBlockOrFacade(world, x, y, z, side);
		Block obscuring = getBlockOrFacade(world, x2, y2, z2, side);

		// no block or a bad API user
		if (con == null) {
			return false;
		}

		boolean ret = con.equals(block) && getBlockOrFacadeMetadata(world, x, y, z, side) == meta;

		// no block obscuring this face
		if (obscuring == null) {
			return true;
		}

		// check that we aren't already connected outwards from this side
		ret &= !(obscuring.equals(block) && getBlockOrFacadeMetadata(world, x2, y2, z2, side) == meta && Configurations.connectInsideCTM);

		return ret;
	}

	public static int getBlockOrFacadeMetadata(IBlockAccess world, int x, int y, int z, int side) {
		Block blk = world.getBlock(x, y, z);
		if (blk instanceof IFacade) {
			return ((IFacade) blk).getFacadeMetadata(world, x, y, z, side);
		}
		return world.getBlockMetadata(x, y, z);
	}

	public static Block getBlockOrFacade(IBlockAccess world, int x, int y, int z, int side) {
		Block blk = world.getBlock(x, y, z);
		if (blk instanceof IFacade) {
			blk = ((IFacade) blk).getFacade(world, x, y, z, side);
		}
		return blk;
	}
}