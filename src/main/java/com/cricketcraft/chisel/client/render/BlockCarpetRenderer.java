package com.cricketcraft.chisel.client.render;

import com.cricketcraft.chisel.Chisel;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockCarpetRenderer extends RendererCTM {

	public BlockCarpetRenderer() {
		super();

		Chisel.renderCarpetId = RenderingRegistry.getNextAvailableRenderId();

		rendererCTM = new RenderBlocksCTMCarpet();
	}

	@Override
	public int getRenderId() {
		return Chisel.renderCarpetId;
	}
}
