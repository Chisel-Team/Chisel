package com.cricketcraft.chisel.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import com.cricketcraft.chisel.Chisel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarvableGlow extends BlockCarvable {

	@SideOnly(Side.CLIENT)
	private IIcon glowTexture;

	private String glowTexturePath;

	public BlockCarvableGlow(String glowTexture) {
		super();
		this.setLightLevel(0.5F); //Same as redstone torch
		this.glowTexturePath = glowTexture;
	}

	public BlockCarvableGlow(Material mat, String glowTexture) {
		super(mat);
		this.glowTexturePath = glowTexture;
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		super.registerBlockIcons(register);
		glowTexture = register.registerIcon(Chisel.MOD_ID + ":" + glowTexturePath);
	}

	public IIcon getGlowTexture() {
		return glowTexture;
	}

	@Override
	public int getRenderType() {
		return Chisel.renderGlowId;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
}
