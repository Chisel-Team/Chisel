package com.cricketcraft.chisel.client.render.tile;

import com.cricketcraft.chisel.client.render.model.ModelDeChisel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

public class RenderDeChisel extends TileEntitySpecialRenderer{
    private ModelDeChisel deChisel = new ModelDeChisel();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        deChisel.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
    }
}
