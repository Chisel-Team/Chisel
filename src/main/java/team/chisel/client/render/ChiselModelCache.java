package team.chisel.client.render;

import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.client.renderer.block.model.BakedQuad;

import java.util.List;

public class ChiselModelCache {


    private TIntObjectHashMap<List<BakedQuad>> faceCache;

    public ChiselModelCache(){
        faceCache = new TIntObjectHashMap<List<BakedQuad>>();
    }

}
