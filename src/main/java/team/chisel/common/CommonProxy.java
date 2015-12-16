package team.chisel.common;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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

    public void preInit(FMLPreInitializationEvent event) {

    }

    public void construct(FMLPreInitializationEvent event){
        ChiselBlockBuilder.VariationBuilder.setInterface(new ChiselBuilderServerImpl());
    }

    public void preTextureStitch() {

    }
}
