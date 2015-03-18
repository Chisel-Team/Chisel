package com.cricketcraft.chisel.common.util;

import com.cricketcraft.chisel.client.render.CTMBlockResources;
import com.cricketcraft.chisel.common.block.BlockCarvable;
import com.cricketcraft.chisel.common.block.subblocks.ICTMSubBlock;
import com.cricketcraft.chisel.common.block.subblocks.ISubBlock;
import net.minecraft.block.Block;

/**
 * Utility for sub block stuff
 *
 * @author minecreatr
 */
public class SubBlockUtil {

    /**
     * Get the resources from the block and sub block id
     * @param block The Block
     * @param subBlock The sub block id
     * @return The Resources
     */
    public static CTMBlockResources getResources(Block block, int subBlock){
        if (block instanceof BlockCarvable){
            return getResources(((BlockCarvable) block).getSubBlock(subBlock));
        }
        return null;
    }

    /**
     * Get the CTMBlock resources for the given sub block
     * @param block The Sub block
     * @return The Block resources
     */
    public static CTMBlockResources getResources(ISubBlock block){
        if (block instanceof ICTMSubBlock){
            return ((ICTMSubBlock) block).getResources();
        }
        return null;
    }
}
