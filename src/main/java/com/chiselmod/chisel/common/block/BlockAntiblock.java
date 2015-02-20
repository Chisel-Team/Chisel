package com.chiselmod.chisel.common.block;

import com.chiselmod.chisel.client.render.CTMBlockResources;
import com.chiselmod.chisel.common.block.subblocks.CTMSubBlock;
import com.google.common.collect.Lists;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import scala.actors.threadpool.Arrays;

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
