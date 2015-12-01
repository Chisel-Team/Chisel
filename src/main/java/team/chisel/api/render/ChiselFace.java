package team.chisel.api.render;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.util.EnumFacing;

/**
 * Chisel Face, basicly a list of IChiselTexture's
 */
public class ChiselFace {

    private Multimap<EnumFacing, IChiselTexture<? extends IBlockRenderContext>> textureMultimap;

    public ChiselFace(){
        this.textureMultimap = HashMultimap.create();
    }

}
