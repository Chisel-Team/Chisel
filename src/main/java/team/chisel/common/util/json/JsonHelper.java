package team.chisel.common.util.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import team.chisel.api.render.ChiselFace;
import team.chisel.api.render.IChiselTexture;
import team.chisel.common.init.TextureRegistry;
import team.chisel.common.util.PossibleType;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Helps with json stuff
 */
public class JsonHelper {

    private static final Gson gson = new Gson();

    private static Map<ResourceLocation, JsonObject> objectCache = new HashMap<ResourceLocation, JsonObject>();

//    public static PossibleType<IChiselTexture, CombinedChiselTexture> getTextureFromResource(ResourceLocation loc){
//        try {
//            if (TextureRegistry.isTex(loc)){
//                return TextureRegistry.getPossible(loc);
//            }
//            JsonTexture tex = gson.fromJson(new InputStreamReader(Minecraft.getMinecraft().
//                    getResourceManager().getResource(loc).getInputStream()), JsonTexture.class);
//            PossibleType<IChiselTexture, CombinedChiselTexture> type = tex.get(Minecraft.getMinecraft().getTextureMapBlocks());
//            if (type.isFirst()){
//                TextureRegistry.registerTexture(loc, type.getFirst());
//            }
//            else {
//                TextureRegistry.registerTexture(loc, type.getSecond());
//            }
//            return type;
//        } catch (Exception exception){
//            throw new RuntimeException(exception);
//        }
//    }

    public static ChiselFace getFaceFromResource(ResourceLocation loc){

    }

    public static IChiselTexture getTextureFromResource(ResourceLocation loc){

    }

    public static boolean isValid(ResourceLocation loc){
        if (objectCache.containsKey(loc)){
            return true;
        }
        try {
            JsonObject object = gson.fromJson(new InputStreamReader(Minecraft.getMinecraft().
                    getResourceManager().getResource(loc).getInputStream()), JsonObject.class);
            if (object.get("type") != null){
                objectCache.put(loc, object);
            }
        } catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }

    public static boolean isCombined(ResourceLocation loc){

    }
}
