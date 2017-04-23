package team.chisel.client.render;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import team.chisel.Chisel;
import team.chisel.api.render.IModelChisel;
import team.chisel.api.render.IModelParser;

@SuppressWarnings("deprecation")
public class ModelLoaderChisel implements ICustomModelLoader {

    private static final String DEFAULT_MODEL = "{\"model\": { \"model\": \"cube\" }, \"face\":\"%s\"}";
    
    // This comes from ModelBlock#SERIALIZER
    static final Gson SERIALIZER = new GsonBuilder()
            .registerTypeAdapter(ModelBlock.class, new ModelBlock.Deserializer())
            .registerTypeAdapter(BlockPart.class, new BlockPart.Deserializer())
            .registerTypeAdapter(BlockPartFace.class, new BlockPartFace.Deserializer())
            .registerTypeAdapter(BlockFaceUV.class, new BlockFaceUV.Deserializer())
            .registerTypeAdapter(ItemTransformVec3f.class, new ItemTransformVec3f.Deserializer())
            .registerTypeAdapter(ItemCameraTransforms.class, new ItemCameraTransforms.Deserializer())
            .registerTypeAdapter(ItemOverride.class, new ItemOverride.Deserializer()).create();
    
    private static final Map<Integer, IModelParser> parserVersions = ImmutableMap.of(1, new ModelParserV1());

    private final Gson gson = ModelBlockDefinition.GSON;
    
    private IResourceManager manager;
    private Map<ResourceLocation, JsonElement> jsonCache = Maps.newHashMap();
    private Map<ResourceLocation, IModelChisel> loadedModels = Maps.newHashMap();
        
    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
        this.manager = resourceManager;
        loadedModels.clear();
    }
    
    private final Set<ResourceLocation> loading = new HashSet<>();

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        if (modelLocation instanceof ModelResourceLocation) {
            modelLocation = new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath());
        }
        if (loading.contains(modelLocation)) {
            return false;
        }
        
        // Old way of doing things
        if (modelLocation.getResourceDomain().equals("ctm")) {
            return true;
        }
        
        // New way, embedded "ctm" object in model file
        JsonElement json = getJSON(modelLocation);
        return json.isJsonObject() && (json.getAsJsonObject().has("ctm") || json.getAsJsonObject().has("ctm_version"));
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws IOException {
        loading.add(modelLocation);
        loadedModels.computeIfAbsent(modelLocation, this::loadFromFile);
        IModelChisel model = loadedModels.get(modelLocation);
        if (model != null) {
            model.load();
        }
        loading.remove(modelLocation);
        return model;
    }
    
    @SuppressWarnings("null")
    private @Nonnull JsonElement getJSON(ResourceLocation modelLocation) {
        return jsonCache.computeIfAbsent(modelLocation, res -> {
            ResourceLocation absolute = new ResourceLocation(res.getResourceDomain(), res.getResourcePath() + ".json");

            try {
                IResource resource = manager.getResource(absolute);
                JsonElement ele = new JsonParser().parse(new InputStreamReader(resource.getInputStream()));
                if (ele != null) {
                    return ele;
                }
            } catch (Exception e) {}

            return JsonNull.INSTANCE;
        });
    }

    private IModelChisel loadFromFile(ResourceLocation res) {
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
            if (obj.has("ctm_version")) {
                IModelParser parser = parserVersions.get(obj.get("ctm_version").getAsInt());
                if (parser == null) {
                    throw new IllegalArgumentException("Invalid \"ctm_version\" in model " + res);
                }
                return parser.fromJson(res, obj);
            }
            if (obj.has("ctm")) {
                JsonElement ctm = obj.get("ctm");
                if (ctm.isJsonObject()) {
                    obj = ctm.getAsJsonObject();
                } else if (ctm.isJsonPrimitive() && ctm.getAsString().equals("parent")) {
                    return loadFromFile(new ResourceLocation(obj.get("parent").getAsString()));
                }
            }
            return gson.fromJson(obj, ModelChiselOld.class);
        } else if (json.isJsonNull()) {
            Chisel.debug("Substituting default model json for missing file " + res);
            return gson.fromJson(String.format(DEFAULT_MODEL, res.toString().replace("models/block/", "").concat(".cf")), ModelChiselOld.class);
        } else {
            throw new IllegalArgumentException("Found invalid JSON information \"" + json + "\" loading model " + res);
        }
    }
}
