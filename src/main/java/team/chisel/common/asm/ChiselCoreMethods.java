package team.chisel.common.asm;

import javax.annotation.Nonnull;

import lombok.SneakyThrows;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.client.render.ModelChiselBlock;
import team.chisel.common.util.ProfileUtil;

public class ChiselCoreMethods {
    
    public static boolean canRenderInLayer(@Nonnull IBlockState state, @Nonnull BlockRenderLayer layer) {
        ProfileUtil.start("chisel_render_in_layer");
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);

        boolean ret;
        if (model instanceof ModelChiselBlock) {
            ret = ((ModelChiselBlock)model).getModel().canRenderInLayer(layer);
        } else {
            ret = state.getBlock().getBlockLayer() == layer;
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
