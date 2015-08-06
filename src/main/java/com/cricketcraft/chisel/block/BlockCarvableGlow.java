package com.cricketcraft.chisel.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.client.render.SubmapManagerBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarvableGlow extends BlockCarvableColor {

	@SideOnly(Side.CLIENT)
	private IIcon glowTexture;

	private SubmapManagerBase submapManagerBase;
	private String glowTexturePath;

	public BlockCarvableGlow(String glowTexture) {
		super();
		setLightLevel(0.5f);
		this.glowTexturePath = glowTexture;
	}

	public BlockCarvableGlow(Material mat, String glowTexture) {
		super(mat);
		this.glowTexturePath = glowTexture;
	}

	public BlockCarvableGlow(Material material, String glowTexture, SubmapManagerBase submapManagerBase) {
		super(material);
		this.glowTexturePath = glowTexture;
		this.submapManagerBase = submapManagerBase;
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		super.registerBlockIcons(register);
		glowTexture = register.registerIcon(Chisel.MOD_ID + ":" + glowTexturePath);
	}

	public boolean hasSubmapManager() {
		return submapManagerBase == null;
	}

	public SubmapManagerBase getSubmapManager() {
		return hasSubmapManager() ? submapManagerBase : null;
	}

	public IIcon getGlowTexture() {
		return glowTexture;
	}

	@Override
	public int getRenderType() {
		return Chisel.renderGlowId;
	}
}
