package team.chisel.client.render;

import team.chisel.ctmlib.CTMRenderer;

import com.cricketcraft.chisel.api.rendering.ClientUtils;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class RendererCTM extends CTMRenderer {

	public RendererCTM() {
		super(RenderingRegistry.getNextAvailableRenderId());
		ClientUtils.renderCTMId = getRenderId();
	}
}
