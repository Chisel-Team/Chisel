package team.chisel.client.render.ctm;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Optional;

import static team.chisel.common.util.Dir.*;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.IFacade;
import team.chisel.client.render.ConnectionLocations;
import team.chisel.common.util.Dir;

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
@ParametersAreNonnullByDefault
public class CTM {
	
    /**
     * The Uvs for the specific "magic number" value
     */
    public static final ISubmap[] uvs = new ISubmap[]{
            //Ctm texture
            new Submap(4, 4, 0, 0),   // 0
            new Submap(4, 4, 4, 0),   // 1
            new Submap(4, 4, 8, 0),   // 2
            new Submap(4, 4, 12, 0),  // 3
            new Submap(4, 4, 0, 4),   // 4
            new Submap(4, 4, 4, 4),   // 5
            new Submap(4, 4, 8, 4),   // 6
            new Submap(4, 4, 12, 4),  // 7
            new Submap(4, 4, 0, 8),   // 8
            new Submap(4, 4, 4, 8),   // 9
            new Submap(4, 4, 8, 8),   // 10
            new Submap(4, 4, 12, 8),  // 11
            new Submap(4, 4, 0, 12),  // 12
            new Submap(4, 4, 4, 12),  // 13
            new Submap(4, 4, 8, 12),  // 14
            new Submap(4, 4, 12, 12), // 15
            // Default texture
            new Submap(8, 8, 0, 0),   // 16
            new Submap(8, 8, 8, 0),   // 17
            new Submap(8, 8, 0, 8),   // 18
            new Submap(8, 8, 8, 8)    // 19
    };
    
    public static final ISubmap FULL_TEXTURE = new Submap(16, 16, 0, 0);
    
    // @formatter:on

	/** Some hardcoded offset values for the different corner indeces */
	protected static int[] submapOffsets = { 4, 5, 1, 0 };
	/** For use via the config only, altering this could cause unintended behavior */
	public static boolean disableObscuredFaceCheckConfig = false;

	public Optional<Boolean> disableObscuredFaceCheck = Optional.absent();

    // Mapping the different corner indeces to their respective dirs
	protected static final Dir[][] submapMap = new Dir[][] {
	    { BOTTOM, LEFT, BOTTOM_LEFT },
	    { BOTTOM, RIGHT, BOTTOM_RIGHT },
	    { TOP, RIGHT, TOP_RIGHT },
	    { TOP, LEFT, TOP_LEFT }
	};
	
	protected byte connectionMap;
	protected int[] submapCache = new int[] { 18, 19, 17, 16 };
	
	@Setter
	@Accessors(fluent = true, chain = true)
	protected boolean ignoreStates;

	public static CTM getInstance() {
		return new CTM();
	}

	/**
	 * @return The indeces of the typical 4x4 submap to use for the given face at the given location.
	 * 
	 *         Indeces are in counter-clockwise order starting at bottom left.
	 */
    public int[] createSubmapIndices(@Nullable IBlockAccess world, BlockPos pos, EnumFacing side) {
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

	public int[] createSubmapIndices(long data, EnumFacing side){
		submapCache = new int[] { 18, 19, 17, 16 };

		buildConnectionMap(data, side);

		// Map connections to submap indeces
		for (int i = 0; i < 4; i++) {
			fillSubmaps(i);
		}

		return submapCache;
	}
    
    public int[] getSubmapIndices() {
        return submapCache;
    }
    
    public long serialized() {
        return Byte.toUnsignedLong(connectionMap);
    }
	
    public static boolean isDefaultTexture(int id) {
        return (id == 16 || id == 17 || id == 18 || id == 19);
    }
    
    protected void setConnectedState(Dir dir, boolean connected) {
        if (connected) {
            connectionMap |= 1 << dir.ordinal();
        } else {
            connectionMap &= ~(1 << dir.ordinal());
        }
    }

    /**
     * Builds the connection map and stores it in this CTM instance. The {@link #connected(Dir)}, {@link #connectedAnd(Dir...)}, and {@link #connectedOr(Dir...)} methods can be used to access it.
     */
    public void buildConnectionMap(IBlockAccess world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        if (state.shouldSideBeRendered(world, pos, side)) {
            for (Dir dir : Dir.VALUES) {
                setConnectedState(dir, dir.isConnected(this, world, pos, side, state));
            }
        }
    }

    public void buildConnectionMap(long data, EnumFacing side) {
        connectionMap = 0; // Clear all connections
        List<ConnectionLocations> connections = ConnectionLocations.decode(data);
        for (ConnectionLocations loc : connections) {
            if (loc.getDirForSide(side) != null) {
                Dir dir = loc.getDirForSide(side);
                if (dir != null) {
                    setConnectedState(dir, true);
                }
            }
        }
    }

	@SuppressWarnings("null")
    private void fillSubmaps(int idx) {
		Dir[] dirs = submapMap[idx];
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
		return ((connectionMap >> dir.ordinal()) & 1) == 1;
	}

	/**
	 * @param dirs
	 *            The directions to check connection in.
	 * @return True if the cached connectionMap holds a connection in <i><b>all</b></i> the given {@link Dir directions}.
	 */
	@SuppressWarnings("null")
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
	@SuppressWarnings("null")
    public boolean connectedOr(Dir... dirs) {
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
    @SuppressWarnings({ "unused", "null" })
    public boolean isConnected(IBlockAccess world, BlockPos current, BlockPos connection, EnumFacing dir, IBlockState state) {

//      if (CTMLib.chiselLoaded() && connectionBlocked(world, x, y, z, dir.ordinal())) {
//          return false;
//      }
      
        BlockPos pos2 = connection.add(dir.getDirectionVec());

        boolean disableObscured = disableObscuredFaceCheck.or(disableObscuredFaceCheckConfig);

        IBlockState con = getBlockOrFacade(world, connection, dir);
        IBlockState obscuring = disableObscured ? null : getBlockOrFacade(world, pos2, dir);

        // bad API user
        if (con == null) {
            throw new IllegalStateException("Error, received null blockstate as facade from block " + world.getBlockState(connection));
        }

        boolean ret = ignoreStates ? con.getBlock() == state.getBlock() : con == state;

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

	public static IBlockState getBlockOrFacade(IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof IFacade) {
			return ((IFacade) state.getBlock()).getFacade(world, pos, side);
		}
		return state;
	}
}