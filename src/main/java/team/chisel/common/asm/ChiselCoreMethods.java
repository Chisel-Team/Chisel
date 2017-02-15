package team.chisel.common.asm;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import lombok.SneakyThrows;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.BlockRenderLayer;
import team.chisel.api.render.IChiselTexture;
import team.chisel.client.render.ModelChiselBlock;
import team.chisel.common.util.ProfileUtil;

public class ChiselCoreMethods {
    
    private static final Map<IBakedModel, Set<BlockRenderLayer>> cache = new WeakHashMap<>();
      
    private static final Function<IBakedModel, Set<BlockRenderLayer>> loadFunc = (model) -> {
        if (model instanceof ModelChiselBlock) {
            return ((ModelChiselBlock) model).getModel().getChiselTextures().stream()
                    .map(IChiselTexture::getLayer)
                    .collect(Collectors.toCollection(() -> EnumSet.noneOf(BlockRenderLayer.class)));
        }
        return Collections.emptySet();
    };

    static {
        ((SimpleReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(r -> cache.clear());
    }

    @SuppressWarnings("deprecation")
    @SneakyThrows
    public static boolean canRenderInLayer(@Nonnull IBlockState state, @Nonnull BlockRenderLayer layer) {
        ProfileUtil.start("chisel_render_in_layer");
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);

        boolean ret;
        if (model instanceof ModelChiselBlock) {
            ret = cache.computeIfAbsent(model, loadFunc).contains(layer);
        } else {
            ret = state.getBlock().canRenderInLayer(layer);
        }
        ProfileUtil.end();
        return ret;
    }
}
