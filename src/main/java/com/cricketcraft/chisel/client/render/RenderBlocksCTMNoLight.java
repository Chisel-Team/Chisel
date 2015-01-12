package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

public class RenderBlocksCTMNoLight extends RenderBlocks {

	RenderBlocksCTMNoLight() {
		super();
		resetVertices();
	}

	Tessellator tessellator;
	double[] X = new double[26];
	double[] Y = new double[26];
	double[] Z = new double[26];
	double[] U = new double[26];
	double[] V = new double[26];
	TextureSubmap submap;
	TextureSubmap submapSmall;
	RenderBlocks rendererOld;

	int bx, by, bz;

	@Override
	public boolean renderStandardBlock(Block block, int x, int y, int z) {
		bx = x;
		by = y;
		bz = z;

		tessellator = Tessellator.instance;
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

		tessellator.addTranslation(x, y, z);

		boolean res = super.renderStandardBlock(block, x, y, z);

		tessellator.addTranslation(-x, -y, -z);

		return res;
	}

	void side(int a, int b, int c, int d, int iconIndex, boolean flip) {
		IIcon icon = iconIndex >= 16 ? submapSmall.icons[iconIndex - 16] : submap.icons[iconIndex];

		double u0 = icon.getMaxU();
		double u1 = icon.getMinU();
		double v0 = icon.getMaxV();
		double v1 = icon.getMinV();

		U[a] = flip ? u1 : u1;
		U[b] = flip ? u0 : u1;
		U[c] = flip ? u0 : u0;
		U[d] = flip ? u1 : u0;

		V[a] = flip ? v1 : v1;
		V[b] = flip ? v1 : v0;
		V[c] = flip ? v0 : v0;
		V[d] = flip ? v0 : v1;

		vert(a);
		vert(b);
		vert(c);
		vert(d);
	}

	void vert(int index) {
		if (enableAO) {
			tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
			tessellator.setBrightness(0xF000F0);
		}

		tessellator.addVertexWithUV(X[index], Y[index], Z[index], U[index], V[index]);
	}

	@Override
	public void renderFaceXNeg(Block block, double x, double y, double z, IIcon icon) {
		if (rendererOld != null && rendererOld.hasOverrideBlockTexture()) {
			IIcon i = rendererOld.overrideBlockTexture;

			tessellator.addVertexWithUV(0.0, 1.0, 0.0, i.getMinU(), i.getMinV());
			tessellator.addVertexWithUV(0.0, 0.0, 0.0, i.getMinU(), i.getMaxV());
			tessellator.addVertexWithUV(0.0, 0.0, 1.0, i.getMaxU(), i.getMaxV());
			tessellator.addVertexWithUV(0.0, 1.0, 1.0, i.getMaxU(), i.getMinV());
		} else {
			int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 4);

			side(1, 14, 9, 23, tex[0], false);
			side(23, 9, 17, 5, tex[1], false);
			side(9, 19, 4, 17, tex[3], false);
			side(14, 0, 19, 9, tex[2], false);
		}
	}

	@Override
	public void renderFaceXPos(Block block, double x, double y, double z, IIcon icon) {
		if (rendererOld != null && rendererOld.hasOverrideBlockTexture()) {
			IIcon i = rendererOld.overrideBlockTexture;

			tessellator.addVertexWithUV(1.0, 1.0, 1.0, i.getMaxU(), i.getMinV());
			tessellator.addVertexWithUV(1.0, 0.0, 1.0, i.getMaxU(), i.getMaxV());
			tessellator.addVertexWithUV(1.0, 0.0, 0.0, i.getMinU(), i.getMaxV());
			tessellator.addVertexWithUV(1.0, 1.0, 0.0, i.getMinU(), i.getMinV());
		} else {
			int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 5);

			side(11, 21, 3, 15, tex[3], false);
			side(16, 7, 21, 11, tex[2], false);
			side(25, 11, 15, 2, tex[1], false);
			side(6, 16, 11, 25, tex[0], false);
		}
	}

	@Override
	public void renderFaceZNeg(Block block, double x, double y, double z, IIcon icon) {
		if (rendererOld != null && rendererOld.hasOverrideBlockTexture()) {
			IIcon i = rendererOld.overrideBlockTexture;

			tessellator.addVertexWithUV(1.0, 1.0, 0.0, i.getMaxU(), i.getMinV());
			tessellator.addVertexWithUV(1.0, 0.0, 0.0, i.getMaxU(), i.getMaxV());
			tessellator.addVertexWithUV(0.0, 0.0, 0.0, i.getMinU(), i.getMaxV());
			tessellator.addVertexWithUV(0.0, 1.0, 0.0, i.getMinU(), i.getMinV());
		} else {
			int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 2);

			side(2, 15, 8, 22, tex[0], false);
			side(15, 3, 18, 8, tex[2], false);
			side(8, 18, 0, 14, tex[3], false);
			side(22, 8, 14, 1, tex[1], false);
		}
	}

	@Override
	public void renderFaceZPos(Block block, double x, double y, double z, IIcon icon) {
		if (rendererOld != null && rendererOld.hasOverrideBlockTexture()) {
			IIcon i = rendererOld.overrideBlockTexture;

			tessellator.addVertexWithUV(0.0, 1.0, 1.0, i.getMinU(), i.getMinV());
			tessellator.addVertexWithUV(0.0, 0.0, 1.0, i.getMinU(), i.getMaxV());
			tessellator.addVertexWithUV(1.0, 0.0, 1.0, i.getMaxU(), i.getMaxV());
			tessellator.addVertexWithUV(1.0, 1.0, 1.0, i.getMaxU(), i.getMinV());
		} else {
			int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 3);

			side(17, 4, 20, 10, tex[2], false);
			side(5, 17, 10, 24, tex[0], false);
			side(24, 10, 16, 6, tex[1], false);
			side(10, 20, 7, 16, tex[3], false);
		}
	}

	@Override
	public void renderFaceYNeg(Block block, double x, double y, double z, IIcon icon) {
		if (rendererOld != null && rendererOld.hasOverrideBlockTexture()) {
			IIcon i = rendererOld.overrideBlockTexture;

			tessellator.addVertexWithUV(0.0, 0.0, 1.0, i.getMinU(), i.getMaxV());
			tessellator.addVertexWithUV(0.0, 0.0, 0.0, i.getMinU(), i.getMinV());
			tessellator.addVertexWithUV(1.0, 0.0, 0.0, i.getMaxU(), i.getMinV());
			tessellator.addVertexWithUV(1.0, 0.0, 1.0, i.getMaxU(), i.getMaxV());
		} else {
			int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 0);

			side(13, 21, 7, 20, tex[3], true);
			side(19, 13, 20, 4, tex[2], true);
			side(0, 18, 13, 19, tex[0], true);
			side(18, 3, 21, 13, tex[1], true);
		}
	}

	@Override
	public void renderFaceYPos(Block block, double x, double y, double z, IIcon icon) {
		if (rendererOld != null && rendererOld.hasOverrideBlockTexture()) {
			IIcon i = rendererOld.overrideBlockTexture;

			tessellator.addVertexWithUV(0.0, 1.0, 0.0, i.getMinU(), i.getMinV());
			tessellator.addVertexWithUV(0.0, 1.0, 1.0, i.getMinU(), i.getMaxV());
			tessellator.addVertexWithUV(1.0, 1.0, 1.0, i.getMaxU(), i.getMaxV());
			tessellator.addVertexWithUV(1.0, 1.0, 0.0, i.getMaxU(), i.getMinV());
		} else {
			int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 1);

			side(12, 24, 6, 25, tex[3], false);
			side(22, 12, 25, 2, tex[1], false);
			side(1, 23, 12, 22, tex[0], false);
			side(23, 5, 24, 12, tex[2], false);
		}
	}

	void resetVertices() {
		X[0] = 0;
		Z[0] = 0;
		Y[0] = 0;

		X[1] = 0;
		Z[1] = 0;
		Y[1] = 1;

		X[2] = 1;
		Z[2] = 0;
		Y[2] = 1;

		X[3] = 1;
		Z[3] = 0;
		Y[3] = 0;

		X[4] = 0;
		Z[4] = 1;
		Y[4] = 0;

		X[5] = 0;
		Z[5] = 1;
		Y[5] = 1;

		X[6] = 1;
		Z[6] = 1;
		Y[6] = 1;

		X[7] = 1;
		Z[7] = 1;
		Y[7] = 0;

		X[8] = 0.5;
		Z[8] = 0;
		Y[8] = 0.5;

		X[9] = 0;
		Z[9] = 0.5;
		Y[9] = 0.5;

		X[10] = 0.5;
		Z[10] = 1;
		Y[10] = 0.5;

		X[11] = 1;
		Z[11] = 0.5;
		Y[11] = 0.5;

		X[12] = 0.5;
		Z[12] = 0.5;
		Y[12] = 1;

		X[13] = 0.5;
		Z[13] = 0.5;
		Y[13] = 0;

		X[14] = 0;
		Z[14] = 0;
		Y[14] = 0.5;

		X[15] = 1;
		Z[15] = 0;
		Y[15] = 0.5;

		X[16] = 1;
		Z[16] = 1;
		Y[16] = 0.5;

		X[17] = 0;
		Z[17] = 1;
		Y[17] = 0.5;

		X[18] = 0.5;
		Z[18] = 0;
		Y[18] = 0;

		X[19] = 0;
		Z[19] = 0.5;
		Y[19] = 0;

		X[20] = 0.5;
		Z[20] = 1;
		Y[20] = 0;

		X[21] = 1;
		Z[21] = 0.5;
		Y[21] = 0;

		X[22] = 0.5;
		Z[22] = 0;
		Y[22] = 1;

		X[23] = 0;
		Z[23] = 0.5;
		Y[23] = 1;

		X[24] = 0.5;
		Z[24] = 1;
		Y[24] = 1;

		X[25] = 1;
		Z[25] = 0.5;
		Y[25] = 1;
	}
}
