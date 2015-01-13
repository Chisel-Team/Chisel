package com.cricketcraft.chisel.block;

import net.minecraft.block.material.Material;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.carving.CarvableHelper;

public class BlockCarvableAntiBlock extends BlockCarvable implements ICarvable {

	public CarvableHelper carverHelper;

	public BlockCarvableAntiBlock() {
		super(Material.rock);
		carverHelper = new CarvableHelper();
	}

	@Override
	public int getRenderType() {
		return Chisel.renderCTMNoLightId;
	}

	/*
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	*/
}
