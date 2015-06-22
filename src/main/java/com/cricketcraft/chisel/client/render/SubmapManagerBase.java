package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.cricketcraft.ctmlib.ISubmapManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public abstract class SubmapManagerBase implements ISubmapManager {

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return getIcon(side, world.getBlockMetadata(x, y, z));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public RenderBlocks createRenderContext(RenderBlocks rendererOld, Block block, IBlockAccess world) {
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void preRenderSide(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, ForgeDirection side) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void postRenderSide(RenderBlocks renderer, IBlockAccess world, int x, int y, int z, ForgeDirection side) {
	}
}
