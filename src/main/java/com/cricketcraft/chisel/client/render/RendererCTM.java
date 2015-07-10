package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.api.carving.IVariationInfo;
import com.cricketcraft.chisel.api.rendering.ClientUtils;
import com.cricketcraft.ctmlib.Drawing;
import com.cricketcraft.ctmlib.RenderBlocksCTM;

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
		IVariationInfo var = ((ICarvable) block).getVariation(new ItemStack(block, 1, metadata));
		RenderBlocks rb = getContext(renderer, block, Minecraft.getMinecraft().theWorld, var, metadata);
		Drawing.drawBlock(block, metadata, rb);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		rb.unlockBlockBounds();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks rendererOld) {
		int meta = world.getBlockMetadata(x, y, z);
		IVariationInfo var = ((ICarvable) block).getVariation(world, x, y, z, meta);
		RenderBlocks rb = getContext(rendererOld, block, world, var, meta);
		boolean ret = rb.renderStandardBlock(block, x, y, z);
		rb.unlockBlockBounds();
		return ret;
	}
	
	private RenderBlocks getContext(RenderBlocks rendererOld, Block block, IBlockAccess world, IVariationInfo var, int meta) {
		if (!rendererOld.hasOverrideBlockTexture() && var != null) {
			RenderBlocks rb = var.getSubmapManager().createRenderContext(rendererOld, block, world);
			if (rb != null && rb != rendererOld) {
				rb.blockAccess = world;
				if (rendererOld.lockBlockBounds) {
					rb.overrideBlockBounds(rendererOld.renderMinX, rendererOld.renderMinY, rendererOld.renderMinZ, rendererOld.renderMaxX, rendererOld.renderMaxY, rendererOld.renderMaxZ);
				}
				if (rb instanceof RenderBlocksCTM) {
					RenderBlocksCTM rbctm = (RenderBlocksCTM) rb;
					rbctm.manager = rbctm.manager == null ? var.getSubmapManager() : rbctm.manager;
					rbctm.rendererOld = rbctm.rendererOld == null ? rendererOld : rbctm.rendererOld;
				}
				return rb;
			}
		}
		return rendererOld;
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
