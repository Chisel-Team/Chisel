package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.TextureStitchEvent.Post;

import org.apache.commons.lang3.tuple.Triple;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.rendering.TextureType;
import com.cricketcraft.chisel.api.rendering.TextureType.AbstractSubmapManager;
import com.cricketcraft.ctmlib.CTM;
import com.cricketcraft.ctmlib.RenderBlocksCTM;
import com.cricketcraft.ctmlib.RendererCTM;
import com.cricketcraft.ctmlib.TextureSubmap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubmapManagerRCTM extends SubmapManagerBase {

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
	private static RenderBlocksCTM rb;

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
			rb = new RenderBlocksCTM();
		}
		rb.setRenderBoundsFromBlock(block);
		// TODO figure out how to get side context...
		rb.submap = TextureType.getRIcon(rType, submap, RendererCTM.x, RendererCTM.y, RendererCTM.z, side);
		rb.submapSmall = null; // TODO
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
