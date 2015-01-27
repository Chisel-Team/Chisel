package com.cricketcraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.cricketcraft.chisel.block.BlockCarvablePillar;
import com.cricketcraft.chisel.carving.CarvableHelper;
import com.cricketcraft.chisel.utils.Drawing;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockMarblePillarRenderer implements ISimpleBlockRenderingHandler {

	public static int id;

	public BlockMarblePillarRenderer() {
		id = RenderingRegistry.getNextAvailableRenderId();
	}

	@Override
	public void renderInventoryBlock(Block blck, int metadata, int modelID, RenderBlocks renderer) {
		if (!(blck instanceof BlockCarvablePillar))
			return;

		BlockCarvablePillar block = (BlockCarvablePillar) blck;

		block.sides[0] = block.sides[1] = block.getCtmIcon(4, metadata);
		block.sides[2] = block.sides[3] = block.sides[4] = block.sides[5] = block.getCtmIcon(0, metadata);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		Drawing.drawBlock(block, metadata, renderer);
	}

	boolean connected(IBlockAccess world, int x, int y, int z, Block block, int meta) {
		return world.getBlock(x, y, z).equals(block) && world.getBlockMetadata(x, y, z) == meta;

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block blck, int modelId, RenderBlocks renderer) {
		if (!(blck instanceof BlockCarvablePillar))
			return false;

		BlockCarvablePillar block = (BlockCarvablePillar) blck;

		int metadata = world.getBlockMetadata(x, y, z);

		//Probably not the best way to handle this...
		if(metadata > block.carverHelper.variations.size())
		{
			metadata = block.carverHelper.variations.size() - 1;
		}
		else {
			if (block.carverHelper.variations.get(metadata).kind != CarvableHelper.CTMV) {
				block.sides[0] = block.carverHelper.getIcon(0, metadata);
				block.sides[1] = block.carverHelper.getIcon(1, metadata);
				block.sides[2] = block.carverHelper.getIcon(2, metadata);
				block.sides[3] = block.carverHelper.getIcon(3, metadata);
				block.sides[4] = block.carverHelper.getIcon(4, metadata);
				block.sides[5] = block.carverHelper.getIcon(5, metadata);

				return renderer.renderStandardBlock(block, x, y, z);
			}
		}

		boolean yp = connected(world, x, y + 1, z, block, metadata);
		boolean yn = connected(world, x, y - 1, z, block, metadata);

		if (yp || yn) {
			block.sides[0] = block.getCtmIcon(4, metadata);
			block.sides[1] = block.getCtmIcon(4, metadata);

			if (yp && yn)
				block.sides[2] = block.getCtmIcon(2, metadata);
			else if (yp)
				block.sides[2] = block.getCtmIcon(3, metadata);
			else
				block.sides[2] = block.getCtmIcon(1, metadata);

			block.sides[3] = block.sides[4] = block.sides[5] = block.sides[2];
		} else {
			boolean xp = connected(world, x + 1, y, z, block, metadata);
			boolean xn = connected(world, x - 1, y, z, block, metadata);

			if (xp && (connected(world, x + 1, y + 1, z, block, metadata) || connected(world, x + 1, y - 1, z, block, metadata)))
				xp = false;
			if (xn && (connected(world, x - 1, y + 1, z, block, metadata) || connected(world, x - 1, y - 1, z, block, metadata)))
				xn = false;

			if (xp || xn) {
				renderer.uvRotateEast = 2;
				renderer.uvRotateWest = 1;
				renderer.uvRotateTop = 1;
				renderer.uvRotateBottom = 1;

				block.sides[4] = block.getCtmIcon(4, metadata);
				block.sides[5] = block.getCtmIcon(4, metadata);

				if (xp && xn)
					block.sides[0] = block.getCtmIcon(2, metadata);
				else if (xp)
					block.sides[0] = block.getCtmIcon(3, metadata);
				else
					block.sides[0] = block.getCtmIcon(1, metadata);

				block.sides[1] = block.sides[2] = block.sides[3] = block.sides[0];
			} else {
				boolean zp = connected(world, x, y, z + 1, block, metadata);
				boolean zn = connected(world, x, y, z - 1, block, metadata);

				if (zp && (connected(world, x, y + 1, z + 1, block, metadata) || connected(world, x, y - 1, z + 1, block, metadata)))
					zp = false;
				if (zp && (connected(world, x + 1, y, z + 1, block, metadata) || connected(world, x - 1, y, z + 1, block, metadata)))
					zp = false;
				if (zn && (connected(world, x, y + 1, z - 1, block, metadata) || connected(world, x, y - 1, z - 1, block, metadata)))
					zn = false;
				if (zn && (connected(world, x + 1, y, z - 1, block, metadata) || connected(world, x - 1, y, z - 1, block, metadata)))
					zn = false;

				if (zp || zn) {
					renderer.uvRotateSouth = 1;
					renderer.uvRotateNorth = 2;

					block.sides[2] = block.getCtmIcon(4, metadata);
					block.sides[3] = block.getCtmIcon(4, metadata);

					if (zp && zn)
						block.sides[0] = block.getCtmIcon(2, metadata);
					else if (zp)
						block.sides[0] = block.getCtmIcon(1, metadata);
					else
						block.sides[0] = block.getCtmIcon(3, metadata);

					block.sides[1] = block.sides[4] = block.sides[5] = block.sides[0];
				} else {
					block.sides[0] = block.sides[1] = block.getCtmIcon(4, metadata);
					block.sides[2] = block.sides[3] = block.sides[4] = block.sides[5] = block.getCtmIcon(0, metadata);
				}
			}
		}

		boolean flag = renderer.renderStandardBlock(block, x, y, z);

		renderer.uvRotateSouth = 0;
		renderer.uvRotateEast = 0;
		renderer.uvRotateWest = 0;
		renderer.uvRotateNorth = 0;
		renderer.uvRotateTop = 0;
		renderer.uvRotateBottom = 0;

		return flag;
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
