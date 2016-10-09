package team.chisel.common.asm;

import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.BlockRenderLayer;
import team.chisel.client.render.ModelChiselBlock;

public class ChiselCoreMethods {
    
    @SuppressWarnings("deprecation")
    public static boolean canRenderInLayer(@Nonnull IBlockState state, @Nonnull BlockRenderLayer layer) {
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        if (model instanceof ModelChiselBlock) {
            return ((ModelChiselBlock) model).getModel().getChiselTextures().stream().map(t -> t.getLayer()).collect(Collectors.toList()).contains(layer);
        }
        return state.getBlock().canRenderInLayer(layer);
    }

}
