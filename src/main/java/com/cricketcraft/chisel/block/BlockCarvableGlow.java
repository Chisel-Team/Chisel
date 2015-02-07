package com.cricketcraft.chisel.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import com.cricketcraft.chisel.Chisel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarvableGlow extends BlockCarvable {

	@SideOnly(Side.CLIENT)
	private IIcon[] baseTextures;

	private String[] texturePaths;

	public final boolean doColors;

	public BlockCarvableGlow(boolean doColors, String... underlayTextures) {
		super();
		setLightLevel(0.5f);
		this.texturePaths = underlayTextures;
		this.doColors = doColors;
	}

	public BlockCarvableGlow(Material mat, boolean doColors, String... underlayTextures) {
		super(mat);
		setLightLevel(0.5f);
		this.texturePaths = underlayTextures;
		this.doColors = doColors;
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		super.registerBlockIcons(register);
		baseTextures = new IIcon[16];
		for (int i = 0; i < texturePaths.length; i++) {
			baseTextures[i] = register.registerIcon(Chisel.MOD_ID + ":" + texturePaths[i]);
		}
	}

	public IIcon getGlowTexture(int meta) {
		return baseTextures[meta % baseTextures.length];
	}

	@Override
	public int getRenderType() {
		return Chisel.renderGlowId;
	}
}
