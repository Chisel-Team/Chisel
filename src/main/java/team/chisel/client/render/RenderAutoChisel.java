package team.chisel.client.render;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import team.chisel.common.block.TileAutoChisel;

public class RenderAutoChisel extends TileEntityRenderer<TileAutoChisel> {
    
    @Override
    public void render(@Nonnull TileAutoChisel te, double x, double y, double z, float partialTicks, int destroyStage) {
        if (te instanceof TileAutoChisel) {
            ItemStack source = te.getSource();

            if (source != null) {
                GlStateManager.pushMatrix();
                GlStateManager.translated(x + 0.5, y + 10/16f, z + 0.5);
                Minecraft.getInstance().getItemRenderer().renderItem(source, TransformType.GROUND);
                GlStateManager.popMatrix();
            }
        }
    }
}
