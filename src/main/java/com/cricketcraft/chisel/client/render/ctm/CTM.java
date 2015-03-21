package com.cricketcraft.chisel.client.render.ctm;

import com.cricketcraft.chisel.common.block.BlockCarvable;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

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

    /**
     * The Uvs for the specific "magic number" value
     */
    public static final float[][] uvs = new float[][] {
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

    public static boolean isDefaultTexture(int id){
        return (id==16||id==17||id==18||id==19);
    }



    public static int[] getSubmapIndices(IExtendedBlockState state, EnumFacing side) {
        if (!(state.getBlock() instanceof BlockCarvable)){
            return new int[] { 18, 19, 17, 16 };
        }
        boolean down = (Boolean)state.getValue(BlockCarvable.CONNECTED_DOWN);
        boolean up = (Boolean)state.getValue(BlockCarvable.CONNECTED_UP);
        boolean north = (Boolean)state.getValue(BlockCarvable.CONNECTED_NORTH);
        boolean south = (Boolean)state.getValue(BlockCarvable.CONNECTED_SOUTH);
        boolean east = (Boolean)state.getValue(BlockCarvable.CONNECTED_EAST);
        boolean west = (Boolean)state.getValue(BlockCarvable.CONNECTED_WEST);
        boolean north_east = (Boolean)state.getValue(BlockCarvable.CONNECTED_NORTH_EAST);
        boolean north_west = (Boolean)state.getValue(BlockCarvable.CONNECTED_NORTH_WEST);
        boolean north_up = (Boolean)state.getValue(BlockCarvable.CONNECTED_NORTH_UP);
        boolean north_down = (Boolean)state.getValue(BlockCarvable.CONNECTED_NORTH_DOWN);
        boolean south_east = (Boolean)state.getValue(BlockCarvable.CONNECTED_SOUTH_EAST);
        boolean south_west = (Boolean)state.getValue(BlockCarvable.CONNECTED_SOUTH_WEST);
        boolean south_up = (Boolean)state.getValue(BlockCarvable.CONNECTED_SOUTH_UP);
        boolean south_down = (Boolean)state.getValue(BlockCarvable.CONNECTED_SOUTH_DOWN);
        boolean east_up = (Boolean)state.getValue(BlockCarvable.CONNECTED_EAST_UP);
        boolean east_down = (Boolean)state.getValue(BlockCarvable.CONNECTED_EAST_DOWN);
        boolean west_up = (Boolean)state.getValue(BlockCarvable.CONNECTED_WEST_UP);
        boolean west_down = (Boolean)state.getValue(BlockCarvable.CONNECTED_WEST_DOWN);




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
        if (side == EnumFacing.DOWN) {
            b[0] = south_west; //South West
            b[1] = south; // South
            b[2] = south_east; // South east
            b[3] = west; // West
            b[4] = east; //East
            b[5] = north_west; // North West
            b[6] = north; // North
            b[7] = north_east; // North East
        } else if (side == EnumFacing.UP) {
            b[0] = north_west; //North West
            b[1] = north; // North
            b[2] = south_west; //North East
            b[3] = west; // West
            b[4] = east; // East
            b[5] = south_west; // South West
            b[6] = south; // South
            b[7] = south_east; //South East
        } else if (side == EnumFacing.NORTH) {
            b[0] = east_up; // Up East
            b[1] = up; // Up
            b[2] = west_up; // Up West
            b[3] = east_up; // East
            b[4] = west; // West
            b[5] = east_down; // Down East
            b[6] = down; // Down
            b[7] = west_down; // Down West
        } else if (side == EnumFacing.SOUTH) {
            b[0] = west_up; // Up West
            b[1] = up; // Up
            b[2] = east_up; // Up East
            b[3] = west; // West
            b[4] = east; // East
            b[5] = west_down; // Down West
            b[6] = down; // Down
            b[7] = east_down; // Down East
        } else if (side == EnumFacing.WEST) {
            b[0] = north_up; //Up North
            b[1] = up; // Up
            b[2] = south_up; // Up South
            b[3] = north; // North
            b[4] = south; // South
            b[5] = north_down; // Down North
            b[6] = down; // Down
            b[7] = south_down; // Down South
        } else if (side == EnumFacing.EAST) {
            b[0] = south_up; // Up South
            b[1] = up; // Up
            b[2] = north_up; // Up North
            b[3] = south; // South
            b[4] = north; // North
            b[5] = south_down; //Down South
            b[6] = down; // Down
            b[7] = north_down; // Down North
        }

        int[] ret = new int[] { 18, 19, 17, 16 };

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



}