package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.api.carving.IVariationInfo;
import com.cricketcraft.chisel.api.rendering.ClientUtils;
import com.cricketcraft.chisel.api.rendering.ISubmapManager;
import com.cricketcraft.chisel.api.rendering.RenderBlocksCTM;
import com.cricketcraft.chisel.utils.Drawing;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RendererCTM implements ISimpleBlockRenderingHandler {

	public RenderBlocksCTM rendererCTM = new RenderBlocksCTM();

	public RendererCTM() {
		if (ClientUtils.renderCTMId == 0) {
			ClientUtils.renderCTMId = RenderingRegistry.getNextAvailableRenderId();
		}
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		Drawing.drawBlock(block, metadata, renderer);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks rendererOld) {
		int meta = world.getBlockMetadata(x, y, z);

		IVariationInfo var = ((ICarvable) block).getVariation(world, x, y, z, meta);

		if (!rendererOld.hasOverrideBlockTexture() && var != null) {
			RenderBlocks rb = var.getSubmapManager().createRenderContext(rendererOld, block, world);
			if (rb != null && rb != rendererOld) {
				rb.blockAccess = world;
				if (rb instanceof RenderBlocksCTM) {
					((RenderBlocksCTM)rb).manager = (ISubmapManager<RenderBlocksCTM>) var.getSubmapManager();
					((RenderBlocksCTM)rb).rendererOld = rendererOld;
				}
				return rb.renderStandardBlock(block, x, y, z);
			}
		}

		return rendererOld.renderStandardBlock(block, x, y, z) || rendererOld.hasOverrideBlockTexture();
	}

	@Override
	public boolean shouldRender3DInInventory(int renderId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return ClientUtils.renderCTMId;
	}
}
