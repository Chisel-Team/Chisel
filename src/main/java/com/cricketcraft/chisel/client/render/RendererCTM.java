package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.api.carving.IVariationInfo;
import com.cricketcraft.chisel.utils.Drawing;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RendererCTM implements ISimpleBlockRenderingHandler {

	public RenderBlocksCTM rendererCTM = new RenderBlocksCTM();
	RenderBlocksColumn rendererColumn = new RenderBlocksColumn();

	public RendererCTM() {
		if (Chisel.renderCTMId == 0) {
			Chisel.renderCTMId = RenderingRegistry.getNextAvailableRenderId();
		}
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		Drawing.drawBlock(block, metadata, renderer);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks rendererOld) {
		int meta = world.getBlockMetadata(x, y, z);

		IVariationInfo var = ((ICarvable) block).getVariation(world, x, y, z, meta);

		if (!rendererOld.hasOverrideBlockTexture() && var != null) {
			RenderBlocks rb = var.getSubmapManager().createRenderContext(rendererOld, world);
			if (rb != null) {
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
		return Chisel.renderCTMId;
	}

}
