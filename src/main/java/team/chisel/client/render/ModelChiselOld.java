package team.chisel.client.render;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.ModelProcessingHelper;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import team.chisel.api.render.IChiselFace;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.IModelChisel;
import team.chisel.common.util.json.JsonHelper;

@Deprecated
public class ModelChiselOld implements IModelChisel {
    
    private Variant model;
    private Map<String, Variant> models = Maps.newHashMap();;
    
    private String face;
    private Map<EnumFacing, String> overrides = Maps.newHashMap();
    
    @Getter(onMethod = @__({@Override}))
    @Accessors(fluent = true)
    private boolean ignoreStates;
    
    @Getter
    @Accessors(fluent = true)
    private boolean ambientOcclusion = true;
    
    private transient IChiselFace faceObj;
    private transient Map<EnumFacing, IChiselFace> overridesObj = new EnumMap<>(EnumFacing.class);
    
    private transient List<ResourceLocation> textures = Lists.newArrayList();
    
    private transient byte layers;
    
    @Override
    public Collection<ResourceLocation> getDependencies() {
        List<ResourceLocation> list = Lists.newArrayList(model.getModelLocation());
        list.addAll(models.values().stream().map(v -> v.getModelLocation()).collect(Collectors.toList()));
        return list;
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return ImmutableList.copyOf(textures);
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        Function<ResourceLocation, TextureAtlasSprite> dummyGetter = t -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(TextureMap.LOCATION_MISSING_TEXTURE.toString());
        IBakedModel parent = bake(model, format, dummyGetter);

        layers = 0;
        for (IChiselTexture<?> tex : getChiselTextures()) {
            layers |= 1 << tex.getLayer().ordinal();
        }
        
        JsonObject rawmodel = ModelLoaderChisel.INSTANCE.getJSON(model.getModelLocation()).getAsJsonObject();
        if (rawmodel.has("ambientocclusion")) {
            JsonElement ao = rawmodel.get("ambientocclusion");
            if (ao.isJsonPrimitive() && ao.getAsJsonPrimitive().isBoolean()) {
                this.ambientOcclusion = ao.getAsBoolean();
            }
        }
        
        return new ModelChiselBlockOld(this, parent);
    }
    
    @SneakyThrows
    private IBakedModel bake(Variant variant, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
        IModel imodel = ModelLoaderRegistry.getModel(variant.getModelLocation());
        imodel = ModelProcessingHelper.uvlock(imodel, variant.isUvLock());
        return imodel.bake(variant.getState(), format, getter);
    }
    
    @Override
    public void load() {
        if (faceObj != null) {
            return;
        }
        faceObj = JsonHelper.getOrCreateFace(new ResourceLocation(face));
        for (Entry<EnumFacing, String> e : overrides.entrySet()) {
            overridesObj.put(e.getKey(), JsonHelper.getOrCreateFace(new ResourceLocation(e.getValue())));
        }
        faceObj.getTextureList().forEach(t -> textures.addAll(t.getTextures()));
        overridesObj.values().forEach(f -> f.getTextureList().forEach(t -> textures.addAll(t.getTextures())));
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

    @Override
    public IChiselFace getDefaultFace() {
        return faceObj;
    }
    
    @Override
    public List<IChiselTexture<?>> getChiselTextures() {
        List<IChiselTexture<?>> ret = Lists.newArrayList();
        ret.addAll(getDefaultFace().getTextureList());
        for (IChiselFace face : overridesObj.values()) {
            ret.addAll(face.getTextureList());
        }
        return ret;
    }
    
    @Override
    public IChiselTexture<?> getTexture(String iconName) {
        return null;
    }

    @Override
    public IChiselFace getFace(EnumFacing facing) {
        return overridesObj.getOrDefault(facing, faceObj);
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return ((layers >> layer.ordinal()) & 1) == 1;
    }
}
