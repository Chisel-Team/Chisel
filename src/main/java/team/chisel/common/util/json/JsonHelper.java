package team.chisel.common.util.json;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import team.chisel.api.render.ChiselFace;
import team.chisel.api.render.ChiselFaceRegistry;
import team.chisel.api.render.ChiselTextureRegistry;
import team.chisel.api.render.IChiselTexture;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonHelper {

    private static final Gson gson = new Gson();

    private static Map<ResourceLocation, JsonObject> objectCache = new HashMap<ResourceLocation, JsonObject>();

    private static ChiselFace createFace(ResourceLocation loc) {
        checkCombined(true, loc);

        JsonObject object = objectCache.get(loc);
        JsonFace face = gson.fromJson(object, JsonFace.class);
        ChiselFace cFace = face.get();
        ChiselFaceRegistry.putFace(loc, cFace);
        return cFace;
    }

    private static IChiselTexture createTexture(ResourceLocation loc) {
        checkCombined(false, loc);

        JsonObject object = objectCache.get(loc);
        JsonTexture texture = gson.fromJson(object, JsonTexture.class);
        IChiselTexture cTexture = texture.get();
        ChiselTextureRegistry.putTexture(loc, cTexture);
        return cTexture;
    }

    public static ChiselFace getOrCreateFace(ResourceLocation loc) {
        if (ChiselFaceRegistry.isFace(loc)) {
            return ChiselFaceRegistry.getFace(loc);
        } else {
            return createFace(loc);
        }
    }

    public static IChiselTexture getOrCreateTexture(ResourceLocation loc) {
        if (ChiselTextureRegistry.isTex(loc)) {
            return ChiselTextureRegistry.getTex(loc);
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
            if (object.has("children") || object.has("textures")) {
                objectCache.put(loc, object);
            } else {
                throw new IllegalArgumentException(loc + " does not have a 'children' and/or 'textures' field!");
            }
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }
    }
    
    public static boolean isCombined(ResourceLocation loc) {
        checkValid(loc);

        JsonObject object = objectCache.get(loc);
        return object.has("children") && !object.has("textures");
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
}
