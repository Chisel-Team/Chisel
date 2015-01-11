package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.cricketcraft.chisel.utils.Drawing;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderStandardBlockNoSunlight implements ISimpleBlockRenderingHandler {

	static int id;

	public RenderStandardBlockNoSunlight() {
		id = RenderingRegistry.getNextAvailableRenderId();
	}

	@Override
	public void renderInventoryBlock(Block block, int meta, int modelID, RenderBlocks renderer) {
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		Drawing.drawBlock(block, meta, renderer);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		int meta = world.getBlockMetadata(x, y, z);

		if (meta != 0)
			renderer.overrideBlockTexture = block.getIcon(0, meta);
		renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, 1.0F, 1.0F, 1.0F);
		renderer.overrideBlockTexture = null;

		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int renderId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return id;
	}

}
