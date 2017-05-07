package team.chisel.common.asm;

import javax.annotation.Nonnull;

import lombok.SneakyThrows;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.WeightedBakedModel;
import net.minecraft.util.BlockRenderLayer;
import team.chisel.client.render.AbstractChiselBakedModel;
import team.chisel.common.util.ProfileUtil;

public class ChiselCoreMethods {
    
    @SuppressWarnings("deprecation")
    @SneakyThrows
    public static boolean canRenderInLayer(@Nonnull IBlockState state, @Nonnull BlockRenderLayer layer) {
        ProfileUtil.start("chisel_render_in_layer");
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        if (model instanceof WeightedBakedModel) {
            model = ((WeightedBakedModel)model).baseModel;
        }
        
        boolean ret;
        if (model instanceof AbstractChiselBakedModel) {
            ret = ((AbstractChiselBakedModel)model).getModel().canRenderInLayer(state, layer);
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
}
