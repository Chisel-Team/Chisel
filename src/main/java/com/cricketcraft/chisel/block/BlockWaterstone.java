package com.cricketcraft.chisel.block;

import net.minecraft.block.material.Material;

public class BlockWaterstone extends BlockMarbleTexturedOre {

	public BlockWaterstone(Material mat, String baseIcon) {
		super(mat, baseIcon);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return true;
	}
}
