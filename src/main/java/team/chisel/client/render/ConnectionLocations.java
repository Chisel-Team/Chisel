package team.chisel.client.render;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import team.chisel.client.render.ctm.CTM;
import team.chisel.common.util.Dir;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents all the different spot for connection locations for a ctm block
 */
public enum ConnectionLocations {

    UP(0, EnumFacing.UP, null, null, Dir.TOP, Dir.TOP, Dir.TOP, Dir.TOP),
    DOWN(1, EnumFacing.DOWN, null, null, Dir.BOTTOM, Dir.BOTTOM, Dir.BOTTOM, Dir.BOTTOM),
    NORTH(2, EnumFacing.NORTH, Dir.TOP, Dir.TOP, null, null, Dir.LEFT, Dir.RIGHT),
    SOUTH(3, EnumFacing.SOUTH, Dir.BOTTOM, Dir.BOTTOM, null, null, Dir.RIGHT, Dir.LEFT),
    EAST(4, EnumFacing.EAST, Dir.RIGHT, Dir.RIGHT, Dir.LEFT, Dir.RIGHT, null, null),
    WEST(5, EnumFacing.WEST, Dir.LEFT, Dir.LEFT, Dir.RIGHT, Dir.LEFT, null, null),
    NORTH_EAST(7, new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST}, Dir.TOP_RIGHT, Dir.TOP_RIGHT, null, null, null, null),
    NORTH_WEST(8, new EnumFacing[]{EnumFacing.NORTH, EnumFacing.WEST}, Dir.TOP_LEFT, Dir.TOP_LEFT, null, null, null, null),
    NORTH_UP(9, new EnumFacing[]{EnumFacing.NORTH, EnumFacing.UP}, null, null, null, null, Dir.TOP_LEFT, Dir.TOP_RIGHT),
    NORTH_DOWN(10, new EnumFacing[]{EnumFacing.NORTH, EnumFacing.DOWN}, null, null, null, null, Dir.BOTTOM_LEFT, Dir.BOTTOM_RIGHT),
    SOUTH_EAST(11, new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.EAST}, Dir.BOTTOM_RIGHT, Dir.BOTTOM_RIGHT, null, null, null, null),
    SOUTH_WEST(12, new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.WEST}, Dir.BOTTOM_LEFT, Dir.BOTTOM_LEFT, null, null, null, null),
    SOUTH_UP(13, new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.UP}, null, null, null, null, Dir.TOP_RIGHT, Dir.TOP_LEFT),
    SOUTH_DOWN(14, new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.DOWN}, null, null, null, null, Dir.BOTTOM_RIGHT, Dir.BOTTOM_LEFT),
    EAST_UP(15, new EnumFacing[]{EnumFacing.EAST, EnumFacing.UP}, null, null, Dir.TOP_LEFT, Dir.TOP_RIGHT, null, null),
    EAST_DOWN(16, new EnumFacing[]{EnumFacing.EAST, EnumFacing.DOWN}, null, null, Dir.BOTTOM_LEFT, Dir.BOTTOM_RIGHT, null, null),
    WEST_UP(17, new EnumFacing[]{EnumFacing.WEST, EnumFacing.UP}, null, null, Dir.TOP_RIGHT, Dir.TOP_LEFT, null, null),
    WEST_DOWN(18, new EnumFacing[]{EnumFacing.WEST, EnumFacing.DOWN}, null, null, Dir.BOTTOM_RIGHT, Dir.BOTTOM_LEFT);

    private int offset;

    /**
     * The enum facing directions needed to get to this connection location
     */
    private EnumFacing[] pos;

    /**
     * Represents the dir needed to get to this absolute CTM location from the direction ordinal index
     */
    private Dir[] directionRelations;

    private ConnectionLocations(int offset, EnumFacing[] pos, Dir... directions){
        this.offset = offset;
        this.pos = pos;
        this.directionRelations = directions;
    }

    private ConnectionLocations(int offset, EnumFacing pos, Dir... directions){
        this.offset = offset;
        this.pos = new EnumFacing[] {pos};
        this.directionRelations = directions;
    }

    public int getOffset(){
        return this.offset;
    }

    public Dir getDirForSide(EnumFacing facing){
        return this.directionRelations[facing.getIndex()];
    }

    public static List<ConnectionLocations> decode(long value){
        List<ConnectionLocations> list = new ArrayList<>();
        for (ConnectionLocations loc : values()){
            if ((1 & (value >> loc.getOffset())) != 0){
                list.add(loc);
            }
        }
        return list;
    }

    public long getMask(){
        return 1 << getOffset();
    }

    public static List<ConnectionLocations> getConnections(IBlockAccess world, BlockPos pos){
        List<ConnectionLocations> locs = new ArrayList<>();
        for (ConnectionLocations loc : values()){
            BlockPos second = pos;
            for (EnumFacing facing : loc.pos){
                second = second.offset(facing);
            }
            if (world.getBlockState(pos).equals(CTM.getBlockOrFacade(world, pos, loc.pos[0]))){
                locs.add(loc);
            }
        }
        return locs;
    }

    public static long getData(IBlockAccess world, BlockPos pos){
        List<ConnectionLocations> locs = getConnections(world, pos);
        long data = 0;
        for (ConnectionLocations loc : locs){
            data = data | loc.getMask();
        }
        return data;
    }


}
