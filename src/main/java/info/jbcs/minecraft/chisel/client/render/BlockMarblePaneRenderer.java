package info.jbcs.minecraft.chisel.client.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import info.jbcs.minecraft.chisel.utils.Drawing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class BlockMarblePaneRenderer implements ISimpleBlockRenderingHandler
{
    public static int id;

    public BlockMarblePaneRenderer()
    {
        id = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void renderInventoryBlock(Block block, int meta, int modelID, RenderBlocks renderer)
    {
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glScalef(1.25f, 1.25f, 1.25f);
        renderer.setRenderBounds(0.0f, 0.0f, 0.5f - 0.0625f, 1.0f, 1.0f, 0.5f + 0.0625f);
        Drawing.drawBlock(block, meta, renderer);
    }

    class PaneRenderer
    {
        Tessellator tessellator;

        double i0u0;
        double i0uh;
        double i0u1;
        double i0v0;
        double i0v1;

        double i1u0;
        double i1u1;
        double i1v0;
        double i1vh;
        double i1v1;
        double i1v;

        double i2u0;
        double i2u1;
        double i2v0;
        double i2v1;
        double i2v;

        double x0;
        double xh;
        double x1;
        double xp0;
        double xp1;

        double z0;
        double zh;
        double z1;
        double zp0;
        double zp1;

        double y0;
        double y1;

        void set(double x, double y, double z, IIcon icon, IIcon icon1, IIcon icon2)
        {
            tessellator = Tessellator.instance;

            i0u0 = icon.getMinU();
            i0uh = icon.getInterpolatedU(8.0D);
            i0u1 = icon.getMaxU();
            i0v0 = icon.getMinV();
            i0v1 = icon.getMaxV();

            i1u0 = icon1.getInterpolatedU(7.0D);
            i1u1 = icon1.getInterpolatedU(9.0D);
            i1v0 = icon1.getMinV();
            i1vh = icon1.getInterpolatedV(8.0D);
            i1v1 = icon1.getMaxV();
            i1v = i1v1 - i1v0;

            i2u0 = icon2.getInterpolatedU(7.0D);
            i2u1 = icon2.getInterpolatedU(9.0D);
            i2v0 = icon2.getMinV();
            i2v1 = icon2.getMaxV();
            i2v = i2v1 - i2v0;

            x0 = x;
            xh = x0 + 0.5D;
            x1 = x + 1;
            xp0 = x0 + 0.5D - 0.0625D;
            xp1 = x0 + 0.5D + 0.0625D;

            z0 = z;
            zh = z0 + 0.5D;
            z1 = z + 1;
            zp0 = z0 + 0.5D - 0.0625D;
            zp1 = z0 + 0.5D + 0.0625D;

            y0 = y;
            y1 = y + 1;
        }

        public void renderNorthPane()
        {
            tessellator.addVertexWithUV(xh, y1, z0, i0uh, i0v0);
            tessellator.addVertexWithUV(xh, y0, z0, i0uh, i0v1);
            tessellator.addVertexWithUV(xh, y0, zh, i0u0, i0v1);
            tessellator.addVertexWithUV(xh, y1, zh, i0u0, i0v0);
            tessellator.addVertexWithUV(xh, y1, zh, i0u0, i0v0);
            tessellator.addVertexWithUV(xh, y0, zh, i0u0, i0v1);
            tessellator.addVertexWithUV(xh, y0, z0, i0uh, i0v1);
            tessellator.addVertexWithUV(xh, y1, z0, i0uh, i0v0);
        }

        public void renderSouthPane()
        {
            tessellator.addVertexWithUV(xh, y1, zh, i0u1, i0v0);
            tessellator.addVertexWithUV(xh, y0, zh, i0u1, i0v1);
            tessellator.addVertexWithUV(xh, y0, z1, i0uh, i0v1);
            tessellator.addVertexWithUV(xh, y1, z1, i0uh, i0v0);
            tessellator.addVertexWithUV(xh, y1, z1, i0uh, i0v0);
            tessellator.addVertexWithUV(xh, y0, z1, i0uh, i0v1);
            tessellator.addVertexWithUV(xh, y0, zh, i0u1, i0v1);
            tessellator.addVertexWithUV(xh, y1, zh, i0u1, i0v0);
        }

        public void renderWestPane()
        {
            tessellator.addVertexWithUV(x0, y1, zh, i0uh, i0v0);
            tessellator.addVertexWithUV(x0, y0, zh, i0uh, i0v1);
            tessellator.addVertexWithUV(xh, y0, zh, i0u0, i0v1);
            tessellator.addVertexWithUV(xh, y1, zh, i0u0, i0v0);
            tessellator.addVertexWithUV(xh, y1, zh, i0u0, i0v0);
            tessellator.addVertexWithUV(xh, y0, zh, i0u0, i0v1);
            tessellator.addVertexWithUV(x0, y0, zh, i0uh, i0v1);
            tessellator.addVertexWithUV(x0, y1, zh, i0uh, i0v0);
        }

        public void renderEastPane()
        {
            tessellator.addVertexWithUV(xh, y1, zh, i0u1, i0v0);
            tessellator.addVertexWithUV(xh, y0, zh, i0u1, i0v1);
            tessellator.addVertexWithUV(x1, y0, zh, i0uh, i0v1);
            tessellator.addVertexWithUV(x1, y1, zh, i0uh, i0v0);
            tessellator.addVertexWithUV(x1, y1, zh, i0uh, i0v0);
            tessellator.addVertexWithUV(x1, y0, zh, i0uh, i0v1);
            tessellator.addVertexWithUV(xh, y0, zh, i0u1, i0v1);
            tessellator.addVertexWithUV(xh, y1, zh, i0u1, i0v0);
        }

        public void renderVerticalNS(double y, double zoff0, double zoff1, double v0, double v1)
        {
            tessellator.addVertexWithUV(xp0, y0 + y, z0 + zoff0, i1u0, i1v0 + i1v * v0);
            tessellator.addVertexWithUV(xp0, y0 + y, z0 + zoff1, i1u0, i1v0 + i1v * v1);
            tessellator.addVertexWithUV(xp1, y0 + y, z0 + zoff1, i1u1, i1v0 + i1v * v1);
            tessellator.addVertexWithUV(xp1, y0 + y, z0 + zoff0, i1u1, i1v0 + i1v * v0);
            tessellator.addVertexWithUV(xp0, y0 + y, z0 + zoff1, i1u0, i1v0 + i1v * v1);
            tessellator.addVertexWithUV(xp0, y0 + y, z0 + zoff0, i1u0, i1v0 + i1v * v0);
            tessellator.addVertexWithUV(xp1, y0 + y, z0 + zoff0, i1u1, i1v0 + i1v * v0);
            tessellator.addVertexWithUV(xp1, y0 + y, z0 + zoff1, i1u1, i1v0 + i1v * v1);
        }

        public void renderVerticalWE(double y, double xoff0, double xoff1, double v0, double v1)
        {
            tessellator.addVertexWithUV(x0 + xoff0, y0 + y, zp1, i1u1, i1v0 + i1v * v0);
            tessellator.addVertexWithUV(x0 + xoff1, y0 + y, zp1, i1u1, i1v0 + i1v * v1);
            tessellator.addVertexWithUV(x0 + xoff1, y0 + y, zp0, i1u0, i1v0 + i1v * v1);
            tessellator.addVertexWithUV(x0 + xoff0, y0 + y, zp0, i1u0, i1v0 + i1v * v0);
            tessellator.addVertexWithUV(x0 + xoff1, y0 + y, zp1, i1u1, i1v0 + i1v * v1);
            tessellator.addVertexWithUV(x0 + xoff0, y0 + y, zp1, i1u1, i1v0 + i1v * v0);
            tessellator.addVertexWithUV(x0 + xoff0, y0 + y, zp0, i1u0, i1v0 + i1v * v0);
            tessellator.addVertexWithUV(x0 + xoff1, y0 + y, zp0, i1u0, i1v0 + i1v * v1);
        }


        public void renderHorizontalNS(double zoff, double v0, double v1)
        {
            tessellator.addVertexWithUV(xp0, y1, z0 + zoff, i2u0, i2v0 + i2v * v1);
            tessellator.addVertexWithUV(xp0, y0, z0 + zoff, i2u0, i2v0 + i2v * v0);
            tessellator.addVertexWithUV(xp1, y0, z0 + zoff, i2u1, i2v0 + i2v * v0);
            tessellator.addVertexWithUV(xp1, y1, z0 + zoff, i2u1, i2v0 + i2v * v1);
            tessellator.addVertexWithUV(xp1, y1, z0 + zoff, i2u1, i2v0 + i2v * v1);
            tessellator.addVertexWithUV(xp1, y0, z0 + zoff, i2u1, i2v0 + i2v * v0);
            tessellator.addVertexWithUV(xp0, y0, z0 + zoff, i2u0, i2v0 + i2v * v0);
            tessellator.addVertexWithUV(xp0, y1, z0 + zoff, i2u0, i2v0 + i2v * v1);

        }

        public void renderHorizontalWE(double xoff, double v0, double v1)
        {
            tessellator.addVertexWithUV(x0 + xoff, y1, zp0, i2u0, i2v0 + i2v * v1);
            tessellator.addVertexWithUV(x0 + xoff, y0, zp0, i2u0, i2v0 + i2v * v0);
            tessellator.addVertexWithUV(x0 + xoff, y0, zp1, i2u1, i2v0 + i2v * v0);
            tessellator.addVertexWithUV(x0 + xoff, y1, zp1, i2u1, i2v0 + i2v * v1);
            tessellator.addVertexWithUV(x0 + xoff, y1, zp1, i2u1, i2v0 + i2v * v1);
            tessellator.addVertexWithUV(x0 + xoff, y0, zp1, i2u1, i2v0 + i2v * v0);
            tessellator.addVertexWithUV(x0 + xoff, y0, zp0, i2u0, i2v0 + i2v * v0);
            tessellator.addVertexWithUV(x0 + xoff, y1, zp0, i2u0, i2v0 + i2v * v1);
        }
    }

    PaneRenderer paneRenderer = new PaneRenderer();

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block b, int modelId, RenderBlocks renderer)
    {
        BlockPane block = (BlockPane) b;
        Tessellator tessellator = Tessellator.instance;

        int worldHeight = world.getHeight();
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

        int meta = world.getBlockMetadata(x, y, z);
        IIcon iconPane = block.getIcon(2, meta);
        IIcon iconTop = block.getIcon(1, meta);
        IIcon iconSide = block.getIcon(0, meta);
        if(iconPane == null || iconTop == null || iconSide == null) return false;

        paneRenderer.set(x, y, z, iconPane, iconTop, iconSide);

        boolean connectsNorth = block.canPaneConnectToBlock(world.getBlock(x, y, z - 1));
        boolean connectsSouth = block.canPaneConnectToBlock(world.getBlock(x, y, z + 1));
        boolean connectsWest = block.canPaneConnectToBlock(world.getBlock(x - 1, y, z));
        boolean connectsEast = block.canPaneConnectToBlock(world.getBlock(x + 1, y, z));
        boolean connectsTop = block.canPaneConnectToBlock(world.getBlock(x, y + 1, z));
        boolean connectsBottom = block.canPaneConnectToBlock(world.getBlock(x, y - 1, z));

        boolean connectsTopNorth = block.canPaneConnectToBlock(world.getBlock(x, y + 1, z - 1));
        boolean connectsTopSouth = block.canPaneConnectToBlock(world.getBlock(x, y + 1, z + 1));
        boolean connectsTopWest = block.canPaneConnectToBlock(world.getBlock(x - 1, y + 1, z));
        boolean connectsTopEast = block.canPaneConnectToBlock(world.getBlock(x + 1, y + 1, z));

        boolean connectsBottomNorth = block.canPaneConnectToBlock(world.getBlock(x, y - 1, z - 1));
        boolean connectsBottomSouth = block.canPaneConnectToBlock(world.getBlock(x, y - 1, z + 1));
        boolean connectsBottomWest = block.canPaneConnectToBlock(world.getBlock(x - 1, y - 1, z));
        boolean connectsBottomEast = block.canPaneConnectToBlock(world.getBlock(x + 1, y - 1, z));

        boolean isolatedTop = !connectsTopNorth && !connectsTopSouth && !connectsTopWest && !connectsTopEast;
        boolean isolatedBottom = !connectsBottomNorth && !connectsBottomSouth && !connectsBottomWest && !connectsBottomEast;

        if(!connectsNorth && !connectsSouth && !connectsWest && !connectsEast)
        {
            connectsNorth = connectsSouth = connectsWest = connectsEast = true;
        }

        int connections = 0;
        if(connectsNorth)
        {
            paneRenderer.renderNorthPane();
            if(!connectsBottom || !connectsBottomNorth && !isolatedBottom)
                paneRenderer.renderVerticalNS(-0.0003, 0.0, 0.5, 0.5, 0.0);
            if(!connectsTop || !connectsTopNorth && !isolatedTop)
                paneRenderer.renderVerticalNS(+1.0003, 0.0, 0.5, 0.5, 0.0);
            connections++;
        }
        if(connectsSouth)
        {
            paneRenderer.renderSouthPane();
            if(!connectsBottom || !connectsBottomSouth && !isolatedBottom)
                paneRenderer.renderVerticalNS(-0.0004, 0.5, 1.0, 1.0, 0.5);
            if(!connectsTop || !connectsTopSouth && !isolatedTop)
                paneRenderer.renderVerticalNS(+1.0004, 0.5, 1.0, 1.0, 0.5);
            connections++;
        }
        if(connectsWest)
        {
            paneRenderer.renderWestPane();
            if(!connectsBottom || !connectsBottomWest && !isolatedBottom)
                paneRenderer.renderVerticalWE(-0.0001, 0.0, 0.5, 0.5, 0.0);
            if(!connectsTop || !connectsTopWest && !isolatedTop)
                paneRenderer.renderVerticalWE(+1.0001, 0.0, 0.5, 0.5, 0.0);
            connections++;
        }
        if(connectsEast)
        {
            paneRenderer.renderEastPane();
            if(!connectsBottom || !connectsBottomEast && !isolatedBottom)
                paneRenderer.renderVerticalWE(-0.0002, 0.5, 1.0, 1.0, 0.5);
            if(!connectsTop || !connectsTopEast && !isolatedTop)
                paneRenderer.renderVerticalWE(+1.0002, 0.5, 1.0, 1.0, 0.5);
            connections++;
        }


        if(connections == 1)
        {
            if(connectsNorth) paneRenderer.renderHorizontalNS(0.5, 1.0, 0.0);
            if(connectsSouth) paneRenderer.renderHorizontalNS(0.5, 0.0, 1.0);
            if(connectsEast) paneRenderer.renderHorizontalWE(0.5, 0.0, 1.0);
            if(connectsWest) paneRenderer.renderHorizontalWE(0.5, 1.0, 0.0);
        } else if(connections == 4)
        {
            paneRenderer.renderHorizontalNS(0.0, 1.0, 0.0);
            paneRenderer.renderHorizontalNS(1.0, 0.0, 1.0);
            paneRenderer.renderHorizontalWE(0.0, 0.0, 1.0);
            paneRenderer.renderHorizontalWE(1.0, 1.0, 0.0);
        }

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
