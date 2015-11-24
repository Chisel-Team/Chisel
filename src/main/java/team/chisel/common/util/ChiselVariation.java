package team.chisel.common.util;

import team.chisel.api.block.VariationData;
import team.chisel.common.variation.Variation;

public class ChiselVariation {

    protected ChiselBlock parent;
    protected VariationData data;
    private Variation variation;

    public ChiselVariation(ChiselBlock parent, Variation.VariationCreator creator, VariationData data){
        this.parent = parent;
        this.data = data;
        this.variation = creator.value(data.name);
    }


    public Variation getVariation(){
        return this.variation;
    }

    public void preInit(){}

    public void init(){}
}