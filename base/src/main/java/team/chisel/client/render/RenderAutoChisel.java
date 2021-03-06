package team.chisel.client.render;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import team.chisel.common.block.TileAutoChisel;

@ParametersAreNonnullByDefault
public class RenderAutoChisel extends TileEntityRenderer<TileAutoChisel> {
    
    public RenderAutoChisel(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }
    
    @Override
    public void render(TileAutoChisel te, float partialTicks, MatrixStack stack, IRenderTypeBuffer buf, int light, int overlay) {
        if (te instanceof TileAutoChisel) {
            ItemStack source = te.getSource();

            if (source != null) {
                stack.push();
                stack.translate(0.5, 10/16f, 0.5);
                Minecraft.getInstance().getItemRenderer().renderItem(source, TransformType.GROUND, light, overlay, stack, buf);
                stack.pop();
            }
        }
    }
}
