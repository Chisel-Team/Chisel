package com.cricketcraft.chisel.api.rendering;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.EnumMap;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.cricketcraft.chisel.api.IFacade;
import com.google.common.collect.Maps;

import static com.cricketcraft.chisel.api.rendering.Dir.*;

// @formatter:off
/**
 * The CTM renderer will draw the block's FACE using by assembling 4 quadrants from the 5 available block
 * textures.  The normal Texture.png is the blocks "unconnected" texture, and is used when CTM is disabled or the block
 * has nothing to connect to.  This texture has all of the outside corner quadrants  The texture-ctm.png contains the
 * rest of the quadrants.
 * <pre><blockquote>
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
 * </blockquote></pre>
 * combining { 18, 13,  9, 16 }, we can generate a texture connected to the right!
 * <pre><blockquote>
 * ╔══════╤═══════
 * ║      │      │
 * ║ 16   │ 9    │
 * ╟──────┼──────┼
 * ║      │      │
 * ║ 18   │ 13   │
 * ╚══════╧═══════
 * </blockquote></pre>
 *
 * combining { 18, 13, 11,  2 }, we can generate a texture, in the shape of an L (connected to the right, and up
 * <pre><blockquote>
 * ║ ─────┼───── ╚
 * ║      │      │
 * ║ 2    │ 11   │
 * ╟──────┼──────┼
 * ║      │      │
 * ║ 18   │ 13   │
 * ╚══════╧═══════
 * </blockquote></pre>
 *
 * HAVE FUN!
 * -CptRageToaster-
 */
// @formatter:on
public class CTM {

	/** Some hardcoded offset values for the different corner indeces */
	protected static int[] submapOffsets = { 4, 5, 1, 0 };
	protected static TIntObjectMap<Dir[]> submapMap = new TIntObjectHashMap<Dir[]>();
	protected static EnumMap<Dir, Boolean> connectionMap = Maps.newEnumMap(Dir.class);
	/** For use via the Chisel 2 config only, altering this could cause unintended behavior */
	public static boolean disableObscuredFaceCheckConfig = false;

	public Boolean disableObscuredFaceCheck = null;

	static {
		for (Dir dir : Dir.VALUES) {
			connectionMap.put(dir, false);
		}
		// Mapping the different corner indeces to their respective dirs
		submapMap.put(0, new Dir[] { BOTTOM, LEFT, BOTTOM_LEFT });
		submapMap.put(1, new Dir[] { BOTTOM, RIGHT, BOTTOM_RIGHT });
		submapMap.put(2, new Dir[] { TOP, RIGHT, TOP_RIGHT });
		submapMap.put(3, new Dir[] { TOP, LEFT, TOP_LEFT });
	}
	
	protected CTM() {
	}

	public static CTM getInstance() {
		return new CTM();
	}

	/**
	 * @return The indeces of the typical 4x4 submap to use for the given face at the given location.
	 * 
	 *         Indeces are in counter-clockwise order starting at bottom left.
	 */
	public int[] getSubmapIndices(IBlockAccess world, int x, int y, int z, int side) {
		int[] ret = new int[] { 18, 19, 17, 16 };

		if (world == null) {
			return ret;
		}

		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);

		buildConnectionMap(world, x, y, z, side, block, meta);

		// Map connections to submap indeces
		for (int i = 0; i < 4; i++) {
			fillSubmaps(ret, i);
		}

		return ret;
	}

	/**
	 * Builds the connection map and stores it in this CTM instance. The {@link #connected(Dir)}, {@link #connectedAnd(Dir...)}, and {@link #connectedOr(Dir...)} methods can be used to access it.
	 */
	public void buildConnectionMap(IBlockAccess world, int x, int y, int z, int side, Block block, int meta) {
		for (Dir dir : Dir.VALUES) {
			connectionMap.put(dir, dir.isConnected(this, world, x, y, z, side, block, meta));
		}
	}

	private void fillSubmaps(int[] ret, int idx) {
		Dir[] dirs = submapMap.get(idx);
		if (connectedOr(dirs[0], dirs[1])) {
			if (connectedAnd(dirs)) {
				// If all dirs are connected, we use the fully connected face,
				// the base offset value.
				ret[idx] = submapOffsets[idx];
			} else {
				// This is a bit magic-y, but basically the array is ordered so
				// the first dir requires an offset of 2, and the second dir
				// requires an offset of 8, plus the initial offset for the
				// corner.
				ret[idx] = submapOffsets[idx] + (connected(dirs[0]) ? 2 : 0) + (connected(dirs[1]) ? 8 : 0);
			}
		}
	}

	/**
	 * @param dir
	 *            The direction to check connection in.
	 * @return True if the cached connectionMap holds a connection in this {@link Dir direction}.
	 */
	public boolean connected(Dir dir) {
		return connectionMap.get(dir);
	}

	/**
	 * @param dirs
	 *            The directions to check connection in.
	 * @return True if the cached connectionMap holds a connection in <i><b>all</b></i> the given {@link Dir directions}.
	 */
	public boolean connectedAnd(Dir... dirs) {
		for (Dir dir : dirs) {
			if (!connected(dir)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param dirs
	 *            The directions to check connection in.
	 * @return True if the cached connectionMap holds a connection in <i><b>one of</b></i> the given {@link Dir directions}.
	 */
	private boolean connectedOr(Dir... dirs) {
		for (Dir dir : dirs) {
			if (connected(dir)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * A simple check for if the given block can connect to the given direction on the given side.
	 * 
	 * @param world
	 * @param x
	 *            The x coordinate of the block to check <i>against</i>. This is not the position of your block.
	 * @param y
	 *            The y coordinate of the block to check <i>against</i>. This is not the position of your block.
	 * @param z
	 *            The z coordinate of the block to check <i>against</i>. This is not the position of your block.
	 * @param side
	 *            The side of the block to check for connection status. This is <i>not</i> the direction to check in.
	 * @param block
	 *            The block to check against for connection.
	 * @param meta
	 *            The metadata to check against for connection.
	 * @return True if the given block can connect to the given location on the given side.
	 */
	public boolean isConnected(IBlockAccess world, int x, int y, int z, int side, Block block, int meta) {
		ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[side];
		return isConnected(world, x, y, z, dir, block, meta);
	}

	/**
	 * A simple check for if the given block can connect to the given direction on the given side.
	 * 
	 * @param world
	 * @param x
	 *            The x coordinate of the block to check <i>against</i>. This is not the position of your block.
	 * @param y
	 *            The y coordinate of the block to check <i>against</i>. This is not the position of your block.
	 * @param z
	 *            The z coordinate of the block to check <i>against</i>. This is not the position of your block.
	 * @param dir
	 *            The {@link ForgeDirection side} of the block to check for connection status. This is <i>not</i> the direction to check in.
	 * @param block
	 *            The block to check against for connection.
	 * @param meta
	 *            The metadata to check against for connection.
	 * @return True if the given block can connect to the given location on the given side.
	 */
	public boolean isConnected(IBlockAccess world, int x, int y, int z, ForgeDirection dir, Block block, int meta) {
		int x2 = x + dir.offsetX;
		int y2 = y + dir.offsetY;
		int z2 = z + dir.offsetZ;

		boolean disableObscured = disableObscuredFaceCheck == null ? disableObscuredFaceCheckConfig : disableObscuredFaceCheck;

		Block con = getBlockOrFacade(world, x, y, z, dir.ordinal());
		Block obscuring = disableObscured ? null : getBlockOrFacade(world, x2, y2, z2, dir.ordinal());

		// no block or a bad API user
		if (con == null) {
			return false;
		}

		boolean ret = con.equals(block) && getBlockOrFacadeMetadata(world, x, y, z, dir.ordinal()) == meta;

		// no block obscuring this face
		if (obscuring == null) {
			return ret;
		}

		// check that we aren't already connected outwards from this side
		ret &= !(obscuring.equals(block) && getBlockOrFacadeMetadata(world, x2, y2, z2, dir.ordinal()) == meta);

		return ret;
	}

	/**
	 * A utility method to access metadata from both the world and {@link IFacade} blocks in the world.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param side
	 *            The side to check for. -1 for unknown.
	 * @return The metadata at this position in the world, or inside an {@link IFacade} block.
	 */
	public int getBlockOrFacadeMetadata(IBlockAccess world, int x, int y, int z, int side) {
		Block blk = world.getBlock(x, y, z);
		if (blk instanceof IFacade) {
			return ((IFacade) blk).getFacadeMetadata(world, x, y, z, side);
		}
		return world.getBlockMetadata(x, y, z);
	}

	/**
	 * A utility method to access a block from both the world and {@link IFacade} blocks in the world.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param side
	 *            The side to check for. -1 for unknown.
	 * @return The block at this position in the world, or inside an {@link IFacade} block.
	 */
	public Block getBlockOrFacade(IBlockAccess world, int x, int y, int z, int side) {
		Block blk = world.getBlock(x, y, z);
		if (blk instanceof IFacade) {
			blk = ((IFacade) blk).getFacade(world, x, y, z, side);
		}
		return blk;
	}
}