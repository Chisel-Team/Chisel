package team.chisel.common;

import team.chisel.api.block.ClientVariationData;
import team.chisel.api.block.VariationData;
import team.chisel.common.util.ChiselBlock;
import team.chisel.common.util.ChiselVariation;
import team.chisel.common.variation.Variation;

/**
 * The Common Proxy
 */
public class CommonProxy implements Reference{

    public void init() {

    }

    public boolean isClient() {
        return false;
    }

    public void preInit() {

    }

    public void preTextureStitch() {

    }

    public ChiselVariation createVariation(ChiselBlock parent, Variation.VariationCreator creator, VariationData data){
        return new ChiselVariation(parent, creator, data);
    }
}
