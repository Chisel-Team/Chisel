package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.cricketcraft.ctmlib.RenderBlocksCTM;
import com.cricketcraft.ctmlib.TextureSubmap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubmapManagerAntiblock extends SubmapManagerBase {

	@SideOnly(Side.CLIENT)
	private static class RenderBlocksCTMFullbright extends RenderBlocksCTM {

		@Override
		protected void fillLightmap(int bottomLeft, int bottomRight, int topRight, int topLeft) {
			int maxLight = 0xF000F0;
			super.fillLightmap(maxLight, maxLight, maxLight, maxLight);
		}

		@Override
		protected void fillColormap(float bottomLeft, float bottomRight, float topRight, float topLeft, float[][] map) {
			int color = 0xFFFFFF;
			super.fillColormap(color, color, color, color, map);
		}
	};

	@SideOnly(Side.CLIENT)
	private static RenderBlocksCTMFullbright rb;

	private String color;
	private TextureSubmap submap, submapSmall;

	public SubmapManagerAntiblock(String color) {
		this.color = color;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return submapSmall.getBaseIcon();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(String modName, Block block, IIconRegister register) {
		submap = new TextureSubmap(register.registerIcon(modName + ":antiblock/" + color + "-antiBlock-ctm"), 4, 4);
		submapSmall = new TextureSubmap(register.registerIcon(modName + ":antiblock/" + color + "-antiBlock"), 2, 2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public RenderBlocks createRenderContext(RenderBlocks rendererOld, Block block, IBlockAccess world) {
		if (rb == null) {
			rb = new RenderBlocksCTMFullbright();
		}
		rb.setRenderBoundsFromBlock(block);
		rb.submap = submap;
		rb.submapSmall = submapSmall;
		return rb;
	}
}
