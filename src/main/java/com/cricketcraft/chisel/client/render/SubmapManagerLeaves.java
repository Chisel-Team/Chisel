package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class SubmapManagerLeaves extends SubmapManagerBase {

	private String name;
	private IIcon normal, opaque;

	public SubmapManagerLeaves(String name) {
		this.name = name;
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return Blocks.leaves.isOpaqueCube() ? opaque : normal;
	}

	@Override
	public void registerIcons(String modName, Block block, IIconRegister register) {
		this.normal = register.registerIcon(modName + ":" + name);
		this.opaque = register.registerIcon(modName + ":" + name + "_opaque");
	}
}
