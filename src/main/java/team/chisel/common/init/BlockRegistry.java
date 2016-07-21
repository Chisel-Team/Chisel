package team.chisel.common.init;

import team.chisel.api.block.VariationData;

import java.util.Arrays;

/**
 * Registry for chisel blocks
 */
public class BlockRegistry {

//    public static void preInit(FMLPreInitializationEvent event){
//        for (ChiselBlockData data : rawBlockData.values()){
//            if (data == null){
//                Chisel.debug("Skipping block cause null data?");
//                continue;
//            }
//            VariationData[][] split = splitVariationArray(data.variations);
//            for (int i = 0; i < split.length; i++) {
//                VariationData[] vars = split[i];
//                BlockCarvable block = new BlockCarvable(data, i);
//                GameRegistry.registerBlock(block, ItemChiselBlock.class, block.getName());
//                blocks.put(block.getName(), block);
//                for (int meta = 0; i < vars.length; i++) {
//                    if (vars[meta].group != null) {
//                        Carving.chisel.addVariation(vars[meta].group, block.getDefaultState().withProperty(block.metaProp, meta), meta + (i * 16));
//                    }
//                }
//                if (event.getSide().isClient()) {
//                    // TODO Seperate Client and server code more
//                    ChiselModelRegistry.INSTANCE.register(block);
//                }
//            }
//        }
//    }


    public static VariationData[][] splitVariationArray(VariationData[] array) {
        if (array.length <= 16) {
            return new VariationData[][]{array};
        }

        VariationData[] first = Arrays.copyOf(array, 16);
        int bound = array.length / 16;
        if (array.length % 16 != 0) {
            bound++;
        }
        VariationData[][] vars = new VariationData[bound][];
        for (int i = 0; i < array.length; i++) {
            int cur = (int) i / 16;
            if (cur >= bound) {
                continue;
            }
            int leftover = (i % 16);
            if (vars[cur] == null) {
                vars[cur] = new VariationData[Math.min(16, array.length - i)];
            }
            //Chisel.debug("cur: "+cur+" leftover: "+leftover);
            vars[cur][leftover] = array[i];
        }
        return vars;
    }
}
