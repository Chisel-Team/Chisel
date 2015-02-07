package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemDye;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.block.BlockCarvableGlow;
import com.cricketcraft.chisel.utils.Drawing;
import com.cricketcraft.chisel.utils.GeneralClient;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockGlowRenderer implements ISimpleBlockRenderingHandler {

	RenderBlocks rbctm = RenderBlocksCTM.getInstance();
	
	public BlockGlowRenderer() {
		Chisel.renderGlowId = RenderingRegistry.getNextAvailableRenderId();
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		if (((BlockCarvableGlow) block).doColors) {
			GeneralClient.setGLColorFromInt(ItemDye.field_150922_c[metadata]);
		}
		GL11.glDisable(GL11.GL_LIGHTING);
		Drawing.drawBlock(block, ((BlockCarvableGlow) block).getGlowTexture(metadata), renderer);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glColor3f(1, 1, 1);
		Drawing.drawBlock(block, metadata, renderer);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if (((BlockCarvableGlow) block).doColors) {
			Tessellator.instance.setColorOpaque_I(ItemDye.field_150922_c[world.getBlockMetadata(x, y, z)]);
		} else {
			Tessellator.instance.setColorOpaque_F(1, 1, 1);
		}
		Tessellator.instance.setBrightness(0xF000F0);
		Drawing.renderAllFaces(renderer, block, x, y, z, ((BlockCarvableGlow) block).getGlowTexture(world.getBlockMetadata(x, y, z)));
		BlockAdvancedMarbleRenderer.INSTANCE.renderWorldBlock(world, x, y, z, block, modelId, renderer);
		return true;
	}
	
	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return Chisel.renderGlowId;
	}
}
