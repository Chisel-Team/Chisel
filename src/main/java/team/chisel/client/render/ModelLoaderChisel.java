package team.chisel.client.render;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import team.chisel.Chisel;
import team.chisel.ctm.api.model.IModelCTM;
import team.chisel.ctm.client.model.parsing.ModelLoaderCTM;

@SuppressWarnings("deprecation")
public enum ModelLoaderChisel implements ICustomModelLoader {
    
    INSTANCE;

    private static final String DEFAULT_MODEL = "{\"model\": { \"model\": \"cube\" }, \"face\":\"%s\"}";

    private final Gson gson = ModelBlockDefinition.GSON;
    private Map<ResourceLocation, IModelCTM> loadedModels = Maps.newHashMap();
        
    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
        loadedModels.clear();
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        // Old way of doing things
        if (modelLocation.getResourceDomain().equals("ctm")) {
            return true;
        }
        
        // New way, embedded "ctm" object in model file
        JsonElement json = ModelLoaderCTM.INSTANCE.getJSON(modelLocation);
        return json.isJsonObject() && json.getAsJsonObject().has("ctm");
    }

    // TODO this code is duplicated from ModelLoaderCTM - find a good way to share?
    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws IOException {
        loadedModels.computeIfAbsent(modelLocation, res -> loadFromFile(res, true));
        IModelCTM model = loadedModels.get(modelLocation);
        if (model != null) {
            model.load();
        }
        return model;
    }

    private IModelCTM loadFromFile(ResourceLocation res, boolean forLoad) {
        if (res.getResourceDomain().equals("ctm")) {
            @Nonnull String domain = res.getResourcePath();
            String path = "models/block/" + domain.substring(domain.indexOf(":") + 1, domain.length());
            domain = domain.substring(0, domain.indexOf(":"));
            domain = domain.substring(domain.lastIndexOf("/") + 1, domain.length());
            res = new ResourceLocation(domain, path);
        }

        JsonElement json = ModelLoaderCTM.INSTANCE.getJSON(res);

        if (json.isJsonObject()) {
            JsonObject obj = json.getAsJsonObject();
            if (obj.has("ctm")) {
                JsonElement ctm = obj.get("ctm");
                if (ctm.isJsonObject()) {
                    obj = ctm.getAsJsonObject();
                } else if (ctm.isJsonPrimitive() && ctm.getAsString().equals("parent")) {
                    return loadFromFile(new ResourceLocation(obj.get("parent").getAsString()), true);
                }
            }
            return gson.fromJson(obj, ModelChisel.class);
        } else if (json.isJsonNull()) {
            Chisel.debug("Substituting default model json for missing file " + res);
            return gson.fromJson(String.format(DEFAULT_MODEL, res.toString().replace("models/block/", "").concat(".cf")), ModelChisel.class);
        } else {
            throw new IllegalArgumentException("Found invalid JSON information \"" + json + "\" loading model " + res);
        }
    }
}
