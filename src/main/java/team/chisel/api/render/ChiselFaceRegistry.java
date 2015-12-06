package team.chisel.api.render;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ChiselFaceRegistry {

    private static Map<ResourceLocation, ChiselFace> map = new HashMap<ResourceLocation, ChiselFace>();


    public static void putFace(ResourceLocation loc, ChiselFace tex){
        map.put(loc, tex);
    }

    public static ChiselFace getFace(ResourceLocation loc){
        return map.get(loc);
    }

    public static void removeFace(ResourceLocation loc){
        map.remove(loc);
    }

    public static boolean isFace(ResourceLocation loc){
        return map.containsKey(loc);
    }
}
