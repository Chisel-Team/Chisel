package team.chisel.utils;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;

import java.util.List;

/**
 * Utility class for stuff to do with roofing
 */
public class RoofingUtil {

    /**
     * Get the sides sloping in the format [north, south, west, east]
     * @param world The World
     * @param x The X Coordinate
     * @param y The Y Coordinate
     * @param z The Z Coordinate
     * @param block The Block
     * @return The sloping boolean[]
     */
    public static boolean[] getSidesSloping(IBlockAccess world, int x, int y, int z, Block block){
        boolean north = world.getBlock(x, y+1, z-1) == block;
        boolean south = world.getBlock(x, y+1, z+1) == block;
        boolean west = world.getBlock(x-1, y+1, z) == block;
        boolean east = world.getBlock(x+1, y+1, z) == block;
        return new boolean[]{north, south, west, east};
    }

    /**
     * Get the corners sloping in the format [south west, south east, north east, north west]
     * @param world The World
     * @param x The X Coordinate
     * @param y The Y Coordinate
     * @param z The Z Coordinate
     * @param block The Block
     * @return The Corner sloping boolean[]
     */
    public static boolean[] getCornersSloping(IBlockAccess world, int x, int y, int z, Block block){
        boolean south_west = world.getBlock(x-1, y+1, z+1) == block && world.getBlock(x-1, y, z) == block && world.getBlock(x, y, z+1) == block;
        boolean south_east = world.getBlock(x+1, y+1, z+1) == block && world.getBlock(x+1, y, z) == block && world.getBlock(x, y, z+1) == block;
        boolean north_east = world.getBlock(x+1, y+1, z-1) == block && world.getBlock(x+1, y, z) == block && world.getBlock(x, y, z-1) == block;
        boolean north_west = world.getBlock(x-1, y+1, z-1) == block && world.getBlock(x-1, y, z) == block && world.getBlock(x, y, z-1) == block;
        return new boolean[] {south_west, south_east, north_east, north_west};
    }

    /**
     * Add the bounding boxes for the slopes to the bounding box list
     * @param list The Bounding box lists
     * @param slopes The Slopes
     */
    public static void addSlopeBoxesToList(List list, boolean[] slopes){
        //North
        if (slopes[0]){
            list.add(AxisAlignedBB.getBoundingBox(0, 0, 0.5, 1, 0.5, 1));
            list.add(AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 0.5));
        }
        //South
        else if (slopes[1]){
            list.add(AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 0.5, 0.5));
            list.add(AxisAlignedBB.getBoundingBox(0, 0, 0.5, 1, 1, 1));
        }
        //West
        else if (slopes[2]){
            list.add(AxisAlignedBB.getBoundingBox(0.5, 0, 0, 1, 0.5, 1));
            list.add(AxisAlignedBB.getBoundingBox(0, 0, 0, 0.5, 1, 1));
        }
        //East
        else if (slopes[3]){
            list.add(AxisAlignedBB.getBoundingBox(0, 0, 0, 0.5, 0.5, 1));
            list.add(AxisAlignedBB.getBoundingBox(0.5, 0, 0, 1, 1, 1));
        }
    }
}
