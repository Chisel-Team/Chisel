package team.chisel.client.render.ctx;

import team.chisel.api.render.IBlockRenderContext;

public class ModuleBlockRenderContext implements IBlockRenderContext{

    private int xModules;

    private int yModules;

    private int zModules;

    private int variationSize;

    public ModuleBlockRenderContext(int xModules, int yModules, int zModules, int variationSize){
        this.xModules = xModules;
        this.yModules = yModules;
        this.zModules = zModules;
        this.variationSize = variationSize;
    }

    public int getXModules(){
        return this.xModules;
    }

    public int getYModules(){
        return this.yModules;
    }

    public int getZModules(){
        return this.zModules;
    }

    public int getVariationSize(){
        return this.variationSize;
    }
}
