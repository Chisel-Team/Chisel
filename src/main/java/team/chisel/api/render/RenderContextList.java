package team.chisel.api.render;


import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * List of IBlockRenderContext's
 */
public class RenderContextList {

    private Map<String, IBlockRenderContext> contextMap;

    public RenderContextList(List<IBlockRenderType> types, IBlockAccess world, BlockPos pos){
        contextMap = new HashMap<String, IBlockRenderContext>();
        for (IBlockRenderType type : types){
            contextMap.put(type.getName(), type.getBlockRenderContext(world, pos));
        }
    }
}
