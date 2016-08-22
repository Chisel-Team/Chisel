package team.chisel.client.render;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import gnu.trove.set.TLongSet;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;


public enum ModelCache {
    
    INSTANCE;
    
    static {
       // MinecraftForge.EVENT_BUS.register(INSTANCE);
    }
    
    @Value
    @AllArgsConstructor
    static class State {
        IBlockState cleanState;
        TLongSet serializedContext;
    }

    private static final Cache<State, ModelChiselBlock> modelcache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).maximumSize(500).<State, ModelChiselBlock>build();
    
    // TODO test if this invalidation strategy works. Otherwise switch to event handler invalidation.
    private static final Cache<Pair<IBlockAccess, BlockPos>, State> statecache = CacheBuilder.newBuilder().expireAfterWrite(100, TimeUnit.MILLISECONDS).build();
    
    @SneakyThrows
    @Nonnull
    ModelChiselBlock getModel(@Nonnull State state, @Nonnull Callable<ModelChiselBlock> loader) {
        return modelcache.get(state, loader);
    }
    
    @SuppressWarnings("null")
    @SneakyThrows
    @Nonnull
    State getState(IBlockAccess world, BlockPos pos, @Nonnull Callable<State> loader) {
        return statecache.get(Pair.of(world, pos), loader);
    }
}
