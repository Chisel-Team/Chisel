package com.cricketcraft.minecraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.cricketcraft.minecraft.chisel.block.BlockRoadLine;
import com.cricketcraft.minecraft.chisel.utils.Drawing;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockRoadLineRenderer implements ISimpleBlockRenderingHandler
{
    public static int id;

    public BlockRoadLineRenderer()
    {
        id = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void renderInventoryBlock(Block block, int meta, int modelID, RenderBlocks renderer)
    {
        renderer.setRenderBoundsFromBlock(block);
        Drawing.drawBlock(block, meta, renderer);
    }

    public void renderTopFace(double y, IIcon icon)
    {
        Tessellator tessellator = Tessellator.instance;

        tessellator.addVertexWithUV(1, y, 0, icon.getMaxU(), icon.getMinV());
        tessellator.addVertexWithUV(0, y, 0, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(0, y, 1, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(1, y, 1, icon.getMaxU(), icon.getMaxV());
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block b, int modelId, RenderBlocks renderer)
    {
        int meta = world.getBlockMetadata(x, y, z);
        BlockRoadLine block = (BlockRoadLine) b;
        Tessellator tessellator = Tessellator.instance;

        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));

        float f = 1.0F;
        int i1 = block.colorMultiplier(world, x, y, z);
        float f1 = (i1 >> 16 & 255) / 255.0F;
        float f2 = (i1 >> 8 & 255) / 255.0F;
        float f3 = (i1 & 255) / 255.0F;

        if(EntityRenderer.anaglyphEnable)
        {
            float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
            float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
            float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }
        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);

        boolean N = world.getBlock(x, y, z - 1).equals(block);
        boolean S = world.getBlock(x, y, z + 1).equals(block);
        boolean W = world.getBlock(x - 1, y, z).equals(block);
        boolean E = world.getBlock(x + 1, y, z).equals(block);

        if(!N && !S && !W && !E)
        {
            renderer.renderStandardBlock(block, x, y, z);
            return true;
        }

        if(N && S)
        {
            renderer.uvRotateTop = 0;
            renderer.overrideBlockTexture = block.fullLineIcon;
            renderer.renderStandardBlock(block, x, y, z);

        } else
        {
            if(N)
            {
                renderer.uvRotateTop = 0;
                renderer.overrideBlockTexture = block.halfLineIcon;
                renderer.renderStandardBlock(block, x, y, z);
            }
            if(S)
            {
                renderer.uvRotateTop = 3;
                renderer.overrideBlockTexture = block.halfLineIcon;
                renderer.renderStandardBlock(block, x, y, z);
            }

        }

        if(E && W)
        {
            renderer.uvRotateTop = 1;
            renderer.overrideBlockTexture = block.fullLineIcon;
            renderer.renderStandardBlock(block, x, y, z);
        } else
        {
            if(E)
            {
                renderer.uvRotateTop = 1;
                renderer.overrideBlockTexture = block.halfLineIcon;
                renderer.renderStandardBlock(block, x, y, z);
            }
            if(W)
            {
                renderer.uvRotateTop = 2;
                renderer.overrideBlockTexture = block.halfLineIcon;
                renderer.renderStandardBlock(block, x, y, z);
            }
        }

        renderer.uvRotateTop = 0;
        renderer.overrideBlockTexture = null;
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int renderId)
    {
        return false;
    }

    @Override
    public int getRenderId()
    {
        return id;
    }
}
