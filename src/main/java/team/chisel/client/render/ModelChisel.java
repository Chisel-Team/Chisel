package team.chisel.client.render;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.ModelProcessingHelper;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.registry.GameData;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselFace;
import team.chisel.api.render.IChiselTexture;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.util.json.JsonHelper;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;


public class ModelChisel implements IModel {
    
    public static final Multimap<Block, IBlockRenderType> VALID_BLOCKS = HashMultimap.create();

    private static StateMapperBase mapper = new DefaultStateMapper();
    
    private Variant model;
    private Map<String, Variant> models = Maps.newHashMap();;
    
    private String face;
    private Map<EnumFacing, String> overrides = Maps.newHashMap();
    
    // Explanation: I need to modify the blockstate to add our extended properties
    // So, there needs to be some kind of backwards reference to the block(s) this model is for.
    // This is an unfortunate consequence of the extended state system being tied to blocks.
    // A forge PR may be forthcoming, but for now this solution will do
    private String[] blocks = {};
    
    private transient IChiselFace faceObj;
    private transient Map<EnumFacing, IChiselFace> overridesObj = new EnumMap<>(EnumFacing.class);
    private transient IBakedModel modelObj;
    
    private transient Map<String, IBakedModel> modelsObj = Maps.newHashMap();
    
    private transient Map<IBlockState, IBakedModel> stateMap = Maps.newHashMap();
    
    private transient List<ResourceLocation> textures = Lists.newArrayList();
        
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
        if (face.equals("glass.cf")) {
            System.out.println();
        }
        Function<ResourceLocation, TextureAtlasSprite> dummyGetter = t -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(TextureMap.LOCATION_MISSING_TEXTURE.toString());
        modelObj = bake(model, format, dummyGetter);
        for (Entry<String, Variant> e : models.entrySet()) {
            Variant v = e.getValue();
            modelsObj.put(e.getKey(), bake(v, format, dummyGetter));
        }
        return new ModelChiselBlock(this);
    }
    
    @SneakyThrows
    private IBakedModel bake(Variant variant, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
        IModel imodel = ModelLoaderRegistry.getModel(variant.getModelLocation());
        imodel = ModelProcessingHelper.uvlock(imodel, variant.isUvLock());
        return imodel.bake(variant.getState(), format, getter);
    }
    
    void load() {
		if (faceObj != null) {
			return;
		}
		if (face.equals("glass.cf")) {
			System.out.println();
		}
		faceObj = JsonHelper.getOrCreateFace(new ResourceLocation(face));
		for (Entry<EnumFacing, String> e : overrides.entrySet()) {
			overridesObj.put(e.getKey(), JsonHelper.getOrCreateFace(new ResourceLocation(e.getValue())));
        }
        faceObj.getTextureList().forEach(t -> textures.addAll(t.getTextures()));
        overridesObj.values().forEach(f -> f.getTextureList().forEach(t -> textures.addAll(t.getTextures())));

        for (int i = 0; i < blocks.length; i++) {
            Block b = GameData.getBlockRegistry().getObject(new ResourceLocation(blocks[i]));
            if (b != null) {
                b.blockState = new ExtendedBlockState(b, b.blockState.getProperties().toArray(new IProperty[0]), new IUnlistedProperty[] { BlockCarvable.CTX_LIST });
                b.defaultBlockState = b.blockState.getBaseState();
                List<IChiselTexture<?>> textures = Lists.newArrayList(faceObj.getTextureList());
                for (IChiselFace f : overridesObj.values()) {
                    textures.addAll(f.getTextureList());
                }
                VALID_BLOCKS.putAll(b, FluentIterable.from(textures).transform(IChiselTexture::getType));
            }
        }
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

    public IChiselFace getDefaultFace() {
        return faceObj;
    }

    public IChiselFace getFace(EnumFacing facing) {
        return overridesObj.getOrDefault(facing, faceObj);
    }

    public IBakedModel getModel(IBlockState state) {
        if (state instanceof IExtendedBlockState) {
            state = ((IExtendedBlockState)state).getClean();
        }
        String stateStr = mapper.getPropertyString(state.getProperties());
        stateStr = stateStr.substring(stateStr.indexOf(",") + 1, stateStr.length());
        
        final String capture = stateStr;
        if (modelsObj.containsKey(stateStr)) {
            stateMap.computeIfAbsent(state, s -> modelsObj.get(capture));
        }
        
        return stateMap.getOrDefault(state, modelObj);
    }
}
