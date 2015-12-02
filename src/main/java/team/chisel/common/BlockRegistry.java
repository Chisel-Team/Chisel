package team.chisel.common;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import team.chisel.api.block.ChiselBlockData;
import team.chisel.api.block.VariationData;
import team.chisel.common.block.BlockCarvable;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for chisel blocks
 */
public class BlockRegistry {

    private static Map<String, ChiselBlockData> rawBlockData = new HashMap<String, ChiselBlockData>();



    public static void registerBlock(ChiselBlockData data){
        if (!rawBlockData.containsKey(data.name)){
            rawBlockData.put(data.name, data);
        }
        else {
            throw new IllegalArgumentException("Block "+data.name+" has already been registered");
        }
    }

    public static void preInit(FMLPreInitializationEvent event){
        for (ChiselBlockData data : rawBlockData.values()){
            VariationData[][] split = splitVariationArray(data.variations);
            for (int i = 0 ; i < split.length ; i++){
                VariationData[] vars = split[i];
                BlockCarvable block = new BlockCarvable(data, i);
                for (VariationData var : vars){

                }
            }

        }
    }

    private static VariationData[][] splitVariationArray(VariationData[] array) {
        if (array.length <= 16) {
            return new VariationData[][]{array};
        }
        int bound = ((int) array.length / 16) + 1;
        VariationData[][] vars = new VariationData[bound][16];
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
