package com.chiselmod.chisel.common.block.subblocks;

import com.chiselmod.chisel.common.block.BlockCarvable;

/**
 * Represents a version of a carvable chisel block
 *
 * @author minecreatr
 */
public interface ISubBlock {

    public String getName();

    public BlockCarvable getParent();
}
