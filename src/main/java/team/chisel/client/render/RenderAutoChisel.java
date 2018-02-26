package team.chisel.client.render;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import team.chisel.common.block.TileAutoChisel;

public class RenderAutoChisel extends TileEntitySpecialRenderer<TileAutoChisel> {
    
    @Override
    public void renderTileEntityAt(@Nonnull TileAutoChisel te, double x, double y, double z, float partialTicks, int destroyStage) {
        if (te instanceof TileAutoChisel) {
            ItemStack source = te.getSource();

            if (source != null) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(x + 0.5, y + 10/16f, z + 0.5);
                Minecraft.getMinecraft().getRenderItem().renderItem(source, TransformType.GROUND);
                GlStateManager.popMatrix();
            }
        }
    }
}
