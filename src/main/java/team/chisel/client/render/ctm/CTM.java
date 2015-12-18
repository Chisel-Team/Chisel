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

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.IFacade;
import team.chisel.common.util.Dir;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

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
	protected int[] submapCache;

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
    public int[] createSubmapIndices(IBlockAccess world, BlockPos pos, EnumFacing side) {
		submapCache = new int[] { 18, 19, 17, 16 };

		if (world == null) {
            return submapCache;
        }

		buildConnectionMap(world, pos, side);

		// Map connections to submap indeces
		for (int i = 0; i < 4; i++) {
			fillSubmaps(i);
		}

		return submapCache;
	}
    
    public int[] getSubmapIndices() {
        return submapCache;
    }
	
    public static boolean isDefaultTexture(int id) {
        return (id == 16 || id == 17 || id == 18 || id == 19);
    }

    /**
     * Builds the connection map and stores it in this CTM instance. The {@link #connected(Dir)}, {@link #connectedAnd(Dir...)}, and {@link #connectedOr(Dir...)} methods can be used to access it.
     */
    public void buildConnectionMap(IBlockAccess world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        for (Dir dir : Dir.VALUES) {
            connectionMap.put(dir, dir.isConnected(this, world, pos, side, state));
        }
    }

	private void fillSubmaps(int idx) {
		Dir[] dirs = submapMap.get(idx);
		if (connectedOr(dirs[0], dirs[1])) {
			if (connectedAnd(dirs)) {
				// If all dirs are connected, we use the fully connected face,
				// the base offset value.
			    submapCache[idx] = submapOffsets[idx];
			} else {
				// This is a bit magic-y, but basically the array is ordered so
				// the first dir requires an offset of 2, and the second dir
				// requires an offset of 8, plus the initial offset for the
				// corner.
			    submapCache[idx] = submapOffsets[idx] + (connected(dirs[0]) ? 2 : 0) + (connected(dirs[1]) ? 8 : 0);
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
     * @param current
     *            The position of your block.
     * @param y
     *            The position of the block to check against.
     * @param dir
     *            The {@link EnumFacing side} of the block to check for connection status. This is <i>not</i> the direction to check in.
     * @return True if the given block can connect to the given location on the given side.
     */
    public boolean isConnected(IBlockAccess world, BlockPos current, BlockPos connection, EnumFacing dir) {

        IBlockState state = world.getBlockState(current);
        return isConnected(world, current, connection, dir, state);
    }

    /**
     * A simple check for if the given block can connect to the given direction on the given side.
     * 
     * @param world
     * @param current
     *            The position of your block.
     * @param y
     *            The position of the block to check against.
     * @param dir
     *            The {@link EnumFacing side} of the block to check for connection status. This is <i>not</i> the direction to check in.
     * @param state
     *            The state to check against for connection.
     * @return True if the given block can connect to the given location on the given side.
     */
    public boolean isConnected(IBlockAccess world, BlockPos current, BlockPos connection, EnumFacing dir, IBlockState state) {

//      if (CTMLib.chiselLoaded() && connectionBlocked(world, x, y, z, dir.ordinal())) {
//          return false;
//      }
      
        BlockPos pos2 = connection.add(dir.getDirectionVec());

        boolean disableObscured = disableObscuredFaceCheck.or(disableObscuredFaceCheckConfig);

        IBlockState con = getBlockOrFacade(world, connection, dir);
        IBlockState obscuring = disableObscured ? null : getBlockOrFacade(world, pos2, dir);

        // no block or a bad API user
        if (con == null) {
            return false;
        }

        boolean ret = con.equals(state);

        // no block obscuring this face
        if (obscuring == null) {
            return ret;
        }

        // check that we aren't already connected outwards from this side
        ret &= !obscuring.equals(state);

        return ret;
    }

//    private boolean connectionBlocked(IBlockAccess world, int x, int y, int z, int side) {
//        Block block = world.getBlock(x, y, z);
//        if (block instanceof IConnectable) {
//            return !((IConnectable) block).canConnectCTM(world, x, y, z, side);
//        }
//        return false;
//    }

	public IBlockState getBlockOrFacade(IBlockAccess world, BlockPos pos, EnumFacing side) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof IFacade) {
			return ((IFacade) state.getBlock()).getFacade(world, pos, side);
		}
		return state;
	}
}