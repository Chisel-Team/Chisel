package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

import org.apache.commons.lang3.tuple.Triple;

import static com.cricketcraft.chisel.client.render.RenderBlocksCTM.Vert.*;
import static com.cricketcraft.chisel.client.render.RenderBlocksCTM.SubSide.*;

public class RenderBlocksCTM extends RenderBlocks {

	/**
	 * An enum for all possible 26 sub-side vertices.
	 * <p>
	 * The naming scheme is as follows:
	 * <ul>
	 * <li>ZERO and ONE are special cases, they are the absolute min and absolute max of the block.</li>
	 * <li>X, Y, Z, or any combination means that the axes listed in the name are at 1.</li>
	 * <li>X, Y, Z, or any combination followed by HALF means that those axes are at 0.5.</li>
	 * <li>X, Y, Z, or any combination after a HALF means those axes are at 1.</li>
	 * </ul>
	 */
	enum Vert {

		ZERO(0, 0, 0),
		ONE(1, 1, 1),
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
				// inst.tessellator.setColorOpaque_F(inst.redCache[cacheID], inst.greenCache[cacheID], inst.blueCache[cacheID]);
				inst.tessellator.setBrightness(inst.lightingCache[cacheID]);
			}

			inst.tessellator.addVertexWithUV(x, y, z, inst.uCache[cacheID], inst.vCache[cacheID]);
		}
	}

	/**
	 * Each side is divided into 4 sub-sides. LB(left bottom), RB(right bottom), LT(right top), and RT(right top).
	 * <p>
	 * Each sub-side contains 4 {@link Vert} objects representing its position on the block.
	 */
	enum SubSide {
		XNEG_LB(ZERO, Z_HALF, YZ_HALF, Y_HALF), XNEG_RB(Z_HALF, Z, Y_HALF_Z, YZ_HALF), XNEG_LT(Y_HALF, YZ_HALF, Z_HALF_Y, Y), XNEG_RT(YZ_HALF, Y_HALF_Z, YZ, Z_HALF_Y);

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
	float[] greenCache = new float[4];
	float[] blueCache = new float[4];
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

	void updateLighting() {
		lightingCache[0] = brightnessBottomLeft;
		lightingCache[1] = brightnessBottomRight;
		lightingCache[2] = brightnessTopRight;
		lightingCache[3] = brightnessTopLeft;

		// // Updated to safely average light and color channels
		// L[e] = averageBrightnessChannels(brightnessBottomLeft, brightnessTopLeft, brightnessTopRight, brightnessBottomRight);
		// L[xa] = averageBrightnessChannels(L[a], L[b]);
		// L[xb] = averageBrightnessChannels(L[b], L[c]);
		// L[xc] = averageBrightnessChannels(L[c], L[d]);
		// L[xd] = averageBrightnessChannels(L[d], L[a]);
		//
		// Rcache[a] = colorRedBottomLeft;
		// Rcache[b] = colorRedBottomRight;
		// Rcache[c] = colorRedTopRight;
		// Rcache[d] = colorRedTopLeft;
		// Rcache[e] = (colorRedBottomLeft + colorRedTopLeft + colorRedTopRight + colorRedBottomRight) / 4;
		// Rcache[xa] = (Rcache[a] + Rcache[b]) / 2;
		// Rcache[xb] = (Rcache[b] + Rcache[c]) / 2;
		// Rcache[xc] = (Rcache[c] + Rcache[d]) / 2;
		// Rcache[xd] = (Rcache[d] + Rcache[a]) / 2;
		//
		// Gcache[a] = colorGreenBottomLeft;
		// Gcache[b] = colorGreenBottomRight;
		// Gcache[c] = colorGreenTopRight;
		// Gcache[d] = colorGreenTopLeft;
		// Gcache[e] = (colorGreenBottomLeft + colorGreenTopLeft + colorGreenTopRight + colorGreenBottomRight) / 4;
		// Gcache[xa] = (Gcache[a] + Gcache[b]) / 2;
		// Gcache[xb] = (Gcache[b] + Gcache[c]) / 2;
		// Gcache[xc] = (Gcache[c] + Gcache[d]) / 2;
		// Gcache[xd] = (Gcache[d] + Gcache[a]) / 2;
		//
		// Bcache[a] = colorBlueBottomLeft;
		// Bcache[b] = colorBlueBottomRight;
		// Bcache[c] = colorBlueTopRight;
		// Bcache[d] = colorBlueTopLeft;
		// Bcache[e] = (colorBlueBottomLeft + colorBlueTopLeft + colorBlueTopRight + colorBlueBottomRight) / 4;
		// Bcache[xa] = (Bcache[a] + Bcache[b]) / 2;
		// Bcache[xb] = (Bcache[b] + Bcache[c]) / 2;
		// Bcache[xc] = (Bcache[c] + Bcache[d]) / 2;
		// Bcache[xd] = (Bcache[d] + Bcache[a]) / 2;
	}

	/**
	 * Takes in a variable number of packed light-integers, and computes the average value of each 4 bit channel
	 * 
	 * For vanilla, these look like: (S = skylight and L = block light) 0000 0000 SSSS 0000 0000 0000 LLLL 0000 With the Colored Light Core installed, these look like: (B = Blue, G = Green, R = Red)
	 * 0000 0000 SSSS BBBB GGGG RRRR LLLL 0000
	 * 
	 * This method will average all of the values appropriately for the more complex case, by separating each color channel into a running total, then it will combine the final results together using
	 * integer division and a lot of bit-cramming.
	 * 
	 * While slightly more complex, the runtime should not be drastically effected, and the averaging routine remains compatible with vanilla.
	 * 
	 * Authored: CptSpaceToaster - 12/19/2014
	 */
	int averageBrightnessChannels(int... lightVals) {
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

		return ((blockLightBitChannel / lightVals.length) & 0xFF) | ((redBitChannel / lightVals.length) & 0xF00) | ((greenBitChannel / lightVals.length) & 0xF000)
				| ((blueBitChannel / lightVals.length) & 0xF0000) | ((sunlightBitChannel / lightVals.length) & 0xF00000);
	}

	void side(SubSide side, int iconIndex, boolean flip) {
		IIcon icon = iconIndex >= 16 ? submapSmall.icons[iconIndex - 16] : submap.icons[iconIndex];

		double umax = icon.getMaxU();
		double umin = icon.getMinU();
		double vmax = icon.getMaxV();
		double vmin = icon.getMinV();

		uCache[0] = flip ? umax : umin;
		uCache[1] = umax;
		uCache[2] = flip ? umin : umax;
		uCache[3] = umin;

		vCache[0] = flip ? vmin : vmax;
		vCache[1] = vmax;
		vCache[2] = flip ? vmax : vmin;
		vCache[3] = vmin;

		side.render(this);
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

			updateLighting();
			side(XNEG_LB, tex[2], false);
			side(XNEG_RB, tex[3], false);
			side(XNEG_RT, tex[1], false);
			side(XNEG_LT, tex[0], false);
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

			updateLighting();
			// side(11, 21, 3, 15, tex[3], false);
			// side(16, 7, 21, 11, tex[2], false);
			// side(25, 11, 15, 2, tex[1], false);
			// side(6, 16, 11, 25, tex[0], false);
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

			updateLighting();
			// side(2, 15, 8, 22, tex[0], false);
			// side(15, 3, 18, 8, tex[2], false);
			// side(8, 18, 0, 14, tex[3], false);
			// side(22, 8, 14, 1, tex[1], false);
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

			updateLighting();
			// side(17, 4, 20, 10, tex[2], false);
			// side(5, 17, 10, 24, tex[0], false);
			// side(24, 10, 16, 6, tex[1], false);
			// side(10, 20, 7, 16, tex[3], false);
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

			updateLighting();
			// side(13, 21, 7, 20, tex[3], true);
			// side(19, 13, 20, 4, tex[2], true);
			// side(0, 18, 13, 19, tex[0], true);
			// side(18, 3, 21, 13, tex[1], true);
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

			updateLighting();
			// side(12, 24, 6, 25, tex[3], false);
			// side(22, 12, 25, 2, tex[1], false);
			// side(1, 23, 12, 22, tex[0], false);
			// side(23, 5, 24, 12, tex[2], false);
		}
	}

}
