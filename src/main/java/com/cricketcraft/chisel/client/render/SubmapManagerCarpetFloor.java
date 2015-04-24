package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.cricketcraft.chisel.api.rendering.ISubmapManager;
import com.cricketcraft.chisel.api.rendering.TextureSubmap;


public class SubmapManagerCarpetFloor implements ISubmapManager {
	
	private static RenderBlocksCTMCarpet rb = new RenderBlocksCTMCarpet();
	
	private TextureSubmap submap;
	private TextureSubmap submapSmall;
	private String color;
	
	public SubmapManagerCarpetFloor(String color) {
		this.color = color;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return submapSmall.getBaseIcon();
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return getIcon(side, world.getBlockMetadata(x, y, z));
	}

	@Override
	public void registerIcons(String modName, Block block, IIconRegister register) {
		String path = modName + ":carpet/" + color;
		submap = new TextureSubmap(register.registerIcon(path + "-ctm"), 4, 4);
		submapSmall = new TextureSubmap(register.registerIcon(path), 2, 2);
	}

	@Override
	public RenderBlocks createRenderContext(RenderBlocks rendererOld, IBlockAccess world) {
		rb.blockAccess = world;
		rb.renderMaxX = 1.0;
		rb.renderMaxY = 1f / 16f;
		rb.renderMaxZ = 1.0;

		rb.submap = submap;
		rb.submapSmall = submapSmall;

		rb.rendererOld = rendererOld;
		return rb;
	}
}
