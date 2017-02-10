package team.chisel.common.asm;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

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
    
    private static final LoadingCache<IBlockState, Set<BlockRenderLayer>> cache = CacheBuilder.newBuilder().build(new CacheLoader<IBlockState, Set<BlockRenderLayer>>() {
        
        @SuppressWarnings("null")
        @Override
        public @Nonnull Set<BlockRenderLayer> load(@Nonnull IBlockState state) throws Exception {
            IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
            if (model instanceof ModelChiselBlock) {
                return ((ModelChiselBlock) model).getModel().getChiselTextures().stream()
                        .map(IChiselTexture::getLayer)
                        .collect(Collectors.toCollection(() -> EnumSet.noneOf(BlockRenderLayer.class)));
            }
            return Collections.emptySet();
        }
    });
    
    static {
        ((SimpleReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(r -> cache.invalidateAll());
    }

    @SuppressWarnings("deprecation")
    @SneakyThrows
    public static boolean canRenderInLayer(@Nonnull IBlockState state, @Nonnull BlockRenderLayer layer) {
        ProfileUtil.start("chisel_render_in_layer");
        Set<BlockRenderLayer> layers = cache.get(state);
        if (layers.isEmpty()) {
            ProfileUtil.end();
            return state.getBlock().canRenderInLayer(layer);
        } else {
            ProfileUtil.end();
            return layers.contains(layer);
        }
    }
}
