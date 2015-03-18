package com.cricketcraft.chisel.common.block.subblocks;

import com.cricketcraft.chisel.common.block.BlockCarvable;

/**
 * Represents a version of a carvable chisel block
 *
 * @author minecreatr
 */
public interface ISubBlock {

    public String getName();

    public BlockCarvable getParent();
}
