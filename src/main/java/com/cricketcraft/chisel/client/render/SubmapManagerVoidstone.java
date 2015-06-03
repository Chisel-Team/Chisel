package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.cricketcraft.chisel.api.rendering.TextureType;
import com.cricketcraft.ctmlib.TextureSubmap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubmapManagerVoidstone extends SubmapManagerBase {

	// TODO there must be a better more generic way to do this...
	@SideOnly(Side.CLIENT)
	private class RenderBlocksVoidstone extends RenderBlocks {

		@Override
		public void renderFaceXNeg(Block block, double x, double y, double z, IIcon p_147798_8_) {
			super.renderFaceXNeg(block, x, y, z, getBase(x, y, z, ForgeDirection.WEST.ordinal()));
			super.renderFaceXNeg(block, x, y, z, icon);
		}

		@Override
		public void renderFaceXPos(Block block, double x, double y, double z, IIcon p_147764_8_) {
			super.renderFaceXPos(block, x, y, z, getBase(x, y, z, ForgeDirection.EAST.ordinal()));
			super.renderFaceXPos(block, x, y, z, icon);
		}

		@Override
		public void renderFaceYNeg(Block block, double x, double y, double z, IIcon p_147768_8_) {
			super.renderFaceYNeg(block, x, y, z, getBase(x, y, z, ForgeDirection.DOWN.ordinal()));
			super.renderFaceYNeg(block, x, y, z, icon);
		}

		@Override
		public void renderFaceYPos(Block block, double x, double y, double z, IIcon p_147806_8_) {
			super.renderFaceYPos(block, x, y, z, getBase(x, y, z, ForgeDirection.UP.ordinal()));
			super.renderFaceYPos(block, x, y, z, icon);
		}

		@Override
		public void renderFaceZNeg(Block block, double x, double y, double z, IIcon p_147761_8_) {
			super.renderFaceZNeg(block, x, y, z, getBase(x, y, z, ForgeDirection.NORTH.ordinal()));
			super.renderFaceZNeg(block, x, y, z, icon);
		}

		@Override
		public void renderFaceZPos(Block block, double x, double y, double z, IIcon p_147734_8_) {
			super.renderFaceZPos(block, x, y, z, getBase(x, y, z, ForgeDirection.SOUTH.ordinal()));
			super.renderFaceZPos(block, x, y, z, icon);
		}
	}

	@SideOnly(Side.CLIENT)
	private RenderBlocks rb;

	private IIcon icon;
	private TextureSubmap base;

	private IIcon getBase(double x, double y, double z, int side) {
		return TextureType.getVIcon(TextureType.V4, base, MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z), side);
	}

	private String texture;

	public SubmapManagerVoidstone(String texture) {
		this.texture = texture;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return icon;
	}

	@Override
	public void registerIcons(String modName, Block block, IIconRegister register) {
		icon = register.registerIcon(modName + ":" + texture);
		base = new TextureSubmap(register.registerIcon(modName + ":" + "animations/hadesX32"), 2, 2);
	}

	@Override
	public RenderBlocks createRenderContext(RenderBlocks rendererOld, Block block, IBlockAccess world) {
		if (rb == null) {
			rb = new RenderBlocksVoidstone();
		}
		rb.setRenderBoundsFromBlock(block);
		return rb;
	}
}
