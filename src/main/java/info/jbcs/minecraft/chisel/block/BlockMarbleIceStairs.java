package info.jbcs.minecraft.chisel.block;

import info.jbcs.minecraft.chisel.CarvableHelper;
import info.jbcs.minecraft.chisel.Chisel;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockMarbleIceStairs extends BlockMarbleStairs {
	public BlockMarbleIceStairs(Block block,int meta, CarvableHelper helper) {
		super(block,meta,helper);
	
		slipperiness = 0.98F;
		setTickRandomly(true);
	}

	@Override
	public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
		Chisel.blockIce.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
	}

	@Override
	public int quantityDropped(Random par1Random) {
		return Chisel.blockIce.quantityDropped(par1Random);
	}
	@Override
	public int damageDropped(int i) {
		return 0;
	}
	
	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		Chisel.blockIce.updateTick(par1World, par2, par3, par4, par5Random);
	}

	@Override
	public int getMobilityFlag() {
		return 0;
	}
}
