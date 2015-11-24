package team.chisel.client.render.ctm;

import static team.chisel.common.util.Dir.BOTTOM;
import static team.chisel.common.util.Dir.BOTTOM_LEFT;
import static team.chisel.common.util.Dir.BOTTOM_RIGHT;
import static team.chisel.common.util.Dir.LEFT;
import static team.chisel.common.util.Dir.RIGHT;
import static team.chisel.common.util.Dir.TOP;
import static team.chisel.common.util.Dir.TOP_LEFT;
import static team.chisel.common.util.Dir.TOP_RIGHT;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.EnumMap;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import team.chisel.api.IFacade;
import team.chisel.client.render.ctx.CTMBlockRenderContext;
import team.chisel.common.util.Dir;
import team.chisel.common.variation.PropertyVariation;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import team.chisel.common.variation.Variation;

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
	
    /**
     * The Uvs for the specific "magic number" value
     */
    public static final float[][] uvs = new float[][]{
            //Ctm texture
            {0, 0, 4, 4},// 0
            {4, 0, 8, 4}, // 1
            {8, 0, 12, 4}, // 2
            {12, 0, 16, 4}, // 3
            {0, 4, 4, 8}, // 4
            {4, 4, 8, 8}, // 5
            {8, 4, 12, 8}, // 6
            {12, 4, 16, 8}, // 7
            {0, 8, 4, 12}, // 8
            {4, 8, 8, 12}, // 9
            {8, 8, 12, 12}, // 10
            {12, 8, 16, 12}, // 11
            {0, 12, 4, 16}, // 12
            {4, 12, 8, 16}, // 13
            {8, 12, 12, 16}, // 14
            {12, 12, 16, 16}, // 15
            // Default texture
            {0, 0, 8, 8}, // 16
            {8, 0, 16, 8}, // 17
            {0, 8, 8, 16}, // 18
            {8, 8, 16, 16} // 19
    };
    
	/** Some hardcoded offset values for the different corner indeces */
	protected static int[] submapOffsets = { 4, 5, 1, 0 };
	/** For use via the Chisel 2 config only, altering this could cause unintended behavior */
	public static boolean disableObscuredFaceCheckConfig = false;

	public Optional<Boolean> disableObscuredFaceCheck = Optional.absent();

	protected TIntObjectMap<Dir[]> submapMap = new TIntObjectHashMap<Dir[]>();
	protected EnumMap<Dir, Boolean> connectionMap = Maps.newEnumMap(Dir.class);

	protected CTM() {
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

	/**
	 * @return The indeces of the typical 4x4 submap to use for the given face at the given location.
	 * 
	 *         Indeces are in counter-clockwise order starting at bottom left.
	 */
	public int[] getSubmapIndices(CTMBlockRenderContext ctx, EnumFacing side) {
		int[] ret = new int[] { 18, 19, 17, 16 };

		buildConnectionMap(ctx, side);

		// Map connections to submap indeces
		for (int i = 0; i < 4; i++) {
			fillSubmaps(ret, i);
		}

		return ret;
	}
	
    public static boolean isDefaultTexture(int id) {
        return (id == 16 || id == 17 || id == 18 || id == 19);
    }

	/**
	 * Builds the connection map and stores it in this CTM instance. The {@link #connected(Dir)}, {@link #connectedAnd(Dir...)}, and {@link #connectedOr(Dir...)} methods can be used to access it.
	 */
	public void buildConnectionMap(CTMBlockRenderContext ctx, EnumFacing side) {
		for (Dir dir : Dir.VALUES) {
			connectionMap.put(dir, dir.isConnected(ctx, side));
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
	 * Whether it is connected on the specified side
	 *
	 * @param world
	 *            The World
	 * @param pos
	 *            The Block pos
	 * @param facing
	 *            The Side
	 * @return Whether it is connected
	 */
	public boolean isConnected(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		return blockStatesEqual(getBlockOrFacade(world, pos, facing), getBlockOrFacade(world, pos.offset(facing), facing));
	}

	/**
	 * Whether it is connected on the specified side
	 *
	 * @param world
	 *            The World
	 * @param x
	 *            The Block x position
	 * @param y
	 *            The Block y position
	 * @param z
	 *            The Block z position
	 * @param facing
	 *            The Side
	 * @return Whether it is connected
	 */
	public boolean isConnected(IBlockAccess world, int x, int y, int z, EnumFacing facing) {
		return isConnected(world, new BlockPos(x, y, z), facing);
	}




    /**
     * Whether the two positions
     *
     * @param w
     * @param pos1
     * @param pos2
     * @return
     */
    public boolean isConnected(World w, BlockPos pos1, BlockPos pos2, PropertyVariation variation) {
        return areBlocksEqual(w.getBlockState(pos1), w.getBlockState(pos2), variation);
    }

	public IBlockState getBlockOrFacade(IBlockAccess world, BlockPos pos, EnumFacing side) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof IFacade) {
			return ((IFacade) state.getBlock()).getFacade(world, pos, side);
		}
		return state;
	}

	/**
	 * Returns whether the two blocks are equal ctm blocks
	 *
	 * @param state1 First state
	 * @param state2 Second state
	 * @return Whether they are the same block
	 */
	public static boolean areBlocksEqual(IBlockState state1, IBlockState state2, PropertyVariation variation) {
		return (state1.getBlock() == state2.getBlock() && ((Variation) state1.getValue(variation)).equals((Variation) state2.getValue(variation)));
	}


	/**
	 * Returns whether the two block states are equal to each other
	 *
	 * @param state1 The First Block State
	 * @param state2 The Second Block State
	 * @return Whether they are equal
	 */
	@SuppressWarnings("unchecked")
	public boolean blockStatesEqual(IBlockState state1, IBlockState state2) {
		for (IProperty p : (ImmutableSet<IProperty>) state1.getProperties().keySet()) {
			if (!state2.getProperties().containsKey(p)) {
				return false;
			}
			if (state1.getValue(p) != state2.getValue(p)) {
				return false;
			}
		}
		return state1.getBlock() == state2.getBlock();
	}

}