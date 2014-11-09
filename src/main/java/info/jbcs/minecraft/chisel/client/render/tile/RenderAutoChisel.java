package info.jbcs.minecraft.chisel.client.render.tile;

import info.jbcs.minecraft.chisel.client.model.ModelAutoChisel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderAutoChisel extends TileEntitySpecialRenderer {

    private final ModelAutoChisel model;
    private final ResourceLocation texture = new ResourceLocation("chisel:textures/blocks/autoChisel.png");

    public RenderAutoChisel(){
        model = new ModelAutoChisel();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float scale) {
        bindTexture(texture);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        GL11.glScalef(0.89F, 1.0F, 0.89F);
        this.model.renderAll();
        GL11.glPopMatrix();
    }
}
