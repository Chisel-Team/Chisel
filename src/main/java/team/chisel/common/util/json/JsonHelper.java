package team.chisel.common.util.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import team.chisel.api.render.ChiselFace;
import team.chisel.api.render.ChiselFaceRegistry;
import team.chisel.api.render.ChiselTextureRegistry;
import team.chisel.api.render.IChiselTexture;
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

    private static ChiselFace createFace(ResourceLocation loc){
        if (isValid(loc) && isCombined(loc)){
            JsonObject object = objectCache.get(loc);
            JsonFace face = gson.fromJson(object, JsonFace.class);
            ChiselFace cFace = face.get();
            ChiselFaceRegistry.putFace(loc, cFace);
            return cFace;
        }
        else {
            throw new IllegalArgumentException("ResourceLocation "+loc+" is not a valid combined texture!");
        }
    }

    private static IChiselTexture createTexture(ResourceLocation loc){
        if (isValid(loc) && !isCombined(loc)){
            JsonObject object = objectCache.get(loc);
            JsonTexture texture = gson.fromJson(object, JsonTexture.class);
            IChiselTexture cTexture = texture.get();
            ChiselTextureRegistry.putTexture(loc, cTexture);
            return cTexture;

        }
        else {
            throw new IllegalArgumentException("ResourceLocation "+loc+" is not a valid noncombined texture!");
        }
    }

    public static ChiselFace getOrCreateFace(ResourceLocation loc){
        if (ChiselFaceRegistry.isFace(loc)){
            return ChiselFaceRegistry.getFace(loc);
        }
        else {
            return createFace(loc);
        }
    }

    public static IChiselTexture getOrCreateTexture(ResourceLocation loc){
        if (ChiselTextureRegistry.isTex(loc)){
            return ChiselTextureRegistry.getTex(loc);
        }
        else {
            return createTexture(loc);
        }
    }



    public static boolean isValid(ResourceLocation loc){
        if (objectCache.containsKey(loc)){
            return true;
        }
        try {
            JsonObject object = gson.fromJson(new InputStreamReader(Minecraft.getMinecraft().
                    getResourceManager().getResource(loc).getInputStream()), JsonObject.class);
            if (object.has("children") || object.has("textures")){
                objectCache.put(loc, object);
                return true;
            }
            else {
                return false;
            }
        } catch (Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean isCombined(ResourceLocation loc){
        if (isValid(loc)){
            JsonObject object = objectCache.get(loc);
            return object.has("children") && !object.has("textures");
        }
        else {
            return false;
        }
    }
}
