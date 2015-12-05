package team.chisel.common.util;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Not actually an IChiselTexture
 * Just used internally for storing links to resource locations for a combined texture
 */
public class CombinedChiselTexture {

    private List<ResourceLocation> texs;

    public CombinedChiselTexture(List<ResourceLocation> texs){
        this.texs = texs;
    }

    public CombinedChiselTexture(){
        this.texs = new ArrayList<ResourceLocation>();
    }

    public void addTexture(ResourceLocation loc){
        if (!texs.contains(loc)){
            texs.add(loc);
        }
    }

    public boolean removeTexture(ResourceLocation loc){
        return texs.remove(loc);
    }
}
