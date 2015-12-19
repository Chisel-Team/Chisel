package team.chisel.common.util.json;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import team.chisel.Chisel;
import team.chisel.api.render.IChiselFace;
import team.chisel.api.render.IChiselTexture;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonHelper {

    private static final Gson gson = new Gson();

    private static Map<ResourceLocation, JsonObject> objectCache = new HashMap<ResourceLocation, JsonObject>();

    private static Map<ResourceLocation, IChiselFace> faceCache = new HashMap<>();

    private static Map<ResourceLocation, IChiselTexture> textureCache = new HashMap<>();

    private static IChiselFace createFace(ResourceLocation loc) {
        checkCombined(true, loc);

        JsonObject object = objectCache.get(loc);
        JsonFace face = gson.fromJson(object, JsonFace.class);
        IChiselFace cFace = face.get(loc);
        faceCache.put(loc, cFace);
        return cFace;
    }

    private static IChiselTexture createTexture(ResourceLocation loc) {
        checkCombined(false, loc);

        JsonObject object = objectCache.get(loc);
        JsonTexture texture = gson.fromJson(object, JsonTexture.class);
        IChiselTexture cTexture = texture.get(loc);
        textureCache.put(loc, cTexture);
        return cTexture;
    }

    public static void flushCaches(){
        objectCache = new HashMap<>();
        faceCache = new HashMap<>();
        textureCache = new HashMap<>();
        Chisel.debug("Flushing Json caches");
    }

    public static IChiselFace getOrCreateFace(ResourceLocation loc) {
        if (faceCache.containsKey(loc)) {
            return faceCache.get(loc);
        } else {
            return createFace(loc);
        }
    }

    public static IChiselTexture getOrCreateTexture(ResourceLocation loc) {
        if (textureCache.containsKey(loc)) {
            return textureCache.get(loc);
        } else {
            return createTexture(loc);
        }
    }

    public static void checkValid(ResourceLocation loc) {
        if (objectCache.containsKey(loc)) {
            return;
        }
        try {
            JsonObject object = gson.fromJson(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream()), JsonObject.class);
            if (object.has("children") || object.has("type")) {
                objectCache.put(loc, object);
            } else {
                throw new IllegalArgumentException(loc + " does not have a 'children' and/or 'type' field!");
            }
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }
    }
    
    public static boolean isCombined(ResourceLocation loc) {
        checkValid(loc);

        JsonObject object = objectCache.get(loc);
        return object.has("children") && !object.has("type");
    }

    public static boolean isFace(ResourceLocation loc){
        return faceCache.containsKey(loc);
    }

    public static boolean isTex(ResourceLocation loc){
        return textureCache.containsKey(loc);
    }



    public static void checkCombined(boolean isCombined, ResourceLocation loc) {
        boolean check = isCombined(loc);
        
        if (!isCombined) {
            check = !check;
        }
        if (!check) {
            throw new IllegalArgumentException(loc.toString() + " must " + (isCombined ? "be" : "not be") + " a combined texture!");
        }
    }

    public static boolean isLocalPath(String path) {
        return path.startsWith("./");
    }
    
    public static String toAbsolutePath(String localPath, ResourceLocation loc) {
        String path = loc.getResourcePath();
        path = path.substring(0, path.lastIndexOf('/') + 1);
        return loc.getResourceDomain() + ":" + path + localPath.substring(2);
    }

    public static String toTexturePath(String resourcePath) {
        return resourcePath.replace("textures/", "").replace(".json", "");
    }
}
