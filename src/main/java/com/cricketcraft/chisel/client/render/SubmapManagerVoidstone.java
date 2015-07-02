package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.rendering.TextureType;
import com.cricketcraft.ctmlib.ISubmapManager;
import com.cricketcraft.ctmlib.TextureSubmap;
import com.cricketcraft.ctmlib.RenderBlocksCTM;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubmapManagerVoidstone extends SubmapManagerBase {

	// TODO there must be a better more generic way to do this...
	@SideOnly(Side.CLIENT)
	private class RenderBlocksVoidstone extends RenderBlocksCTM {

		@Override
		public void renderFaceXNeg(Block block, double x, double y, double z, IIcon p_147798_8_) {
			super.renderFaceXNeg(block, x, y, z, getBase(x, y, z, ForgeDirection.WEST.ordinal()));
			super.renderFaceXNeg(block, x, y, z, p_147798_8_);
		}

		@Override
		public void renderFaceXPos(Block block, double x, double y, double z, IIcon p_147764_8_) {
			super.renderFaceXPos(block, x, y, z, getBase(x, y, z, ForgeDirection.EAST.ordinal()));
			super.renderFaceXPos(block, x, y, z, p_147764_8_);
		}

		@Override
		public void renderFaceYNeg(Block block, double x, double y, double z, IIcon p_147768_8_) {
			super.renderFaceYNeg(block, x, y, z, getBase(x, y, z, ForgeDirection.DOWN.ordinal()));
			super.renderFaceYNeg(block, x, y, z, p_147768_8_);
		}

		@Override
		public void renderFaceYPos(Block block, double x, double y, double z, IIcon p_147806_8_) {
			super.renderFaceYPos(block, x, y, z, getBase(x, y, z, ForgeDirection.UP.ordinal()));
			super.renderFaceYPos(block, x, y, z, p_147806_8_);
		}

		@Override
		public void renderFaceZNeg(Block block, double x, double y, double z, IIcon p_147761_8_) {
			super.renderFaceZNeg(block, x, y, z, getBase(x, y, z, ForgeDirection.NORTH.ordinal()));
			super.renderFaceZNeg(block, x, y, z, p_147761_8_);
		}

		@Override
		public void renderFaceZPos(Block block, double x, double y, double z, IIcon p_147734_8_) {
			super.renderFaceZPos(block, x, y, z, getBase(x, y, z, ForgeDirection.SOUTH.ordinal()));
			super.renderFaceZPos(block, x, y, z, p_147734_8_);
		}
	}

	@SideOnly(Side.CLIENT)
	private RenderBlocksCTM rb;

	private ISubmapManager overlay;
	private TextureSubmap base;

	private IIcon getBase(double x, double y, double z, int side) {
		return TextureType.getVIcon(TextureType.V4, base, MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z), side);
	}

	private String texture;
	private int meta;

	public SubmapManagerVoidstone(String texture, int meta) {
		this.texture = texture;
		this.meta = meta;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return overlay.getIcon(side, meta);
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return overlay.getIcon(world, x, y, z, side);
	}

	@Override
	public void registerIcons(String modName, Block block, IIconRegister register) {
		overlay = TextureType.getTypeFor(null, modName, texture).createManagerFor(CarvingUtils.getDefaultVariationFor(block, meta, 0), texture);
		overlay.registerIcons(modName, block, register);
		base = new TextureSubmap(register.registerIcon(modName + ":" + "animations/hadesX32"), 2, 2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public RenderBlocks createRenderContext(RenderBlocks rendererOld, Block block, IBlockAccess world) {
		if (rb == null) {
			rb = new RenderBlocksVoidstone();
		}
		RenderBlocks ctx = overlay.createRenderContext(rendererOld, block, world);
		rb.setRenderBoundsFromBlock(block);
		if (ctx instanceof RenderBlocksCTM) {
			rb.submap = ((RenderBlocksCTM) ctx).submap;
			rb.submapSmall = ((RenderBlocksCTM) ctx).submapSmall;
		}
		return rb;
	}
}
