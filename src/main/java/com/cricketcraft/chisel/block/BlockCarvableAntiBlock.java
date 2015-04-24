package com.cricketcraft.chisel.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.IBlockAccess;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.api.carving.CarvableHelper;

public class BlockCarvableAntiBlock extends BlockCarvableColor implements ICarvable {

	public CarvableHelper carverHelper;

	public BlockCarvableAntiBlock() {
		super(Material.rock);
		carverHelper = new CarvableHelper(this);
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
	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
	    return false;
	}
}
