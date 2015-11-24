package team.chisel.common.util;

import team.chisel.Chisel;
import team.chisel.api.block.ChiselBlockData;
import team.chisel.api.block.IBlockHandler;
import team.chisel.api.block.VariationData;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.variation.PropertyVariation;
import team.chisel.common.variation.Variation;

/**
 * Represents a created chisel block
 *
 * @author minecreatr
 */
public class ChiselBlock {

    private ChiselBlockData data;
    private BlockCarvable[] blockInstances;
    private PropertyVariation propertyVariation;
    private ChiselVariation[] variations;

    private IBlockHandler handler;

    public ChiselBlock(ChiselBlockData data){
        this.data = data;
        this.propertyVariation = new PropertyVariation();
        variations = new ChiselVariation[data.variations.size()];
        Variation.VariationCreator creator = Variation.creator(propertyVariation);
        for (int i = 0 ; i < data.variations.size() ; i++) {
            VariationData v = data.variations.get(i);
            variations[i] = Chisel.proxy.createVariation(this, creator, v);
        }
        try {
            Class clazz = Class.forName(data.name);
            if (clazz.isAssignableFrom(IBlockHandler.class)){
                this.handler = (IBlockHandler) clazz.newInstance();
                Chisel.debug("Handler for block "+data.name+" is "+data.handler);
            }
            else {
                Chisel.debug(data.handler+" is not a handler, using default handler for block "+data.name);
                this.handler = new DefaultBlockHandler();
            }

        } catch (Exception exception){
            Chisel.debug("Error loading handler "+data.handler+" for block "+data.name);
            this.handler = new DefaultBlockHandler();
        }
    }

    public IBlockHandler getHandler(){
        return this.handler;
    }

    public ChiselBlockData getData(){
        return this.data;
    }

    public void preInit(){
        ChiselVariation[][] split = splitVariationArray(variations);
        for (int i = 0; i < split.length; i++) {
            ChiselVariation[] vArray = split[i];
            BlockCarvable block = new BlockCarvable()
        }


        for (ChiselVariation v : variations){
            v.preInit();
        }
    }

    public ChiselVariation[] getVariations(){
        return this.variations;
    }

    private static ChiselVariation[][] splitVariationArray(ChiselVariation[] array) {
        if (array.length <= 16) {
            return new ChiselVariation[][]{array};
        }
        int bound = ((int) Math.ceil(array.length / 16) + 1);
        ChiselVariation[][] vars = new ChiselVariation[][][bound][16];
        for (int i = 0; i < array.length; i++) {
            int cur = (int) i / 16;
            if (cur >= bound) {
                continue;
            }
            int leftover = (i % 16);
            //Chisel.debug("cur: "+cur+" leftover: "+leftover);
            vars[cur][leftover] = array[i];
        }
        return vars;
    }
}
