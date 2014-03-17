package info.jbcs.minecraft.chisel.render;

import info.jbcs.minecraft.chisel.block.BlockTexturedOre;
import info.jbcs.minecraft.utilities.Drawing;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockTexturedOreRenderer implements ISimpleBlockRenderingHandler {
	float bot=-0.001f,top=1.0f-bot;
	public static int id;

	public BlockTexturedOreRenderer() {
		id = RenderingRegistry.getNextAvailableRenderId();
	}

	@Override
	public void renderInventoryBlock(Block blck, int meta, int modelID, RenderBlocks renderer) {
		if (blck == null || !(blck instanceof BlockTexturedOre))
			return;
		
		BlockTexturedOre block=(BlockTexturedOre) blck;
		
		if(block.icon!=null){
			renderer.overrideBlockTexture=block.icon;
			renderer.renderBlockAsItem(Blocks.stone,meta,1.0f);
			renderer.overrideBlockTexture=null;
		} else if(block.base!=null){
			renderer.renderBlockAsItem(block.base,meta,1.0f);
		}
		
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		renderer.setRenderBounds(bot, bot, bot, top, top, top);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		Drawing.drawBlock(block, meta, renderer);
		GL11.glDisable(3042);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block blck, int modelId, RenderBlocks renderer) {
		if (blck == null || !(blck instanceof BlockTexturedOre))
			return false;
		BlockTexturedOre block=(BlockTexturedOre) blck;
		
		if(block.currentPass==0){
			if(block.icon!=null){
				renderer.overrideBlockTexture=block.icon;
				renderer.renderStandardBlock(block, x, y, z);
				renderer.overrideBlockTexture=null;
			} else if(block.base!=null){
				renderer.renderBlockByRenderType(block.base, x, y, z);
			}
		} else{
			renderer.setRenderBounds(bot, bot, bot, top, top, top);
			renderer.renderStandardBlock(block, x, y, z);
		}

		return true;
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
