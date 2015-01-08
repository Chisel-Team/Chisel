package com.cricketcraft.chisel.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.init.ChiselBlocks;

public class BlockMarbleIceStairs extends BlockMarbleStairs {

	public BlockMarbleIceStairs(Block block, int meta, CarvableHelper helper) {
		super(block, meta, helper);

		slipperiness = 0.98F;
		setTickRandomly(true);
	}

	@Override
	public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
		ChiselBlocks.ice.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
	}

	@Override
	public int quantityDropped(Random par1Random) {
		return ChiselBlocks.ice.quantityDropped(par1Random);
	}

	@Override
	public int damageDropped(int i) {
		return 0;
	}

	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		ChiselBlocks.ice.updateTick(par1World, par2, par3, par4, par5Random);
	}

	@Override
	public int getMobilityFlag() {
		return 0;
	}
}
