package com.cricketcraft.chisel.common.block;

import com.cricketcraft.chisel.client.render.CTMBlockResources;
import com.cricketcraft.chisel.common.block.subblocks.CTMSubBlock;
import net.minecraft.block.material.Material;

/**
 * Antiblock Block
 *
 * @author minecreatr
 */
public class BlockAntiblock extends BlockCarvable{

    public BlockAntiblock(){
        super(Material.rock, "antiblock", true);
    }

    @Override
    public void initSubBlocks(){
        addSubBlock(CTMSubBlock.generateSubBlock(this, "white", "White Antiblock"));
    }

    @Override
    public void preInitSubBlocks(){
        CTMBlockResources.preGenerateBlockResources(this, "white");
    }
}
