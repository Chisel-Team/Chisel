package com.cricketcraft.chisel.common.init;

import com.cricketcraft.chisel.common.block.BlockAntiblock;
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
