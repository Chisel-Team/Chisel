package team.chisel.client.render;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import team.chisel.api.render.IChiselFace;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.IModelChisel;
import team.chisel.client.ClientUtil;
import team.chisel.client.render.texture.MetadataSectionChisel;

public class ModelChisel implements IModelChisel {

    private final ModelBlock modelinfo;
    private final IModel parent;
    private IBakedModel bakedparent;
    private final Map<String, String[]> textureLists;
    
    private Collection<ResourceLocation> textureDependencies;
    
    private transient byte layers;

    private Multimap<ResourceLocation, IChiselTexture<?>> textures = HashMultimap.create();
    
    public BiMap<ResourceLocation, TextureAtlasSprite> spritecache;
    
    public ModelChisel(ModelBlock modelinfo, IModel parent, Map<String, String[]> textureLists) {
        this.modelinfo = modelinfo;
        this.parent = parent;
        this.textureLists = textureLists;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptySet();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        if (textureDependencies != null) {
            return textureDependencies;
        }
        Set<ResourceLocation> textures = new HashSet<>();
        Map<ResourceLocation, String[]> resolvedTextureLists = new HashMap<>();
        for (Entry<String, String[]> e : textureLists.entrySet()) {
            if (modelinfo.isTexturePresent(e.getKey())) {
                resolvedTextureLists.put(new ResourceLocation(modelinfo.textures.get(e.getKey())), e.getValue());
            } else {
                resolvedTextureLists.put(new ResourceLocation(e.getKey()), e.getValue());
            }
        }
        for (ResourceLocation rl : parent.getTextures()) {
            if (resolvedTextureLists.containsKey(rl)) {
                for (String s : resolvedTextureLists.get(rl)) {
                    textures.add(new ResourceLocation(s));
                }
            } else {
                textures.add(rl);
            }
        }
        textureDependencies = new HashSet<>();
        for (ResourceLocation rl : textures) {
            MetadataSectionChisel meta = ClientUtil.getMetadata(ClientUtil.spriteToAbsolute(rl));
            textureDependencies.add(rl);
            if (meta != null) {
                textureDependencies.addAll(Arrays.asList(meta.getAdditionalTextures()));
            }
        }
        return getTextures();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        spritecache = HashBiMap.create();
        bakedparent = parent.bake(state, format, rl -> {
            TextureAtlasSprite sprite = bakedTextureGetter.apply(rl);            
            spritecache.put(rl, sprite);
            return sprite;
        });
        
        return new ModelChiselBlock(this);
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

    @Override
    public void load() {}

    @Override
    public Collection<IChiselTexture<?>> getChiselTextures() {
        return textures.values();
    }

    @Override
    public IBakedModel getModel(IBlockState state) {
        return this.bakedparent;
    }

    @Override
    public IChiselFace getFace(EnumFacing facing) {
        return null;
    }

    @Override
    public IChiselFace getDefaultFace() {
        return null;
    }
    
    @Override
    public TextureAtlasSprite getSprite(ResourceLocation resourceLocation) {
        return spritecache.get(resourceLocation);
    }

    public boolean canRenderInLayer(BlockRenderLayer layer) {
        return ((layers >> layer.ordinal()) & 1) == 1;
    }

    @Override
    public boolean ignoreStates() {
        return false;
    }
}
