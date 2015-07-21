package com.cricketcraft.chisel.common.block.subblocks;

import com.cricketcraft.chisel.client.render.CTMBlockResources;

/**
 * Represents a carvable connected textures sub block
 *
 * @author minecreatr
 */
public interface ICTMSubBlock extends ISubBlock {

    @Override
    public CTMBlockResources getResources();
}
