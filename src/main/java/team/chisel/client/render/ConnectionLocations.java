package team.chisel.client.render;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.Chisel;
import team.chisel.client.render.ctm.CTM;
import team.chisel.common.util.Dir;

/**
 * Represents all the different spot for connection locations for a ctm block
 */
@ParametersAreNonnullByDefault
public enum ConnectionLocations {

    UP(Dir.TOP),
    DOWN(Dir.BOTTOM),
    NORTH(EnumFacing.EAST, Dir.RIGHT),
    SOUTH(EnumFacing.EAST, Dir.LEFT),
    EAST(Dir.RIGHT),
    WEST(Dir.LEFT),
    
    NORTH_EAST(EnumFacing.UP, Dir.TOP_RIGHT),
    NORTH_WEST(EnumFacing.UP, Dir.TOP_LEFT),
    SOUTH_EAST(EnumFacing.UP, Dir.BOTTOM_RIGHT),
    SOUTH_WEST(EnumFacing.UP, Dir.BOTTOM_LEFT),
    
    NORTH_UP(EnumFacing.EAST, Dir.TOP_RIGHT),
    NORTH_DOWN(EnumFacing.EAST, Dir.BOTTOM_RIGHT),
    SOUTH_UP(EnumFacing.EAST, Dir.TOP_LEFT),
    SOUTH_DOWN(EnumFacing.EAST, Dir.BOTTOM_LEFT),
    
    EAST_UP(Dir.TOP_RIGHT),
    EAST_DOWN(Dir.BOTTOM_RIGHT),
    WEST_UP(Dir.TOP_LEFT),
    WEST_DOWN(Dir.BOTTOM_LEFT),
    
    NORTH_EAST_UP(EnumFacing.EAST, Dir.TOP_RIGHT, true),
    NORTH_EAST_DOWN(EnumFacing.EAST, Dir.BOTTOM_RIGHT, true),
    
    SOUTH_EAST_UP(EnumFacing.EAST, Dir.TOP_LEFT, true),
    SOUTH_EAST_DOWN(EnumFacing.EAST, Dir.BOTTOM_LEFT, true),
    
    SOUTH_WEST_UP(EnumFacing.WEST, Dir.TOP_LEFT, true),
    SOUTH_WEST_DOWN(EnumFacing.WEST, Dir.BOTTOM_LEFT, true),
    
    NORTH_WEST_UP(EnumFacing.WEST, Dir.TOP_RIGHT, true),
    NORTH_WEST_DOWN(EnumFacing.WEST, Dir.BOTTOM_RIGHT, true),
    
    UP_UP(EnumFacing.UP, null, true),
    DOWN_DOWN(EnumFacing.DOWN, null, true),
    NORTH_NORTH(EnumFacing.NORTH, null, true),
    SOUTH_SOUTH(EnumFacing.SOUTH, null, true),
    EAST_EAST(EnumFacing.EAST, null, true),
    WEST_WEST(EnumFacing.WEST, null, true),
    
    ;
    
    public static final ConnectionLocations[] VALUES = values();
    
    /**
     * The enum facing directions needed to get to this connection location
     */
    private final EnumFacing normal;
    private final @Nullable Dir dir;
    private boolean offset;

    private ConnectionLocations(@Nullable Dir dir) {
        this(EnumFacing.SOUTH, dir);
    }
    
    private ConnectionLocations(@Nullable Dir dir, boolean offset) {
        this(EnumFacing.SOUTH, dir, offset);
    }
    
    private ConnectionLocations(EnumFacing normal, @Nullable Dir dir){
        this(normal, dir, false);
    }
    
    private ConnectionLocations(EnumFacing normal, @Nullable Dir dir, boolean offset) {
        this.normal = normal;
        this.dir = dir;
        this.offset = offset;
    }

    public @Nullable Dir getDirForSide(EnumFacing facing){
        return dir == null ? null : dir.relativize(facing);
    }

    public @Nullable EnumFacing clipOrDestroy(EnumFacing direction) {
        EnumFacing[] dirs = dir == null ? new EnumFacing[] {normal, normal} : dir.getNormalizedDirs(direction);
        if (dirs[0] == direction) {
            return dirs.length > 1 ? dirs[1] : null;
        } else if (dirs.length > 1 && dirs[1] == direction) {
            return dirs[0];
        } else {
            return null;
        }
    }

    @SuppressWarnings("null")
    public BlockPos transform(BlockPos pos) {
        if (dir != null) {
            for (EnumFacing facing : dir.getNormalizedDirs(normal)) {
                pos = pos.offset(facing);
            }
        } else {
            pos = pos.offset(normal);
        }

        if (offset) {
            pos = pos.offset(normal);
        }
        return pos;
    }

    public static ConnectionLocations fromFacing(EnumFacing facing){
        switch (facing){
            case NORTH: return NORTH;
            case SOUTH: return SOUTH;
            case EAST: return EAST;
            case WEST: return WEST;
            case UP: return UP;
            case DOWN: return DOWN;
            default: return NORTH;
        }
    }

    public static EnumFacing toFacing(ConnectionLocations loc){
        switch (loc){
            case NORTH: return EnumFacing.NORTH;
            case SOUTH: return EnumFacing.SOUTH;
            case EAST: return EnumFacing.EAST;
            case WEST: return EnumFacing.WEST;
            case UP: return EnumFacing.UP;
            case DOWN: return EnumFacing.DOWN;
            default: return EnumFacing.NORTH;
        }
    }

    public static List<ConnectionLocations> decode(long data) {
        List<ConnectionLocations> list = new ArrayList<>();
        for (ConnectionLocations loc : values()) {
            if ((1 & (data >> loc.ordinal())) != 0) {
                list.add(loc);
            }
        }
        return list;
    }

    public long getMask(){
        return 1 << ordinal();
    }

    public static List<ConnectionLocations> getConnections(IBlockAccess world, BlockPos pos, ConnectionLocations[] values){
        List<ConnectionLocations> locs = new ArrayList<>();
        IBlockState state = world.getBlockState(pos);
        for (ConnectionLocations loc : values) {
            BlockPos second = loc.transform(pos);
            if (state.equals(CTM.getBlockOrFacade(world, second, null))){
                locs.add(loc);
            }
        }
        return locs;
    }

    public static long getData(IBlockAccess world, BlockPos pos, ConnectionLocations[] values){
        List<ConnectionLocations> locs = getConnections(world, pos, values);
        long data = 0;
        for (ConnectionLocations loc : locs){
            data = data | loc.getMask();
        }
        if (Chisel.debug) {
            String s = Long.toBinaryString(data);
            while (s.length() < 32) {
                s = "0" + s;
            }
            System.out.println(pos + ": " + s);
        }
        return data;
    }
}
