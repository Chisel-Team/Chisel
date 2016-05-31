package team.chisel.api.render;


import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TLongHashSet;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.Chisel;

/**
 * List of IBlockRenderContext's
 */
public class RenderContextList {

    private Map<IBlockRenderType, IBlockRenderContext> contextMap;

    public RenderContextList(List<IBlockRenderType> types, IBlockAccess world, BlockPos pos){
        contextMap = new IdentityHashMap<>();
        for (IBlockRenderType type : types){
            IBlockRenderContext ctx = type.getBlockRenderContext(world, pos);
            if (ctx != null) {
                contextMap.put(type, ctx);
            }
        }
    }

    public IBlockRenderContext getRenderContext(IBlockRenderType type){
        try {
            return this.contextMap.get(type);
        } catch (ClassCastException exception){
            Chisel.debug("Contextmap had a bad type context pair");
            throw exception;
        }
    }

    public boolean contains(IBlockRenderType type){
        return getRenderContext(type) != null;
    }

    public RenderContextList(){

    }

    public TLongSet serialize() {
        TLongSet ret = new TLongHashSet();
        for (IBlockRenderContext ctx : contextMap.values()) {
            ret.add(ctx.getCompressedData());
        }
        return ret;
    }
}
