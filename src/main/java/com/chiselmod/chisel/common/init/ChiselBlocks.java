package com.chiselmod.chisel.common.init;

import com.chiselmod.chisel.common.block.BlockAntiblock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Holds all the instances of blocks
 *
 * @author minecreatr
 */
public class ChiselBlocks {

    public static BlockAntiblock antiblock;

    public static void preInit(){
        antiblock = new BlockAntiblock();
        antiblock.preInitSubBlocks();
        GameRegistry.registerBlock(antiblock, "antiblock");

    }

    public static void init(){
        antiblock.initSubBlocks();
    }
}
