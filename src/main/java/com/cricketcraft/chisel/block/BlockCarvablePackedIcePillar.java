package com.cricketcraft.chisel.block;

import net.minecraft.block.material.Material;

public class BlockCarvablePackedIcePillar extends BlockCarvablePillar {

	public BlockCarvablePackedIcePillar(Material m) {
		super(m);
		this.slipperiness = 0.98F;
	}
}
