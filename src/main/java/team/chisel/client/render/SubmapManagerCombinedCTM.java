package team.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Triple;

import team.chisel.ctmlib.RenderBlocksCTM;
import team.chisel.ctmlib.TextureSubmap;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.rendering.TextureType;
import com.cricketcraft.chisel.api.rendering.TextureType.AbstractSubmapManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubmapManagerCombinedCTM extends SubmapManagerBase {

	@SideOnly(Side.CLIENT)
	private class RenderBlocksCombinedCTM extends RenderBlocksCTM {

		@Override
		public void renderFaceXNeg(Block block, double x, double y, double z, IIcon icon) {
			setIcons(rType, x, y, z, ForgeDirection.WEST);
			super.renderFaceXNeg(block, x, y, z, inWorld ? submapSmall.getSubIcon(0, 0) : icon);
		}

		@Override
		public void renderFaceXPos(Block block, double x, double y, double z, IIcon icon) {
			setIcons(rType, x, y, z, ForgeDirection.EAST);
			super.renderFaceXPos(block, x, y, z, inWorld ? submapSmall.getSubIcon(0, 0) : icon);
		}

		@Override
		public void renderFaceYNeg(Block block, double x, double y, double z, IIcon icon) {
			setIcons(rType, x, y, z, ForgeDirection.DOWN);
			super.renderFaceYNeg(block, x, y, z, inWorld ? submapSmall.getSubIcon(0, 0) : icon);
		}

		@Override
		public void renderFaceYPos(Block block, double x, double y, double z, IIcon icon) {
			setIcons(rType, x, y, z, ForgeDirection.UP);
			super.renderFaceYPos(block, x, y, z, inWorld ? submapSmall.getSubIcon(0, 0) : icon);
		}

		@Override
		public void renderFaceZNeg(Block block, double x, double y, double z, IIcon icon) {
			setIcons(rType, x, y, z, ForgeDirection.NORTH);
			super.renderFaceZNeg(block, x, y, z, inWorld ? submapSmall.getSubIcon(0, 0) : icon);
		}

		@Override
		public void renderFaceZPos(Block block, double x, double y, double z, IIcon icon) {
			setIcons(rType, x, y, z, ForgeDirection.SOUTH);
			super.renderFaceZPos(block, x, y, z, inWorld ? submapSmall.getSubIcon(0, 0) : icon);
		}

		private void setIcons(TextureType type, double x, double y, double z, ForgeDirection side) {
			int ix = MathHelper.floor_double(x);
			int iy = MathHelper.floor_double(y);
			int iz = MathHelper.floor_double(z);
			if (type == TextureType.V4 || type == TextureType.V9) {
				submap = (TextureSubmap) TextureType.getVIcon(rType, SubmapManagerCombinedCTM.this.submap, ix, iy, iz, side.ordinal());
				submapSmall = (TextureSubmap) TextureType.getVIcon(rType, SubmapManagerCombinedCTM.this.smallSubmap, ix, iy, iz, side.ordinal());
			} else {
				submap = (TextureSubmap) TextureType.getRIcon(rType, SubmapManagerCombinedCTM.this.submap, ix, iy, iz, side.ordinal());
				submapSmall = (TextureSubmap) TextureType.getRIcon(rType, SubmapManagerCombinedCTM.this.smallSubmap, ix, iy, iz, side.ordinal());
			}
		}
	}

	private class Submap extends TextureSubmap {

		private TextureSubmap[][] submap;

		public Submap(IIcon base, int wh, TextureSubmap[][] submap) {
			super(base, wh, wh);
			this.submap = submap;
		}

		@Override
		public void texturesStitched() {
			for (int i = 0; i < icons.length; i++) {
				for (int j = 0; j < icons[i].length; j++) {
					icons[i][j] = submap[i][j];
					submap[i][j].texturesStitched();
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

	public SubmapManagerCombinedCTM(int meta, String texturePath, TextureType rType) {
		assert rType == TextureType.R16 || rType == TextureType.R9 || rType == TextureType.R4 || rType == TextureType.V4 || rType == TextureType.V9 : "Not a valid random type!";

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
	@SideOnly(Side.CLIENT)
	public RenderBlocks createRenderContext(RenderBlocks rendererOld, Block block, IBlockAccess world) {
		if (rb == null) {
			rb = new RenderBlocksCombinedCTM();
		}
		rb.setRenderBoundsFromBlock(block);
		return rb;
	}

	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(String modName, Block block, IIconRegister register) {
		IIcon base = register.registerIcon(modName + ":" + texturePath);
		int wh = (int) Math.sqrt(size);
		TextureSubmap[][] submaps = new TextureSubmap[wh][wh];
		TextureSubmap[][] submapsSmall = new TextureSubmap[wh][wh];
		for (int i = 0; i < size; i++) {
			AbstractSubmapManager manager = (AbstractSubmapManager) TextureType.CTMX.createManagerFor(CarvingUtils.getDefaultVariationFor(block, meta, 0), texturePath + "-" + i);
			manager.registerIcons(modName, block, register);
			Object cached = manager.getCachedObject();
			Triple<IIcon, TextureSubmap, TextureSubmap> triple = (Triple<IIcon, TextureSubmap, TextureSubmap>) cached;
			submaps[i % wh][i / wh] = triple.getMiddle();
			submapsSmall[i % wh][i / wh] = triple.getRight();
			if (i == 0) {
				defaultIcon = triple.getRight().getBaseIcon();
			}
		}
		submap = new Submap(base, wh, submaps);
		smallSubmap = new Submap(base, wh, submapsSmall);
	}
}
