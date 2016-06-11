package team.chisel.api.render;

import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TLongHashSet;

import java.util.List;
import java.util.Map;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import com.google.common.collect.Maps;

/**
 * List of IBlockRenderContext's
 */
public class RenderContextList {

    private final Map<IBlockRenderType, IBlockRenderContext> contextMap = Maps.newIdentityHashMap();
    private final TLongSet serialized = new TLongHashSet();

    public RenderContextList() {}

    public RenderContextList(List<IBlockRenderType> types, IBlockAccess world, BlockPos pos) {
        for (IBlockRenderType type : types) {
            IBlockRenderContext ctx = type.getBlockRenderContext(world, pos);
            if (ctx != null) {
                contextMap.put(type, ctx);
            }
        }
        
        for (IBlockRenderContext ctx : contextMap.values()) {
            serialized.add(ctx.getCompressedData());
        }
    }

    public IBlockRenderContext getRenderContext(IBlockRenderType type) {
        return this.contextMap.get(type);
    }

    public boolean contains(IBlockRenderType type) {
        return getRenderContext(type) != null;
    }

    public TLongSet serialized() {
        TLongSet ret = new TLongHashSet();
        for (IBlockRenderContext ctx : contextMap.values()) {
            ret.add(ctx.getCompressedData());
        }
        return ret;
    }
}
