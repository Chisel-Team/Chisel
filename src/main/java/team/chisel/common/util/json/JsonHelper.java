package team.chisel.common.util.json;

import java.io.IOException;
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
    
    private static RuntimeException cachedException;

    private static final Gson gson = new Gson();

    private static Map<ResourceLocation, JsonObject> objectCache = new HashMap<ResourceLocation, JsonObject>();

    private static Map<ResourceLocation, IChiselFace> faceCache = new HashMap<>();

    private static Map<ResourceLocation, IChiselTexture<?>> textureCache = new HashMap<>();

    private static IChiselFace createFace(ResourceLocation loc) {
        if (isValidFace(loc)) {
            JsonObject object = objectCache.get(loc);
            JsonFace face = gson.fromJson(object, JsonFace.class);
            IChiselFace cFace = face.get(loc);
            faceCache.put(loc, cFace);
            return cFace;
        }
        throw clearException();
    }

    private static IChiselTexture<?> createTexture(ResourceLocation loc) {
        if (isCombinedTexture(false, loc)) {
            JsonObject object = objectCache.get(loc);
            JsonTexture texture = gson.fromJson(object, JsonTexture.class);
            IChiselTexture<?> cTexture = texture.get(loc);
            textureCache.put(loc, cTexture);
            return cTexture;
        }
        throw clearException();
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

    public static IChiselTexture<?> getOrCreateTexture(ResourceLocation loc) {
        if (textureCache.containsKey(loc)) {
            return textureCache.get(loc);
        } else {
            return createTexture(loc);
        }
    }

    public static boolean isValidTexture(ResourceLocation loc) {
        ResourceLocation absolute = new ResourceLocation(loc.getResourceDomain(), "textures/blocks/" + loc.getResourcePath());
        return isValid(loc, absolute);
    }
    
    public static boolean isValidFace(ResourceLocation loc) {
        // TODO put this somewhere statically accessible
        ResourceLocation absolute = new ResourceLocation(loc.getResourceDomain(), "models/block/" + loc.getResourcePath());
        if (isValid(loc, absolute)) {
            JsonObject obj = objectCache.get(loc);
            return obj.has("children") && !obj.has("type");
        }
        return false;
    }
    
    private static boolean isValid(ResourceLocation relative, ResourceLocation absolute) {
        clearException();
        if (objectCache.containsKey(relative)) {
            return true;
        }
        JsonObject object;
        try {
            object = gson.fromJson(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(absolute).getInputStream()), JsonObject.class);
        } catch (IOException e) {
            cachedException = new RuntimeException(e);
            return false;
        }
        if (object.has("children") || object.has("type")) {
            objectCache.put(relative, object);
            return true;
        } else {
            throw new IllegalArgumentException(relative + " does not have a 'children' and/or 'type' field!");
        }
    }

    public static boolean isCombinedTexture(boolean combined, ResourceLocation loc) {
        if (isValidTexture(loc)) {
            JsonObject object = objectCache.get(loc);
            boolean ret = object.has("children") && !object.has("type");
            return ret == combined;
        }
        return false;
    }
    
    public static RuntimeException clearException() {
        RuntimeException e = cachedException;
        cachedException = null;
        return e;
    }

    public static boolean isFace(ResourceLocation loc){
        return faceCache.containsKey(loc);
    }

    public static boolean isTex(ResourceLocation loc){
        return textureCache.containsKey(loc);
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
        String s = resourcePath.replace("textures/", "").replace(".ctx", "");
        if (!s.startsWith("blocks")) {
            s = "blocks/".concat(s);
        }
        return s;
    }
}
