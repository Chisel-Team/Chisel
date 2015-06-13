package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.TextureStitchEvent.Post;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Triple;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.rendering.TextureType;
import com.cricketcraft.chisel.api.rendering.TextureType.AbstractSubmapManager;
import com.cricketcraft.ctmlib.RenderBlocksCTM;
import com.cricketcraft.ctmlib.TextureSubmap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubmapManagerRCTM extends SubmapManagerBase {

	@SideOnly(Side.CLIENT)
	private class RenderBlocksRCTM extends RenderBlocksCTM {

		@Override
		public void renderFaceXNeg(Block block, double x, double y, double z, IIcon icon) {
			submap = (TextureSubmap) TextureType.getRIcon(rType, SubmapManagerRCTM.this.submap, MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z), ForgeDirection.WEST.ordinal());
			submapSmall = (TextureSubmap) TextureType.getRIcon(rType, SubmapManagerRCTM.this.smallSubmap, MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z), ForgeDirection.WEST.ordinal());
			super.renderFaceXNeg(block, x, y, z, icon);
		}

		@Override
		public void renderFaceXPos(Block block, double x, double y, double z, IIcon icon) {
			submap = (TextureSubmap) TextureType.getRIcon(rType, SubmapManagerRCTM.this.submap, MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z), ForgeDirection.EAST.ordinal());
			submapSmall = (TextureSubmap) TextureType.getRIcon(rType, SubmapManagerRCTM.this.smallSubmap, MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z), ForgeDirection.EAST.ordinal());
			super.renderFaceXPos(block, x, y, z, icon);
		}

		@Override
		public void renderFaceYNeg(Block block, double x, double y, double z, IIcon icon) {
			submap = (TextureSubmap) TextureType.getRIcon(rType, SubmapManagerRCTM.this.submap, MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z), ForgeDirection.DOWN.ordinal());
			submapSmall = (TextureSubmap) TextureType.getRIcon(rType, SubmapManagerRCTM.this.smallSubmap, MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z), ForgeDirection.DOWN.ordinal());
			super.renderFaceYNeg(block, x, y, z, icon);
		}

		@Override
		public void renderFaceYPos(Block block, double x, double y, double z, IIcon icon) {
			submap = (TextureSubmap) TextureType.getRIcon(rType, SubmapManagerRCTM.this.submap, MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z), ForgeDirection.UP.ordinal());
			submapSmall = (TextureSubmap) TextureType.getRIcon(rType, SubmapManagerRCTM.this.smallSubmap, MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z), ForgeDirection.UP.ordinal());
			super.renderFaceYPos(block, x, y, z, icon);
		}

		@Override
		public void renderFaceZNeg(Block block, double x, double y, double z, IIcon icon) {
			submap = (TextureSubmap) TextureType.getRIcon(rType, SubmapManagerRCTM.this.submap, MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z), ForgeDirection.NORTH.ordinal());
			submapSmall = (TextureSubmap) TextureType.getRIcon(rType, SubmapManagerRCTM.this.smallSubmap, MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z), ForgeDirection.UP.ordinal());
			super.renderFaceZNeg(block, x, y, z, icon);
		}

		@Override
		public void renderFaceZPos(Block block, double x, double y, double z, IIcon icon) {
			submap = (TextureSubmap) TextureType.getRIcon(rType, SubmapManagerRCTM.this.submap, MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z), ForgeDirection.SOUTH.ordinal());
			submapSmall = (TextureSubmap) TextureType.getRIcon(rType, SubmapManagerRCTM.this.smallSubmap, MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z), ForgeDirection.SOUTH.ordinal());
			super.renderFaceZPos(block, x, y, z, icon);
		}
	}
	
	private class Submap extends TextureSubmap {

		private TextureSubmap[][] submap;

		public Submap(IIcon base, int wh, TextureSubmap[][] submap) {
			super(base, wh, wh);
			this.submap = submap;
		}

		@Override
		public void TexturesStitched(Post event) {
			super.TexturesStitched(event);
			for (int i = 0; i < icons.length; i++) {
				for (int j = 0; j < icons[i].length; j++) {
					icons[i][j] = submap[i][j];
					submap[i][j].TexturesStitched(event);
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private RenderBlocksCTM rb;

	private TextureSubmap submap, smallSubmap;
	private int size;
	private String texturePath;
	private int meta;
	private IIcon defaultIcon;
	private TextureType rType;

	public SubmapManagerRCTM(int meta, String texturePath, TextureType rType) {
		assert rType == TextureType.R16 || rType == TextureType.R9 || rType == TextureType.R4
				: "Not a valid random type!";

		this.meta = meta;
		this.texturePath = texturePath;
		this.size = Integer.parseInt(rType.name().substring(1, rType.name().length())); // >.>
		this.rType = rType;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return defaultIcon;
	}

	@Override
	public RenderBlocks createRenderContext(RenderBlocks rendererOld, Block block, IBlockAccess world) {
		if (rb == null) {
			rb = new RenderBlocksRCTM();
		}
		rb.setRenderBoundsFromBlock(block);
		return rb;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void registerIcons(String modName, Block block, IIconRegister register) {
		IIcon base = register.registerIcon(modName + ":" + texturePath);
		TextureSubmap[][] submaps = new TextureSubmap[size][size];
		TextureSubmap[][] submapsSmall = new TextureSubmap[size][size];
		int wh = (int) Math.sqrt(size);
		for (int i = 0; i < size; i++) {
			AbstractSubmapManager manager = (AbstractSubmapManager) TextureType.CTMX.createManagerFor(CarvingUtils.getDefaultVariationFor(block, meta, 0), texturePath + "-ctm-" + i);
			manager.registerIcons(modName, block, register);
			Object cached = manager.getCachedObject();
			Triple<IIcon, TextureSubmap, TextureSubmap> triple = (Triple<IIcon, TextureSubmap, TextureSubmap>) cached;
			submaps[size % wh][size / wh] = triple.getMiddle();
			submapsSmall[size % wh][size / wh] = triple.getRight();
			if (i == 0) {
				defaultIcon = triple.getRight().getSubIcon(0, 0);
			}
		}
		submap = new Submap(base, wh, submaps);
		smallSubmap = new Submap(base, wh, submapsSmall);
	}
}
