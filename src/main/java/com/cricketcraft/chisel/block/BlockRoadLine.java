package com.cricketcraft.chisel.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.cricketcraft.chisel.client.render.BlockRoadLineRenderer;
import com.cricketcraft.chisel.config.Configurations;

public class BlockRoadLine extends Block {

	public IIcon aloneIcon;
	public IIcon halfLineIcon;
	public IIcon fullLineIcon;

	public BlockRoadLine() {
		super(Material.circuits);

		if (Configurations.useRoadLineTool) {
			this.setHarvestLevel(Configurations.getRoadLineTool,Configurations.roadLineToolLevel);
		}
		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.00390625f, 1.0f);
		// this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return BlockRoadLineRenderer.id;
	}

	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		return World.doesBlockHaveSolidTopSurface(par1World, par2, par3 - 1, par4) || par1World.getBlock(par2, par3 - 1, par4).equals(Blocks.glowstone);
	}

	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block block) {
		if (par1World.isRemote)
			return;

		if (!this.canPlaceBlockAt(par1World, par2, par3, par4)) {
			this.dropBlockAsItem(par1World, par2, par3, par4, 0, 0);
			par1World.setBlockToAir(par2, par3, par4);
		}

		super.onNeighborBlockChange(par1World, par2, par3, par4, block);
	}

	@Override
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = aloneIcon = reg.registerIcon("Chisel:line-marking/white-center");
		halfLineIcon = reg.registerIcon("Chisel:line-marking/white-side");
		fullLineIcon = reg.registerIcon("Chisel:line-marking/white-long");
	}

}
