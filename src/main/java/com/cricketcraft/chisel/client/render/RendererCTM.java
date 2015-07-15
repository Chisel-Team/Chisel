package com.cricketcraft.chisel.client.render;

import com.cricketcraft.chisel.api.rendering.ClientUtils;
import com.cricketcraft.ctmlib.CTMRenderer;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class RendererCTM extends CTMRenderer {

	public RendererCTM() {
		super(RenderingRegistry.getNextAvailableRenderId());
		ClientUtils.renderCTMId = getRenderId();
	}
}
