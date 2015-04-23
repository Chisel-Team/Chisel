package com.cricketcraft.chisel.client.render;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.EnumMap;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.cricketcraft.chisel.api.IFacade;
import com.cricketcraft.chisel.config.Configurations;
import com.google.common.collect.Maps;

import static com.cricketcraft.chisel.client.render.CTM.Dir.*;
import static net.minecraftforge.common.util.ForgeDirection.*;

/**
 * @formatter:off
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
		private static final Dir[] VALUES = values();

		private ForgeDirection[] dirs;

		private Dir(ForgeDirection... dirs) {
			this.dirs = dirs;
		}

		private boolean isConnected(CTM inst, IBlockAccess world, int x, int y, int z, int sideIdx, Block block, int meta) {
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

	/** Some hardcoded offset values for the different corner indeces */
	private int[] submapOffsets = { 4, 5, 1, 0 };
	private TIntObjectMap<Dir[]> submapMap = new TIntObjectHashMap<Dir[]>();
	private static EnumMap<Dir, Boolean> connectionMap = Maps.newEnumMap(Dir.class);
	
	public boolean disableObscuredFaceCheck = false;
	
	private CTM() {
		for (Dir dir : Dir.VALUES) {
			connectionMap.put(dir, false);
		}
		// Mapping the different corner indeces to their respective dirs
		submapMap.put(0, new Dir[] { BOTTOM, LEFT, BOTTOM_LEFT });
		submapMap.put(1, new Dir[] { BOTTOM, RIGHT, BOTTOM_RIGHT });
		submapMap.put(2, new Dir[] { TOP, RIGHT, TOP_RIGHT });
		submapMap.put(3, new Dir[] { TOP, LEFT, TOP_LEFT });
	}
	
	public static CTM getInstance() {
		return new CTM();
	}

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
	 * Builds the connection map and stores it in this class. The
	 * {@link #connected(Dir)}, {@link #connectedAnd(Dir...)}, and
	 * {@link #connectedOr(Dir...)} methods can be used to access it.
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

	public boolean connected(Dir dir) {
		return connectionMap.get(dir);
	}

	public boolean connectedAnd(Dir... dirs) {
		for (Dir dir : dirs) {
			if (!connected(dir)) {
				return false;
			}
		}
		return true;
	}

	private boolean connectedOr(Dir... dirs) {
		for (Dir dir : dirs) {
			if (connected(dir)) {
				return true;
			}
		}
		return false;
	}

	public boolean isConnected(IBlockAccess world, int x, int y, int z, int side, Block block, int meta) {
		ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[side];
		return isConnected(world, x, y, z, dir, block, meta);
	}

	public boolean isConnected(IBlockAccess world, int x, int y, int z, ForgeDirection dir, Block block, int meta) {
		int x2 = x + dir.offsetX;
		int y2 = y + dir.offsetY;
		int z2 = z + dir.offsetZ;

		Block con = getBlockOrFacade(world, x, y, z, dir.ordinal());
		Block obscuring = disableObscuredFaceCheck ? null : getBlockOrFacade(world, x2, y2, z2, dir.ordinal());

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
		ret &= !(obscuring.equals(block) && getBlockOrFacadeMetadata(world, x2, y2, z2, dir.ordinal()) == meta && Configurations.connectInsideCTM);

		return ret;
	}

	public int getBlockOrFacadeMetadata(IBlockAccess world, int x, int y, int z, int side) {
		Block blk = world.getBlock(x, y, z);
		if (blk instanceof IFacade) {
			return ((IFacade) blk).getFacadeMetadata(world, x, y, z, side);
		}
		return world.getBlockMetadata(x, y, z);
	}

	public Block getBlockOrFacade(IBlockAccess world, int x, int y, int z, int side) {
		Block blk = world.getBlock(x, y, z);
		if (blk instanceof IFacade) {
			blk = ((IFacade) blk).getFacade(world, x, y, z, side);
		}
		return blk;
	}
}