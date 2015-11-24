package team.chisel.common;

import team.chisel.api.block.ChiselBlockData;

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


}
