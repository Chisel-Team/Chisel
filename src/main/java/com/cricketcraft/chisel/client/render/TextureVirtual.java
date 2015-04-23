package com.cricketcraft.chisel.client.render;

import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TextureVirtual implements IIcon {

	private int width, height;
	private float umin, umax, vmin, vmax;
	private String name;
	private IIcon parentIcon;

	TextureVirtual(IIcon parent, int w, int h, int x, int y) {
		parentIcon = parent;

		umin = parentIcon.getInterpolatedU(16.0 * (x) / w);
		umax = parentIcon.getInterpolatedU(16.0 * (x + 1) / w);
		vmin = parentIcon.getInterpolatedV(16.0 * (y) / h);
		vmax = parentIcon.getInterpolatedV(16.0 * (y + 1) / h);

		name = parentIcon.getIconName() + "|" + x + "." + y;

		width = parentIcon.getIconWidth();
		height = parentIcon.getIconHeight();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getMinU() {
		return umin;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getMaxU() {
		return umax;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getInterpolatedU(double d0) {
		return (float) (umin + (umax - umin) * d0 / 16.0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getMinV() {
		return vmin;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getMaxV() {
		return vmax;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getInterpolatedV(double d0) {
		return (float) (vmin + (vmax - vmin) * d0 / 16.0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getIconName() {
		return name;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getIconWidth() {
		return width;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getIconHeight() {
		return height;
	}
}
