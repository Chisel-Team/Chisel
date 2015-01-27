package com.cricketcraft.chisel.item;

import net.minecraft.block.Block;


public class ItemBlockPresent extends ItemCarvable {

	public ItemBlockPresent(Block block) {
		super(block);
	}
	
	@Override
	public int getMetadata(int i) {
		return 0;
	}
}
