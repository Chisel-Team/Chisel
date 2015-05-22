package com.cricketcraft.chisel.common.util;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.client.render.BlockResources;
import com.cricketcraft.chisel.client.render.CTMBlockResources;
import com.cricketcraft.chisel.client.render.IBlockResources;
import com.cricketcraft.chisel.common.block.BlockCarvable;
import com.cricketcraft.chisel.common.block.subblocks.ICTMSubBlock;
import com.cricketcraft.chisel.common.block.subblocks.ISubBlock;
import com.cricketcraft.chisel.common.variation.Variation;
import net.minecraft.block.Block;

/**
 * Utility for sub block stuff
 *
 * @author minecreatr
 */
public class SubBlockUtil {

    /**
     * Get the resources from the block and Variation
     * @param block The Block
     * @param v The Variation
     * @return The Resources
     */
    public static IBlockResources getResources(Block block, Variation v){
        if (block instanceof BlockCarvable){
            return getResources(((BlockCarvable) block).getSubBlock(v));
        }
        Chisel.logger.error("Block not instance of block carvable, instance of "+block.getClass());
        return null;
    }

    /**
     * Get the CTMBlock resources for the given sub block
     * @param block The Sub block
     * @return The Block resources
     */
    public static IBlockResources getResources(ISubBlock block){
        return block.getResources();
    }
}
