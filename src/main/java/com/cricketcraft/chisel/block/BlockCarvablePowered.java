package com.cricketcraft.chisel.block;

import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockCarvablePowered extends BlockCarvable {

	public BlockCarvablePowered(Material m) {
		super(m);
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this change based on its state.
	 */
	@Override
	public boolean canProvidePower() {
		return true;
	}

	/**
	 * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube returns true, standard redstone propagation rules will apply instead and this will
	 * not be called. Args: World, X, Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
	 */
	@Override
	public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return 15;
	}

}
