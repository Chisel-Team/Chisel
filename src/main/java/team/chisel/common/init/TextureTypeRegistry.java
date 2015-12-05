package team.chisel.common.init;

import team.chisel.api.render.IBlockRenderType;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Registry for all the different texture types
 */
public class TextureTypeRegistry {

    private static Map<String, IBlockRenderType> map = new HashMap<String, IBlockRenderType>();

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
