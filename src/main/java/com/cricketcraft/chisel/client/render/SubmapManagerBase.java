package com.cricketcraft.chisel.client.render;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.cricketcraft.chisel.api.rendering.ISubmapManager;


public abstract class SubmapManagerBase<T extends RenderBlocks> implements ISubmapManager<T> {

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return getIcon(side, world.getBlockMetadata(x, y, z));
	}
	
	@Override
	public T createRenderContext(RenderBlocks rendererOld, IBlockAccess world) {
		return null;
	}

	@Override
	public void preRenderSide(T renderer, IBlockAccess world, int x, int y, int z, ForgeDirection side) {
	}

	@Override
	public void postRenderSide(T renderer, IBlockAccess world, int x, int y, int z, ForgeDirection side) {
	}
}
