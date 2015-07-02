package com.cricketcraft.chisel.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.ForgeHooksClient;

import com.cricketcraft.chisel.client.render.RendererMultiLayer;

public abstract class BlockMultiLayerBase extends Block {

	public Block base;
	public IIcon icon;
	String iconFile;

	public BlockMultiLayerBase(Material mat, Block base) {
		super(mat);

		this.base = base;
	}

	public BlockMultiLayerBase(Material mat, String iconFile) {
		super(mat);

		this.iconFile = iconFile;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return RendererMultiLayer.id;
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public boolean canRenderInPass(int pass) {
		ForgeHooksClient.setRenderPass(pass);
		return pass == 1 || pass == 0;
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		if (iconFile != null)
			icon = register.registerIcon(iconFile);
	}

}
