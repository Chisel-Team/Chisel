package team.chisel.client.render;

import java.util.Collections;
import java.util.Deque;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import lombok.SneakyThrows;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import team.chisel.api.render.IModelChisel;
import team.chisel.api.render.IModelParser;

public class ModelParserV1 implements IModelParser {
    
    private static final Deque<ResourceLocation> _loadingModels = ReflectionHelper.getPrivateValue(ModelLoaderRegistry.class, null, "loadingModels");
    private static final Gson GSON = new Gson();
    
    @Override
    @Nonnull
    @SneakyThrows
    public IModelChisel fromJson(ResourceLocation res, JsonObject json) {
        ModelBlock modelinfo = ModelBlock.deserialize(json.toString());
        
        // Hack around circularity detection to load vanilla model
        ResourceLocation prev = _loadingModels.removeLast();
        IModel vanillamodel = ModelLoaderRegistry.getModel(new ResourceLocation(res.getResourceDomain(), res.getResourcePath().replace("models/", "")));
        _loadingModels.addLast(prev);

        Map<String, String[]> faces = GSON.fromJson(json.getAsJsonObject("faces"), new TypeToken<Map<String, String[]>>(){}.getType());
        if (faces == null) {
            faces = Collections.emptyMap();
        }
        return new ModelChisel(modelinfo, vanillamodel, faces);
    }
}
