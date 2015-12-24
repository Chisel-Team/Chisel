package team.chisel.common;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import team.chisel.api.block.ChiselBlockBuilder.VariationBuilder.IVariationBuilderDelegate;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.util.BuilderDelegateServer;

public class CommonProxy implements Reference {

    public void construct(FMLPreInitializationEvent event) {
    }

    public void preInit(FMLPreInitializationEvent event) {
    }

    public void init() {
    }

    public void preTextureStitch() {
    }

    public void initiateFaceData(BlockCarvable carvable) {
    }

    private static final IVariationBuilderDelegate SERVER_DELEGATE = new BuilderDelegateServer();

    public IVariationBuilderDelegate getBuilderDelegate() {
        return SERVER_DELEGATE;
    }
}
