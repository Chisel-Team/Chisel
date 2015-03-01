package com.cricketcraft.chisel.client.render;

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
 *  ┌─────────────────┐ ┌────────────────────────────────┐
 *  │ texture.png     │ │ texture-ctm.png                │
 *  │ ╔══════╤══════╗ │ │  ──────┼────── ║ ─────┼───── ║ │
 *  │ ║      │      ║ │ │ │      │      │║      │      ║ │
 *  │ ║ 16   │ 17   ║ │ │ │ 0    │ 1    │║ 2    │ 3    ║ │
 *  │ ╟──────┼──────╢ │ │ ┼──────┼──────┼╟──────┼──────╢ │
 *  │ ║      │      ║ │ │ │      │      │║      │      ║ │
 *  │ ║ 18   │ 19   ║ │ │ │ 4    │ 5    │║ 6    │ 7    ║ │
 *  │ ╚══════╧══════╝ │ │  ──────┼────── ║ ─────┼───── ║ │
 *  └─────────────────┘ │ ═══════╤═══════╝ ─────┼───── ╚ │
 *                      │ │      │      ││      │      │ │
 *                      │ │ 8    │ 9    ││ 10   │ 11   │ │
 *                      │ ┼──────┼──────┼┼──────┼──────┼ │
 *                      │ │      │      ││      │      │ │
 *                      │ │ 12   │ 13   ││ 14   │ 15   │ │
 *                      │ ═══════╧═══════╗ ─────┼───── ╔ │
 *                      └────────────────────────────────┘
 *
 * combining { 18, 13,  9, 16 }, we can generate a texture connected to the right!
 *
 *  ╔══════╤═══════
 *  ║      │      │
 *  ║ 16   │ 9    │
 *  ╟──────┼──────┼
 *  ║      │      │
 *  ║ 18   │ 13   │
 *  ╚══════╧═══════
 *
 *
 * combining { 18, 13, 11,  2 }, we can generate a texture, in the shape of an L (connected to the right, and up
 *
 *  ║ ─────┼───── ╚
 *  ║      │      │
 *  ║ 2    │ 11   │
 *  ╟──────┼──────┼
 *  ║      │      │
 *  ║ 18   │ 13   │
 *  ╚══════╧═══════
 *
 *
 * HAVE FUN!
 * -CptRageToaster-
 *
 * TODO: Everything
 */


public class CTM {

    /**
     *  The quads are ordered the same here, and in the renderer:
     *  1. Lower left
     *  2. Lower Right
     *  3. Top Right
     *  4. Top Left
     */
	static int submaps[][] = {
            { 18, 19, 17, 16 }, //  0 - No connection, with border

            { 18, 19,  3,  2 }, //  1 - Connected from above
            { 18, 13,  9, 16 }, //  2 - Connected to the right
            {  6,  7, 17, 16 }, //  3 - Connected from below
            { 12, 19, 17,  8 }, //  4 - Connected to the left

            {  6,  7,  3,  2 }, //  5 - ║
            { 12, 13,  9,  8 }, //  6 - ═
            { 18, 13, 11,  2 }, //  7 - ╚  with inside corner
            {  6, 15,  3, 16 }, //  8 - ╔  with inside corner
            { 14,  7, 17,  8 }, //  9 - ╗  with inside corner
            { 12, 19,  3, 10 }, // 10 - ╝  with inside corner

            { 18, 13,  1,  2 }, // 11 - ╚  no inside corner
            {  6,  5,  3, 16 }, // 12 - ╔  no inside corner
            {  4,  7, 17,  8 }, // 13 - ╗  no inside corner
            { 12, 19,  3,  0 }, // 14 - ╝  no inside corner

            {  6, 15, 11,  2 }, // 15 - ╠  with inside corners
            { 14, 15,  9,  8 }, // 16 - ╦  with inside corners
            { 14,  7,  3, 10 }, // 17 - ╣  with inside corners
            { 12, 13, 11, 10 }, // 18 - ╩  with inside corners

            {  6,  5, 11,  2 }, // 23 - ╠  with top right inside corner
            {  4, 15,  9,  8 }, // 24 - ╦  with bottom right inside corner
            { 14,  7,  3,  0 }, // 25 - ╣  with bottom left inside corner
            { 12, 13,  1, 10 }, // 26 - ╩  with top left inside corner

            {  6, 15,  1,  2 }, // 19 - ╠  with bottom right inside corner
            { 14,  5,  9,  8 }, // 20 - ╦  with bottom left inside corner
            {  4,  7,  3, 10 }, // 21 - ╣  with top left inside corner
            { 12, 13, 11,  0 }, // 22 - ╩  with top right inside corner

            {  6,  5,  1,  2 }, // 27 - ╠  no inside corners
            {  4,  5,  9,  8 }, // 28 - ╦  no inside corners
            {  4,  7,  3,  0 }, // 29 - ╣  no inside corners
            { 12, 13,  1,  0 }, // 30 - ╩  no inside corners

            { 14, 15, 11, 10 }, // 31 - ╬, with all inside corners

            { 14,  5, 11, 10 }, // 32 - ╬, with all but bottom right inside corner
            {  4, 15, 11, 10 }, // 33 - ╬, with all but bottom left inside corner
            { 14, 15, 11,  0 }, // 34 - ╬, with all but top left inside corner
            { 14, 15,  1, 10 }, // 35 - ╬, with all but top right inside corner

            {  4,  5, 11, 10 }, // 36 - ╬, with top inside corners
            {  4, 15, 11,  0 }, // 37 - ╬, with right inside corners
            { 14, 15,  1,  0 }, // 38 - ╬, with right inside corners
            { 14,  5,  1, 10 }, // 39 - ╬, with left inside corners
            {  4, 15,  1, 10 }, // 40 - ╬, with top left and bottom right inside corners
            { 14,  5, 11,  0 }, // 41 - ╬, with top right and bottom left inside corners

            {  4,  5,  1, 10 }, // 42 - ╬, with top left inside corner
            {  4,  5, 11,  0 }, // 43 - ╬, with top right inside corner
            {  4, 15,  1,  0 }, // 44 - ╬, with bottom right inside corner
            { 14,  5,  1,  0 }, // 45 - ╬, with bottom left inside corner

            {  4,  5,  1,  0 }, // 46 - ╬, no inside corners
    };

	public static int[] getSubmapIndices(IBlockAccess world, int x, int y, int z, int side) {
		int index = getTexture(world, x, y, z, side);

		return submaps[index];
	}

	public static int getTexture(IBlockAccess world, int x, int y, int z, int side) {
		if (world == null)
			return 0;

		int texture = 0;
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
            //TODO:
            b[0] = isConnected(world, x - 1, y, z, side, block, blockMetadata);
            b[1] = isConnected(world, x + 1, y, z, side, block, blockMetadata);
            b[2] = isConnected(world, x, y, z + 1, side, block, blockMetadata);
            b[3] = isConnected(world, x, y, z - 1, side, block, blockMetadata);
        } else if (side == 1) {
            //TODO:
			b[0] = isConnected(world, x - 1, y, z, side, block, blockMetadata);
			b[1] = isConnected(world, x + 1, y, z, side, block, blockMetadata);
			b[2] = isConnected(world, x, y, z - 1, side, block, blockMetadata);
			b[3] = isConnected(world, x, y, z + 1, side, block, blockMetadata);
		} else if (side == 2) {
            //TODO:
			b[0] = isConnected(world, x + 1, y, z, side, block, blockMetadata);
			b[1] = isConnected(world, x - 1, y, z, side, block, blockMetadata);
			b[2] = isConnected(world, x, y - 1, z, side, block, blockMetadata);
			b[3] = isConnected(world, x, y + 1, z, side, block, blockMetadata);
		} else if (side == 3) {
            //TODO:
			b[0] = isConnected(world, x - 1, y, z, side, block, blockMetadata);
			b[1] = isConnected(world, x + 1, y, z, side, block, blockMetadata);
			b[2] = isConnected(world, x, y - 1, z, side, block, blockMetadata);
			b[3] = isConnected(world, x, y + 1, z, side, block, blockMetadata);
		} else if (side == 4) {
            //TODO:
			b[0] = isConnected(world, x, y, z - 1, side, block, blockMetadata);
			b[1] = isConnected(world, x, y, z + 1, side, block, blockMetadata);
			b[2] = isConnected(world, x, y - 1, z, side, block, blockMetadata);
			b[3] = isConnected(world, x, y + 1, z, side, block, blockMetadata);
		} else if (side == 5) {
            //TODO:
			b[0] = isConnected(world, x, y, z + 1, side, block, blockMetadata);
			b[1] = isConnected(world, x, y, z - 1, side, block, blockMetadata);
			b[2] = isConnected(world, x, y - 1, z, side, block, blockMetadata);
			b[3] = isConnected(world, x, y + 1, z, side, block, blockMetadata);
		}

        //TODO: return something other than zero

		return texture;
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
		ret &= !(obscuring.equals(block) && getBlockOrFacadeMetadata(world, x2, y2, z2, side) == meta);
		
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
