package team.chisel.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import team.chisel.common.block.TileAutoChisel;

public class RenderAutoChisel implements BlockEntityRenderer<TileAutoChisel> {

    private final BlockEntityRendererProvider.Context context;

    public RenderAutoChisel(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(TileAutoChisel te, float partialTicks, PoseStack stack, MultiBufferSource buf, int light, int overlay) {
        if (te instanceof TileAutoChisel) {
            ItemStack source = te.getSource();

            if (source != null) {
                stack.pushPose();
                stack.translate(0.5, 10 / 16f, 0.5);
                Minecraft.getInstance().getItemRenderer().renderStatic(source, TransformType.GROUND, light, overlay, stack, buf, 0);
                stack.popPose();
            }
        }
    }
}
