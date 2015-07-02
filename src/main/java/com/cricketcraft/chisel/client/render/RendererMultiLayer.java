package com.cricketcraft.chisel.client.render;

import com.cricketcraft.chisel.block.BlockMultiLayerBase;
import com.cricketcraft.ctmlib.Drawing;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;

import static org.lwjgl.opengl.GL11.*;

public class RendererMultiLayer implements ISimpleBlockRenderingHandler {

	float bot = -0.001f, top = 1.0f - bot;
	public static int id;

	public RendererMultiLayer() {
		id = RenderingRegistry.getNextAvailableRenderId();
	}

	@Override
	public void renderInventoryBlock(Block blck, int meta, int modelID, RenderBlocks renderer) {
		if (blck == null || !(blck instanceof BlockMultiLayerBase))
			return;

		BlockMultiLayerBase block = (BlockMultiLayerBase) blck;

		if (block.icon != null) {
			renderer.overrideBlockTexture = block.icon;
			renderer.renderBlockAsItem(Blocks.stone, meta, 1.0f);
			renderer.overrideBlockTexture = null;
		} else if (block.base != null) {
			renderer.renderBlockAsItem(block.base, meta, 1.0f);
		}
		
		glPushMatrix();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		renderer.setRenderBounds(bot, bot, bot, top, top, top);
		glTranslatef(-0.5F, -0.5F, -0.5F);
		Drawing.drawBlock(block, meta, renderer);
		glDisable(GL_BLEND);
		glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block blck, int modelId, RenderBlocks renderer) {
		if (blck == null || !(blck instanceof BlockMultiLayerBase))
			return false;
		BlockMultiLayerBase block = (BlockMultiLayerBase) blck;

		if (block.currentPass == 0) {
			if (block.icon != null) {
				renderer.overrideBlockTexture = block.icon;
				renderer.renderStandardBlock(block, x, y, z);
				renderer.overrideBlockTexture = null;
				return true;
			} else if (block.base != null) {
				renderer.renderBlockByRenderType(block.base, x, y, z);
				return true;
			}
		} else {
			renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
			return renderer.renderStandardBlock(block, x, y, z);
		}

		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return id;
	}
}
