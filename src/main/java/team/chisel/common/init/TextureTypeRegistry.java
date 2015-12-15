package team.chisel.common.init;

import team.chisel.api.render.IBlockRenderType;
import team.chisel.client.render.type.BlockRenderTypeCTM;
import team.chisel.client.render.type.BlockRenderTypeCTMH;
import team.chisel.client.render.type.BlockRenderTypeCTMV;
import team.chisel.client.render.type.BlockRenderTypeNormal;
import team.chisel.client.render.type.BlockRenderTypeV;
import team.chisel.client.render.type.BlockRenderTypeV4;
import team.chisel.client.render.type.BlockRenderTypeV9;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Registry for all the different texture types
 */
public class TextureTypeRegistry {

    private static Map<String, IBlockRenderType> map;

    static {
        map = new HashMap<String, IBlockRenderType>();
        map.put("CTM", new BlockRenderTypeCTM());
        map.put("NORMAL", new BlockRenderTypeNormal());
        map.put("CTMH", new BlockRenderTypeCTMH());
        map.put("CTMV", new BlockRenderTypeCTMV());
        map.put("V4", new BlockRenderTypeV4());
        map.put("V9", new BlockRenderTypeV9());
    }


    public static void register(String name, IBlockRenderType type){
        String key = name.toUpperCase(Locale.US);
        if (map.containsKey(key) && map.get(key) != type){
            throw new IllegalArgumentException("Render Type with name "+key+" has already been registered!");
        }
        else if (map.get(key) != type){
            map.put(key, type);
        }
    }

    public static IBlockRenderType getType(String name){
        String key = name.toUpperCase(Locale.US);
        return map.get(key);
    }

    public static boolean isValid(String name){
        return map.containsKey(name);
    }
}
