package team.chisel.common;

import team.chisel.api.block.ChiselBlockBuilder;
import team.chisel.common.util.ChiselBuilderServerImpl;

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

    public void construct(){
        ChiselBlockBuilder.VariationBuilder.setInterface(new ChiselBuilderServerImpl());
    }

    public void preTextureStitch() {

    }
}
