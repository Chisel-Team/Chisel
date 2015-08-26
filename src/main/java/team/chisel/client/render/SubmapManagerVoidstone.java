package team.chisel.client.render;

import team.chisel.ctmlib.ISubmapManager;
import team.chisel.ctmlib.RenderBlocksCTM;
import team.chisel.ctmlib.TextureSubmap;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.rendering.TextureType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubmapManagerVoidstone extends SubmapManagerBase {

	// TODO there must be a better more generic way to do this...
	@SideOnly(Side.CLIENT)
	private class RenderBlocksVoidstone extends RenderBlocksCTM {

		@Override
		public void renderFaceXNeg(Block block, double x, double y, double z, IIcon icon) {
			super.renderFaceXNeg(block, x, y, z, icon);
			renderMinX += 0.001;
			setOverrideBlockTexture(getBase(x, y, z, ForgeDirection.WEST.ordinal()));
			super.renderFaceXNeg(block, x, y, z, null);
			clearOverrideBlockTexture();
		}

		@Override
		public void renderFaceXPos(Block block, double x, double y, double z, IIcon icon) {
			super.renderFaceXPos(block, x, y, z, icon);
			setOverrideBlockTexture(getBase(x, y, z, ForgeDirection.EAST.ordinal()));
			renderMaxX -= 0.001;
			super.renderFaceXPos(block, x, y, z, null);
			clearOverrideBlockTexture();
		}

		@Override
		public void renderFaceYNeg(Block block, double x, double y, double z, IIcon icon) {
			super.renderFaceYNeg(block, x, y, z, icon);
			setOverrideBlockTexture(getBase(x, y, z, ForgeDirection.DOWN.ordinal()));
			renderMinY += 0.001;
			super.renderFaceYNeg(block, x, y, z, null);
			clearOverrideBlockTexture();
		}

		@Override
		public void renderFaceYPos(Block block, double x, double y, double z, IIcon icon) {
			super.renderFaceYPos(block, x, y, z, icon);
			setOverrideBlockTexture(getBase(x, y, z, ForgeDirection.UP.ordinal()));
			renderMaxY -= 0.001;
			super.renderFaceYPos(block, x, y, z, null);
			clearOverrideBlockTexture();
		}

		@Override
		public void renderFaceZNeg(Block block, double x, double y, double z, IIcon icon) {
			super.renderFaceZNeg(block, x, y, z, icon);
			setOverrideBlockTexture(getBase(x, y, z, ForgeDirection.NORTH.ordinal()));
			renderMinZ += 0.001;
			super.renderFaceZNeg(block, x, y, z, null);
			clearOverrideBlockTexture();
		}

		@Override
		public void renderFaceZPos(Block block, double x, double y, double z, IIcon icon) {
			super.renderFaceZPos(block, x, y, z, icon);
			setOverrideBlockTexture(getBase(x, y, z, ForgeDirection.SOUTH.ordinal()));
			renderMaxZ -= 0.001;
			super.renderFaceZPos(block, x, y, z, null);
			clearOverrideBlockTexture();
		}
	}

	@SideOnly(Side.CLIENT)
	private RenderBlocksVoidstone rb;

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
