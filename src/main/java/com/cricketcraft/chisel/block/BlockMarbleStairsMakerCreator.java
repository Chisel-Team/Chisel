package com.cricketcraft.chisel.block;

import com.cricketcraft.chisel.carving.CarvableHelper;

import net.minecraft.block.Block;

public interface BlockMarbleStairsMakerCreator
{
    public BlockMarbleStairs create(Block block, int meta, CarvableHelper helper);
}
