package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

import static com.cricketcraft.chisel.client.render.RenderBlocksCTM.Vert.*;
import static com.cricketcraft.chisel.client.render.RenderBlocksCTM.SubSide.*;
import static com.cricketcraft.chisel.client.render.RenderBlocksCTM.Vert.Y_HALF_X;

public class RenderBlocksCTM extends RenderBlocks {

	/**
	 * An enum for all possible 26 sub-side vertices.
	 *
	 * The naming scheme is as follows:
	 *
	 * ZERO and ONE are special cases, they are the absolute min and absolute max of the block.
	 * X, Y, Z, or any combination means that the axes listed in the name are at 1.
	 * X, Y, Z, or any combination followed by HALF means that those axes are at 0.5.
	 * X, Y, Z, or any combination after a HALF means those axes are at 1.
	 */
	enum Vert {

		ZERO(0, 0, 0),
		XYZ(1, 1, 1),
		X(1, 0, 0),
		Y(0, 1, 0),
		Z(0, 0, 1),
		XY(1, 1, 0),
		YZ(0, 1, 1),
		XZ(1, 0, 1),
		X_HALF(0.5, 0, 0),
		Y_HALF(0, 0.5, 0),
		Z_HALF(0, 0, 0.5),
		XY_HALF(0.5, 0.5, 0),
		YZ_HALF(0, 0.5, 0.5),
		XZ_HALF(0.5, 0, 0.5),
		X_HALF_Y(0.5, 1, 0),
		X_HALF_Z(0.5, 0, 1),
		Y_HALF_X(1, 0.5, 0),
		Y_HALF_Z(0, 0.5, 1),
		Z_HALF_X(1, 0, 0.5),
		Z_HALF_Y(0, 1, 0.5),
		X_HALF_YZ(0.5, 1, 1),
		Y_HALF_XZ(1, 0.5, 1),
		Z_HALF_XY(1, 1, 0.5),
		XY_HALF_Z(0.5, 0.5, 1),
		YZ_HALF_X(1, 0.5, 0.5),
		XZ_HALF_Y(0.5, 1, 0.5);

		private double x, y, z;

		private Vert(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		void render(RenderBlocksCTM inst, int cacheID) {
			if (inst.enableAO) {
				inst.tessellator.setColorOpaque_F(inst.redCache[cacheID], inst.grnCache[cacheID], inst.bluCache[cacheID]);
				inst.tessellator.setBrightness(inst.lightingCache[cacheID]);
			}

			inst.tessellator.addVertexWithUV(x, y, z, inst.uCache[cacheID], inst.vCache[cacheID]);
		}
	}

	/**
	 * Each side is divided into 4 sub-sides. LB(left bottom), RB(right bottom), LT(right top), and RT(right top).
	 *
	 * Each sub-side contains 4 {@link Vert} objects representing its position on the block.
	 */
	enum SubSide {
		XNEG_LB(ZERO, Z_HALF, YZ_HALF, Y_HALF), XNEG_RB(Z_HALF, Z, Y_HALF_Z, YZ_HALF), XNEG_RT(YZ_HALF, Y_HALF_Z, YZ, Z_HALF_Y), XNEG_LT(Y_HALF, YZ_HALF, Z_HALF_Y, Y),
		XPOS_LB(XZ, Z_HALF_X, YZ_HALF_X, Y_HALF_XZ), XPOS_RB(Z_HALF_X, X, Y_HALF_X, YZ_HALF_X), XPOS_RT(YZ_HALF_X, Y_HALF_X, XY, Z_HALF_XY), XPOS_LT(Y_HALF_XZ, YZ_HALF_X, Z_HALF_XY, XYZ),

		ZNEG_LB(X, X_HALF, XY_HALF, Y_HALF_X), ZNEG_RB(X_HALF, ZERO, Y_HALF, XY_HALF), ZNEG_RT(XY_HALF, Y_HALF, Y, X_HALF_Y), ZNEG_LT(Y_HALF_X, XY_HALF, X_HALF_Y, XY),
		ZPOS_LB(Z, X_HALF_Z, XY_HALF_Z, Y_HALF_Z), ZPOS_RB(X_HALF_Z, XZ, Y_HALF_XZ, XY_HALF_Z), ZPOS_RT(XY_HALF_Z, Y_HALF_XZ, XYZ, X_HALF_YZ), ZPOS_LT(Y_HALF_Z, XY_HALF_Z, X_HALF_YZ, YZ),

		YNEG_LB(ZERO, X_HALF, XZ_HALF, Z_HALF), YNEG_RB(X_HALF, X, Z_HALF_X, XZ_HALF), YNEG_RT(XZ_HALF, Z_HALF_X, XZ, X_HALF_Z), YNEG_LT(Z_HALF, XZ_HALF, X_HALF_Z, Z),
		YPOS_LB(YZ, X_HALF_YZ, XZ_HALF_Y, Z_HALF_Y), YPOS_RB(X_HALF_YZ, XYZ, Z_HALF_XY, XZ_HALF_Y), YPOS_RT(XZ_HALF_Y, Z_HALF_XY, XY, X_HALF_Y), YPOS_LT(Z_HALF_Y, XZ_HALF_Y, X_HALF_Y, Y);
		private Vert xmin, xmax, ymin, ymax;

		SubSide(Vert xmin, Vert ymin, Vert ymax, Vert xmax) {
			this.xmin = xmin;
			this.ymin = ymin;
			this.ymax = ymax;
			this.xmax = xmax;
		}

		void render(RenderBlocksCTM inst) {
			xmin.render(inst, 0);
			ymin.render(inst, 1);
			ymax.render(inst, 2);
			xmax.render(inst, 3);
		}
	}

	// globals added to save the JVM some trouble. No need to constantly create
	// and destroy ints if we don't have to
	int blockLightBitChannel = 0;
	int redBitChannel = 0;
	int greenBitChannel = 0;
	int blueBitChannel = 0;
	int sunlightBitChannel = 0;

	RenderBlocksCTM() {
		super();
	}

	Tessellator tessellator;
	double[] uCache = new double[4];
	double[] vCache = new double[4];
	int[] lightingCache = new int[4];
	float[] redCache = new float[4];
	float[] grnCache = new float[4];
	float[] bluCache = new float[4];
	TextureSubmap submap;
	TextureSubmap submapSmall;
	RenderBlocks rendererOld;

	int[][] lightmap = new int[3][3];
	float[][] redmap = new float[3][3];
	float[][] grnmap = new float[3][3];
	float[][] blumap = new float[3][3];

	int bx, by, bz;

	@Override public boolean renderStandardBlock(Block block, int x, int y, int z) {
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

	/* This method fills a 3x3 grid of light values based on the four corners, by averaging them together.
	 *
	 * 2  TL   x    TR
	 *
	 * 1  x    x    x
	 *
	 * 0  BL   x    BR
	 *    0    1    2
	 *
	 * Note: Variable names mean nothing... don't touch
	 *
	 * ._.
	 *
	 * shakes fist in anger
	 */
	void fillLightmap(int bottomLeft, int bottomRight, int topRight, int topLeft) {
		lightmap[0][0] = bottomLeft;
		lightmap[2][0] = bottomRight;
		lightmap[2][2] = topRight;
		lightmap[0][2] = topLeft;

		lightmap[1][0] = avg(bottomLeft, bottomRight);
		lightmap[2][1] = avg(bottomRight, topRight);
		lightmap[1][2] = avg(topRight, topLeft);
		lightmap[0][1] = avg(topLeft, bottomLeft);

		lightmap[1][1] = avg(bottomLeft, bottomRight, topRight, topLeft);
	}

	void fillColormap(float bottomLeft, float bottomRight, float topRight, float topLeft, float[][] map) {
		map[0][0] = bottomLeft;
		map[2][0] = bottomRight;
		map[2][2] = topRight;
		map[0][2] = topLeft;

		map[1][0] = (bottomLeft + bottomRight) / 2.0F;
		map[2][1] = (bottomRight + topRight) / 2.0F;
		map[1][2] = (topRight + topLeft) / 2.0F;
		map[0][1] = (topLeft + bottomLeft) / 2.0F;

		map[1][1] = (bottomLeft + bottomRight + topRight + topLeft) / 4.0F;
	}

	void getLight(int x, int y) {
		lightingCache[0] = lightmap[0 + x][0 + y];
		lightingCache[1] = lightmap[1 + x][0 + y];
		lightingCache[2] = lightmap[1 + x][1 + y];
		lightingCache[3] = lightmap[0 + x][1 + y];

		redCache[0] = redmap[0 + x][0 + y];
		redCache[1] = redmap[1 + x][0 + y];
		redCache[2] = redmap[1 + x][1 + y];
		redCache[3] = redmap[0 + x][1 + y];

		grnCache[0] = grnmap[0 + x][0 + y];
		grnCache[1] = grnmap[1 + x][0 + y];
		grnCache[2] = grnmap[1 + x][1 + y];
		grnCache[3] = grnmap[0 + x][1 + y];

		bluCache[0] = blumap[0 + x][0 + y];
		bluCache[1] = blumap[1 + x][0 + y];
		bluCache[2] = blumap[1 + x][1 + y];
		bluCache[3] = blumap[0 + x][1 + y];
	}

	/**
	 * This works around a bug in CLC atm
	 */
	int avg(int... lightVals) {
		blockLightBitChannel = 0;
		redBitChannel = 0;
		greenBitChannel = 0;
		blueBitChannel = 0;
		sunlightBitChannel = 0;

		for (int light : lightVals) {
			blockLightBitChannel += (light & 0xFF);
			redBitChannel += (light & 0xF00);
			greenBitChannel += (light & 0xF000);
			blueBitChannel += (light & 0xF0000);
			sunlightBitChannel += (light & 0xF00000);
		}

		return ((blockLightBitChannel / lightVals.length) & 0xFF) | ((redBitChannel / lightVals.length) & 0xF00) | ((greenBitChannel / lightVals.length) & 0xF000) | (
				(blueBitChannel / lightVals.length) & 0xF0000) | ((sunlightBitChannel / lightVals.length) & 0xF00000);
	}

	void side(SubSide side, int iconIndex) {
		IIcon icon = iconIndex >= 16 ? submapSmall.icons[iconIndex - 16] : submap.icons[iconIndex];

		double umax = icon.getMaxU();
		double umin = icon.getMinU();
		double vmax = icon.getMaxV();
		double vmin = icon.getMinV();

		uCache[0] = umin;
		uCache[1] = umax;
		uCache[2] = umax;
		uCache[3] = umin;

		vCache[0] = vmax;
		vCache[1] = vmax;
		vCache[2] = vmin;
		vCache[3] = vmin;

		side.render(this);
	}

	@Override public void renderFaceXNeg(Block block, double x, double y, double z, IIcon icon) {
		if (rendererOld != null && rendererOld.hasOverrideBlockTexture()) {
			IIcon i = rendererOld.overrideBlockTexture;

			tessellator.addVertexWithUV(0.0, 1.0, 0.0, i.getMinU(), i.getMinV());
			tessellator.addVertexWithUV(0.0, 0.0, 0.0, i.getMinU(), i.getMaxV());
			tessellator.addVertexWithUV(0.0, 0.0, 1.0, i.getMaxU(), i.getMaxV());
			tessellator.addVertexWithUV(0.0, 1.0, 1.0, i.getMaxU(), i.getMinV());
		} else {
			int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 4);

			fillLightmap(brightnessBottomRight, brightnessTopRight, brightnessTopLeft, brightnessBottomLeft);
			fillColormap(colorRedBottomRight, colorRedTopRight, colorRedTopLeft, colorRedBottomLeft, redmap);
			fillColormap(colorGreenBottomRight, colorGreenTopRight, colorGreenTopLeft, colorGreenBottomLeft, grnmap);
			fillColormap(colorBlueBottomRight, colorBlueTopRight, colorBlueTopLeft, colorBlueBottomLeft, blumap);

			getLight(0, 0);
			side(XNEG_LB, tex[0]);
			getLight(1, 0);
			side(XNEG_RB, tex[1]);
			getLight(1, 1);
			side(XNEG_RT, tex[2]);
			getLight(0, 1);
			side(XNEG_LT, tex[3]);
		}
	}

	@Override public void renderFaceXPos(Block block, double x, double y, double z, IIcon icon) {
		if (rendererOld != null && rendererOld.hasOverrideBlockTexture()) {
			IIcon i = rendererOld.overrideBlockTexture;

			tessellator.addVertexWithUV(1.0, 1.0, 1.0, i.getMaxU(), i.getMinV());
			tessellator.addVertexWithUV(1.0, 0.0, 1.0, i.getMaxU(), i.getMaxV());
			tessellator.addVertexWithUV(1.0, 0.0, 0.0, i.getMinU(), i.getMaxV());
			tessellator.addVertexWithUV(1.0, 1.0, 0.0, i.getMinU(), i.getMinV());
		} else {
			int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 5);

			fillLightmap(brightnessTopLeft, brightnessBottomLeft, brightnessBottomRight, brightnessTopRight);
			fillColormap(colorRedTopLeft, colorRedBottomLeft, colorRedBottomRight, colorRedTopRight, redmap);
			fillColormap(colorGreenTopLeft, colorGreenBottomLeft, colorGreenBottomRight, colorGreenTopRight, grnmap);
			fillColormap(colorBlueTopLeft, colorBlueBottomLeft, colorBlueBottomRight, colorBlueTopRight, blumap);
			getLight(0, 0);
			side(XPOS_LB, tex[0]);
			getLight(1, 0);
			side(XPOS_RB, tex[1]);
			getLight(1, 1);
			side(XPOS_RT, tex[2]);
			getLight(0, 1);
			side(XPOS_LT, tex[3]);
		}
	}

	@Override public void renderFaceZNeg(Block block, double x, double y, double z, IIcon icon) {
		if (rendererOld != null && rendererOld.hasOverrideBlockTexture()) {
			IIcon i = rendererOld.overrideBlockTexture;

			tessellator.addVertexWithUV(1.0, 1.0, 0.0, i.getMaxU(), i.getMinV());
			tessellator.addVertexWithUV(1.0, 0.0, 0.0, i.getMaxU(), i.getMaxV());
			tessellator.addVertexWithUV(0.0, 0.0, 0.0, i.getMinU(), i.getMaxV());
			tessellator.addVertexWithUV(0.0, 1.0, 0.0, i.getMinU(), i.getMinV());
		} else {
			int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 2);

			fillLightmap(brightnessBottomRight, brightnessTopRight, brightnessTopLeft, brightnessBottomLeft);
			fillColormap(colorRedBottomRight, colorRedTopRight, colorRedTopLeft, colorRedBottomLeft, redmap);
			fillColormap(colorGreenBottomRight, colorGreenTopRight, colorGreenTopLeft, colorGreenBottomLeft, grnmap);
			fillColormap(colorBlueBottomRight, colorBlueTopRight, colorBlueTopLeft, colorBlueBottomLeft, blumap);
			getLight(0, 0);
			side(ZNEG_LB, tex[0]);
			getLight(1, 0);
			side(ZNEG_RB, tex[1]);
			getLight(1, 1);
			side(ZNEG_RT, tex[2]);
			getLight(0, 1);
			side(ZNEG_LT, tex[3]);
		}
	}

	@Override public void renderFaceZPos(Block block, double x, double y, double z, IIcon icon) {
		if (rendererOld != null && rendererOld.hasOverrideBlockTexture()) {
			IIcon i = rendererOld.overrideBlockTexture;

			tessellator.addVertexWithUV(0.0, 1.0, 1.0, i.getMinU(), i.getMinV());
			tessellator.addVertexWithUV(0.0, 0.0, 1.0, i.getMinU(), i.getMaxV());
			tessellator.addVertexWithUV(1.0, 0.0, 1.0, i.getMaxU(), i.getMaxV());
			tessellator.addVertexWithUV(1.0, 1.0, 1.0, i.getMaxU(), i.getMinV());
		} else {
			int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 3);

			fillLightmap(brightnessBottomLeft, brightnessBottomRight, brightnessTopRight, brightnessTopLeft);
			fillColormap(colorRedBottomLeft, colorRedBottomRight, colorRedTopRight, colorRedTopLeft, redmap);
			fillColormap(colorGreenBottomLeft, colorGreenBottomRight, colorGreenTopRight, colorGreenTopLeft, grnmap);
			fillColormap(colorBlueBottomLeft, colorBlueBottomRight, colorBlueTopRight, colorBlueTopLeft, blumap);
			getLight(0, 0);
			side(ZPOS_LB, tex[0]);
			getLight(1, 0);
			side(ZPOS_RB, tex[1]);
			getLight(1, 1);
			side(ZPOS_RT, tex[2]);
			getLight(0, 1);
			side(ZPOS_LT, tex[3]);
		}
	}

	@Override public void renderFaceYNeg(Block block, double x, double y, double z, IIcon icon) {
		if (rendererOld != null && rendererOld.hasOverrideBlockTexture()) {
			IIcon i = rendererOld.overrideBlockTexture;

			tessellator.addVertexWithUV(0.0, 0.0, 1.0, i.getMinU(), i.getMaxV());
			tessellator.addVertexWithUV(0.0, 0.0, 0.0, i.getMinU(), i.getMinV());
			tessellator.addVertexWithUV(1.0, 0.0, 0.0, i.getMaxU(), i.getMinV());
			tessellator.addVertexWithUV(1.0, 0.0, 1.0, i.getMaxU(), i.getMaxV());
		} else {
			int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 0);

			fillLightmap(brightnessBottomLeft, brightnessBottomRight, brightnessTopRight, brightnessTopLeft);
			fillColormap(colorRedBottomLeft, colorRedBottomRight, colorRedTopRight, colorRedTopLeft, redmap);
			fillColormap(colorGreenBottomLeft, colorGreenBottomRight, colorGreenTopRight, colorGreenTopLeft, grnmap);
			fillColormap(colorBlueBottomLeft, colorBlueBottomRight, colorBlueTopRight, colorBlueTopLeft, blumap);
			getLight(0, 0);
			side(YNEG_LB, tex[0]);
			getLight(1, 0);
			side(YNEG_RB, tex[1]);
			getLight(1, 1);
			side(YNEG_RT, tex[2]);
			getLight(0, 1);
			side(YNEG_LT, tex[3]);
		}
	}

	@Override public void renderFaceYPos(Block block, double x, double y, double z, IIcon icon) {
		if (rendererOld != null && rendererOld.hasOverrideBlockTexture()) {
			IIcon i = rendererOld.overrideBlockTexture;

			tessellator.addVertexWithUV(0.0, 1.0, 0.0, i.getMinU(), i.getMinV());
			tessellator.addVertexWithUV(0.0, 1.0, 1.0, i.getMinU(), i.getMaxV());
			tessellator.addVertexWithUV(1.0, 1.0, 1.0, i.getMaxU(), i.getMaxV());
			tessellator.addVertexWithUV(1.0, 1.0, 0.0, i.getMaxU(), i.getMinV());
		} else {
			int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 1);

			fillLightmap(brightnessTopRight, brightnessTopLeft, brightnessBottomLeft, brightnessBottomRight);
			fillColormap(colorRedTopRight, colorRedTopLeft, colorRedBottomLeft, colorRedBottomRight, redmap);
			fillColormap(colorGreenTopRight, colorGreenTopLeft, colorGreenBottomLeft, colorGreenBottomRight, grnmap);
			fillColormap(colorBlueTopRight, colorBlueTopLeft, colorBlueBottomLeft, colorBlueBottomRight, blumap);
			getLight(0, 0);
			side(YPOS_LB, tex[0]);
			getLight(1, 0);
			side(YPOS_RB, tex[1]);
			getLight(1, 1);
			side(YPOS_RT, tex[2]);
			getLight(0, 1);
			side(YPOS_LT, tex[3]);
		}
	}
}