package com.cricketcraft.chisel.block;

import net.minecraft.block.Block;

import com.cricketcraft.chisel.api.carving.CarvableHelper;

public interface IStairsCreator {

	public BlockCarvableStairs create(Block block, int meta, CarvableHelper helper);
}
