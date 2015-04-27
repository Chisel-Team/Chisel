package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.cricketcraft.chisel.api.rendering.RenderBlocksCTM;
import com.cricketcraft.chisel.api.rendering.TextureSubmap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubmapManagerCarpetFloor extends SubmapManagerBase<RenderBlocksCTM> {

	@SideOnly(Side.CLIENT)
	private RenderBlocksCTM rb;

	private TextureSubmap submap;
	private TextureSubmap submapSmall;
	private String color;

	public SubmapManagerCarpetFloor(String color) {
		this.color = color;
		this.rb = new RenderBlocksCTM();
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
	@SideOnly(Side.CLIENT)
	public void registerIcons(String modName, Block block, IIconRegister register) {
		String path = modName + ":carpet/" + color;
		submap = new TextureSubmap(register.registerIcon(path + "-ctm"), 4, 4);
		submapSmall = new TextureSubmap(register.registerIcon(path), 2, 2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public RenderBlocksCTM createRenderContext(RenderBlocks rendererOld, IBlockAccess world) {
		rb.renderMaxY = 1f / 16f;
		rb.submap = submap;
		rb.submapSmall = submapSmall;
		return rb;
	}
}
