package com.cricketcraft.chisel.client.render;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.block.BlockCarvableGlow;
import com.cricketcraft.chisel.config.Configurations;
import com.cricketcraft.chisel.utils.Drawing;
import com.cricketcraft.chisel.utils.GeneralClient;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderLayeredGlowNoColor implements ISimpleBlockRenderingHandler{

    public RenderLayeredGlowNoColor() {
        Chisel.renderNoColorGlowId = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glColor3f(1, 1, 1);
        GL11.glDisable(GL11.GL_LIGHTING);
        Drawing.drawBlock(block, ((BlockCarvableGlow) block).getGlowTexture(), renderer);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glColor3f(1, 1, 1);
        Drawing.drawBlock(block, metadata, renderer);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        Tessellator.instance.setBrightness(0xF000F0);
        Drawing.renderAllFaces(renderer, block, x, y, z, ((BlockCarvableGlow) block).getGlowTexture());
        renderer.renderStandardBlock(block, x, y, z);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return Chisel.renderNoColorGlowId;
    }
}
