package com.cricketcraft.minecraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.cricketcraft.minecraft.chisel.Chisel;
import com.cricketcraft.minecraft.chisel.utils.Drawing;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockEldritchRenderer implements ISimpleBlockRenderingHandler
{
    public BlockEldritchRenderer()
    {
        Chisel.RenderEldritchId = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        Drawing.drawBlock(block, metadata, renderer);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }


    RenderBlocksEldritch renderer = new RenderBlocksEldritch();

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks rendererOld)
    {
        int meta = world.getBlockMetadata(x, y, z);

//        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
/*
        double d0 = 0.4000000059604645D;
        double d1 = 0.5D - d0;
        double d2 = 0.20000000298023224D;

 */
        //       renderer.renderTorchAtAngle(Block.torchWood, x - d1, y + d2, z, -d0, 0.0D, 0);
/*
        Icon icon = block.getIcon(2, meta);
        double u0 = icon.getMinU();
        double v0 = icon.getMaxV();
        double u1 = icon.getMaxU();
        double v1 = icon.getMinV();
        double uu = icon.getInterpolatedU(8.0D);
        double vv = icon.getInterpolatedV(8.0D);


        int lyp=block.getMixedBrightnessForBlock(world, x, y+1, z);
        int lym=block.getMixedBrightnessForBlock(world, x, y-1, z);
        int lxp=block.getMixedBrightnessForBlock(world, x+1, y, z);
        int lxm=block.getMixedBrightnessForBlock(world, x-1, y, z);
        int lzp=block.getMixedBrightnessForBlock(world, x, y, z+1);
        int lzm=block.getMixedBrightnessForBlock(world, x, y, z-1);












        icon = block.getIcon(0, meta);
        u0 = icon.getMinU();
        v0 = icon.getMinV();
        u1 = icon.getMaxU();
        v1 = icon.getMaxV();
        vertex(1,u0,v0); vertex(5,u1,v0); vertex(6,u1,v1); vertex(2,u0,v1);

        icon = block.getIcon(1, meta);
        u0 = icon.getMinU();
        v0 = icon.getMinV();
        u1 = icon.getMaxU();
        v1 = icon.getMaxV();
        vertex(0,u0,v0); vertex(3,u0,v1); vertex(7,u1,v1); vertex(4,u1,v0);

        */

        renderer.blockAccess = world;
        renderer.renderMaxX = 1.0;
        renderer.renderMaxY = 1.0;
        renderer.renderMaxZ = 1.0;
        renderer.renderStandardBlock(block, x, y, z);

        return true;
/*

		boolean flag = renderer.renderStandardBlock(block, x, y, z);

        return flag;*/
    }

    @Override
    public boolean shouldRender3DInInventory(int renderId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return Chisel.RenderEldritchId;
    }

}
