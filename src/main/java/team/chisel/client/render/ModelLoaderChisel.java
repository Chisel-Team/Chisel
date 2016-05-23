package team.chisel.client.render;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.gson.Gson;


public class ModelLoaderChisel implements ICustomModelLoader {

    private static final String DEFAULT_MODEL = "{\"model\": { \"model\": \"cube\" }, \"face\":\"%s\"}";
    
    private final Gson gson = ModelBlockDefinition.GSON;
    
    private IResourceManager manager;
    private Map<ResourceLocation, ModelChisel> loadedModels = Maps.newHashMap();
    
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.manager = resourceManager;
        loadedModels.clear();
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equals("ctm");
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

    private ModelChisel loadFromFile(ResourceLocation res) {
        String domain = res.getResourcePath();
        String path = "models/block/" + domain.substring(domain.indexOf(":") + 1, domain.length());
        domain = domain.substring(0, domain.indexOf(":"));
        domain = domain.substring(domain.lastIndexOf("/") + 1, domain.length());
        res = new ResourceLocation(domain, path);
        try {
            IResource resource = manager.getResource(res);
            return gson.fromJson(new InputStreamReader(resource.getInputStream()), ModelChisel.class);
        } catch (FileNotFoundException f) {
            return gson.fromJson(String.format(DEFAULT_MODEL, res.toString().replace("models/block/", "").replace(".json", ".cf")), ModelChisel.class);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
