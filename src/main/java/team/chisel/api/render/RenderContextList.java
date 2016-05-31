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

    private Map<IBlockRenderType, IBlockRenderContext> contextMap = Maps.newIdentityHashMap();

    public RenderContextList() {}

    public RenderContextList(List<IBlockRenderType> types, IBlockAccess world, BlockPos pos) {
        for (IBlockRenderType type : types) {
            IBlockRenderContext ctx = type.getBlockRenderContext(world, pos);
            if (ctx != null) {
                contextMap.put(type, ctx);
            }
        }
    }

    public IBlockRenderContext getRenderContext(IBlockRenderType type) {
        return this.contextMap.get(type);
    }

    public boolean contains(IBlockRenderType type) {
        return getRenderContext(type) != null;
    }

    public TLongSet serialize() {
        TLongSet ret = new TLongHashSet();
        for (IBlockRenderContext ctx : contextMap.values()) {
            ret.add(ctx.getCompressedData());
        }
        return ret;
    }
}
