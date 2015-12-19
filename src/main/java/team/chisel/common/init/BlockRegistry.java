package team.chisel.common.init;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import team.chisel.Chisel;
import team.chisel.api.block.ChiselBlockData;
import team.chisel.api.block.VariationData;
import team.chisel.client.render.ChiselModelRegistry;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.block.ItemChiselBlock;
import team.chisel.common.carving.Carving;

/**
 * Registry for chisel blocks
 */
public class BlockRegistry {

    private static Map<String, ChiselBlockData> rawBlockData = new HashMap<String, ChiselBlockData>();

    private static Map<String, BlockCarvable> blocks = new HashMap<>();

    public static void registerBlock(ChiselBlockData data){
        if (data == null){
            throw new IllegalArgumentException("Block Data cannot be null!");
        }
        if (!rawBlockData.containsKey(data.name)){
            rawBlockData.put(data.name, data);
        }
        else {
            throw new IllegalArgumentException("Block "+data.name+" has already been registered");
        }
    }

    public static Collection<ChiselBlockData> getAllBlockData(){
        return rawBlockData.values();
    }

    public static void preInit(FMLPreInitializationEvent event){
        for (ChiselBlockData data : rawBlockData.values()){
            if (data == null){
                Chisel.debug("Skipping block cause null data?");
                continue;
            }
            VariationData[][] split = splitVariationArray(data.variations);
            for (int i = 0; i < split.length; i++) {
                VariationData[] vars = split[i];
                BlockCarvable block = new BlockCarvable(data, i);
                GameRegistry.registerBlock(block, ItemChiselBlock.class, block.getName());
                blocks.put(block.getName(), block);
                for (int meta = 0; i < vars.length; i++) {
                    if (vars[meta].group != null) {
                        Carving.chisel.addVariation(vars[meta].group, block.getDefaultState().withProperty(block.metaProp, meta), meta + (i * 16));
                    }
                }
                if (event.getSide().isClient()) {
                    // TODO Seperate Client and server code more
                    ChiselModelRegistry.register(block);
                }
            }

        }
    }

    public static List<BlockCarvable> getAllBlocks(){
        return new ArrayList<>(blocks.values());
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
