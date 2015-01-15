package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.carving.CarvableVariation;
import com.cricketcraft.chisel.utils.Drawing;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockCTMNoLightRenderer implements ISimpleBlockRenderingHandler {

	public RenderBlocksCTMNoLight rendererCTMNoLight = new RenderBlocksCTMNoLight();

	public BlockCTMNoLightRenderer() {
		if (Chisel.renderCTMNoLightId == 0) {
			Chisel.renderCTMNoLightId = RenderingRegistry.getNextAvailableRenderId();
		}
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		GL11.glDisable(GL11.GL_LIGHTING);
		Drawing.drawBlock(block, metadata, renderer);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks rendererOld) {
		int meta = world.getBlockMetadata(x, y, z);

		CarvableVariation var = ((ICarvable) block).getVariation(world, x, y, z, meta);

		switch (var == null ? 0 : var.kind) {
		case CarvableHelper.CTMX:
			rendererCTMNoLight.blockAccess = world;
			rendererCTMNoLight.renderMaxX = 1.0;
			rendererCTMNoLight.renderMaxY = 1.0;
			rendererCTMNoLight.renderMaxZ = 1.0;

			rendererCTMNoLight.submap = var.submap;
			rendererCTMNoLight.submapSmall = var.submapSmall;

			rendererCTMNoLight.rendererOld = rendererOld;

			return rendererCTMNoLight.renderStandardBlock(block, x, y, z);
		default:
			return rendererOld.renderStandardBlock(block, x, y, z);
		}
	}

	@Override
	public boolean shouldRender3DInInventory(int renderId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return Chisel.renderCTMNoLightId;
	}

}