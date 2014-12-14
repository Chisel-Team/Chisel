package info.jbcs.minecraft.chisel.client.render;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.api.ICarvable;
import info.jbcs.minecraft.chisel.carving.CarvableHelper;
import info.jbcs.minecraft.chisel.carving.CarvableVariation;
import info.jbcs.minecraft.chisel.utils.Drawing;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockAdvancedMarbleRenderer implements ISimpleBlockRenderingHandler
{
    public RenderBlocksCTM rendererCTM = new RenderBlocksCTM();
    RenderBlocksColumn rendererColumn = new RenderBlocksColumn();

    public BlockAdvancedMarbleRenderer()
    {
        if(Chisel.RenderCTMId == 0)
        {
            Chisel.RenderCTMId = RenderingRegistry.getNextAvailableRenderId();

        }
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        Drawing.drawBlock(block, metadata, renderer);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks rendererOld)
    {
        int meta = world.getBlockMetadata(x, y, z);

        CarvableVariation var = ((ICarvable) block).getVariation(meta);

        switch(var == null ? 0 : var.kind)
        {
            case CarvableHelper.CTMX:
                rendererCTM.blockAccess = world;
                rendererCTM.renderMaxX = 1.0;
                rendererCTM.renderMaxY = 1.0;
                rendererCTM.renderMaxZ = 1.0;

                rendererCTM.submap = var.submap;
                rendererCTM.submapSmall = var.submapSmall;

                rendererCTM.rendererOld = rendererOld;

                return rendererCTM.renderStandardBlock(block, x, y, z);
            case CarvableHelper.CTMV:
                rendererColumn.blockAccess = world;
                rendererColumn.renderMaxX = 1.0;
                rendererColumn.renderMaxY = 1.0;
                rendererColumn.renderMaxZ = 1.0;

                rendererColumn.submap = var.seamsCtmVert;
                rendererColumn.iconTop = var.iconTop;

                return rendererColumn.renderStandardBlock(block, x, y, z);
            default:
                return rendererOld.renderStandardBlock(block, x, y, z);
        }
    }

    @Override
    public boolean shouldRender3DInInventory(int renderId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return Chisel.RenderCTMId;
    }

}
