package team.chisel.client.render;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.message.MessageFormatMessage;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import team.chisel.Chisel;

public class ModelLoaderChisel implements ICustomModelLoader {

    private static final String DEFAULT_MODEL = "{\"model\": { \"model\": \"cube\" }, \"face\":\"%s\"}";
    
    private final Gson gson = ModelBlockDefinition.GSON;
    
    private IResourceManager manager;
    private Map<ResourceLocation, ModelChisel> loadedModels = Maps.newHashMap();
    
    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
        this.manager = resourceManager;
        loadedModels.clear();
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        // Old way of doing things
        if (modelLocation.getResourceDomain().equals("ctm")) {
            return true;
        }
        
        // New way, embedded "ctm" object in model file
        JsonElement json = getJSON(modelLocation);
        return json.isJsonObject() && json.getAsJsonObject().has("ctm");
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws IOException {
        loadedModels.computeIfAbsent(modelLocation, this::loadFromFile);
        ModelChisel model = loadedModels.get(modelLocation);
        if (model != null) {
            model.load();
        }
        return model;
    }
    
    @SuppressWarnings("null")
    private @Nonnull JsonElement getJSON(ResourceLocation modelLocation) {
        ResourceLocation absolute = new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath() + ".json");

        try {
            IResource resource = manager.getResource(absolute);
            JsonElement ele = new JsonParser().parse(new InputStreamReader(resource.getInputStream()));
            if (ele != null) {
                return ele;
            }
        } catch (Exception e) {}
        
        return JsonNull.INSTANCE;
    }

    private ModelChisel loadFromFile(ResourceLocation res) {
        if (res.getResourceDomain().equals("ctm")) {
            @Nonnull String domain = res.getResourcePath();
            String path = "models/block/" + domain.substring(domain.indexOf(":") + 1, domain.length());
            domain = domain.substring(0, domain.indexOf(":"));
            domain = domain.substring(domain.lastIndexOf("/") + 1, domain.length());
            res = new ResourceLocation(domain, path);
        }
        
        JsonElement json = getJSON(res);

        if (json.isJsonObject()) {
            JsonObject obj = json.getAsJsonObject();
            if (obj.has("ctm")) {
                JsonElement ctm = obj.get("ctm");
                if (ctm.isJsonObject()) {
                    obj = ctm.getAsJsonObject();
                } else if (ctm.isJsonPrimitive() && ctm.getAsString().equals("parent")) {
                    return loadFromFile(new ResourceLocation(obj.get("parent").getAsString()));
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
