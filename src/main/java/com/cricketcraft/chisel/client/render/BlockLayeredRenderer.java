package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.block.BlockCarvableLayered;
import com.cricketcraft.chisel.utils.Drawing;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockLayeredRenderer implements ISimpleBlockRenderingHandler {

	public BlockLayeredRenderer() {
		Chisel.renderLayeredId = RenderingRegistry.getNextAvailableRenderId();
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		GL11.glDisable(GL11.GL_LIGHTING);
		Drawing.drawBlock(block, ((BlockCarvableLayered)block).getBaseTex(), renderer);
		GL11.glEnable(GL11.GL_LIGHTING);
		Drawing.drawBlock(block, metadata, renderer);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		Drawing.renderAllFaces(renderer, block, x, y, z, ((BlockCarvableLayered) block).getBaseTex());
		Tessellator.instance.setBrightness(0xFFFFFF);
		renderer.renderStandardBlock(block, x, y, z);
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return Chisel.renderLayeredId;
	}
}
