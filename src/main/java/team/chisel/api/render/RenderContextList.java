package team.chisel.api.render;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Maps;

import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TLongHashSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * List of IBlockRenderContext's
 */
@ParametersAreNonnullByDefault
public class RenderContextList {

    private final IBlockState state;
    
    private final Map<IBlockRenderType, IBlockRenderContext> contextMap = Maps.newIdentityHashMap();
    private final TLongSet serialized = new TLongHashSet();

    public RenderContextList(IBlockState state, List<IBlockRenderType> types, IBlockAccess world, BlockPos pos) {
        this.state = state;
        
        for (IBlockRenderType type : types) {
            IBlockRenderContext ctx = type.getBlockRenderContext(state, world, pos);
            if (ctx != null) {
                contextMap.put(type, ctx);
            }
        }
        
        for (IBlockRenderContext ctx : contextMap.values()) {
            serialized.add(ctx.getCompressedData());
        }
    }

    public @Nullable IBlockRenderContext getRenderContext(IBlockRenderType type) {
        return this.contextMap.get(type);
    }

    public boolean contains(IBlockRenderType type) {
        return getRenderContext(type) != null;
    }

    public TLongSet serialized() {
        return serialized;
    }
}
