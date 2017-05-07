package team.chisel.common.asm;

import javax.annotation.Nonnull;

import lombok.SneakyThrows;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.client.render.GlowRenderer;
import team.chisel.client.render.ModelChiselBlockOld;
import team.chisel.common.util.ProfileUtil;

@SuppressWarnings("unused")
public class ChiselCoreMethods {
    
    @SuppressWarnings("deprecation")
    @SneakyThrows
    public static boolean canRenderInLayer(@Nonnull IBlockState state, @Nonnull BlockRenderLayer layer) {
        ProfileUtil.start("chisel_render_in_layer");
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);

        boolean ret;
        if (model instanceof ModelChiselBlockOld) {
            ret = ((ModelChiselBlockOld)model).getModel().canRenderInLayer(layer);
        } else {
            ret = state.getBlock().canRenderInLayer(layer);
        }
        ProfileUtil.end();
        return ret;
    }
    
    public static ThreadLocal<Boolean> renderingDamageModel = ThreadLocal.withInitial(() -> false);
    
    public static void preDamageModel() {
        renderingDamageModel.set(true);
    }
    
    public static void postDamageModel() {
        renderingDamageModel.set(false);
    }

    @SideOnly(Side.CLIENT)
    public static void renderGlow(ItemStack stack, IBakedModel model) {
        GlowRenderer.glow(stack, model);
    }

    @SideOnly(Side.CLIENT)
    public static boolean renderGlow(BlockModelRenderer blockModelRenderer, IBlockAccess world, IBakedModel model, IBlockState state, BlockPos pos, VertexBuffer buf) {
        return GlowRenderer.glow(blockModelRenderer, world, model, state, pos, buf);
    }
}
