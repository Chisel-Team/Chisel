package team.chisel.common.init;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import team.chisel.api.render.BlockRenderType;
import team.chisel.api.render.IBlockRenderType;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;

/**
 * Registry for all the different texture types
 */
public class TextureTypeRegistry {

    private static Map<String, IBlockRenderType> map = Maps.newHashMap();

    @SuppressWarnings("unchecked")
    public static void preInit(FMLPreInitializationEvent event) {
        Set<ASMData> annots = event.getAsmData().getAll(BlockRenderType.class.getName());
        for (ASMData data : annots) {
            try {
                String name = (String) data.getAnnotationInfo().get("value");
                register(name, ((Class<? extends IBlockRenderType>) Class.forName(data.getClassName())).newInstance());
            } catch (Exception e) {
                Throwables.propagate(e);
            }
        }
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
