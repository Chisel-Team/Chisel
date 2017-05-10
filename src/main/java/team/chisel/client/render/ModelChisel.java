package team.chisel.client.render;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.google.common.base.Function;
import com.google.common.collect.ObjectArrays;

import lombok.Setter;
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
import net.minecraftforge.common.property.IExtendedBlockState;
import team.chisel.api.render.IChiselFace;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.IModelChisel;
import team.chisel.api.render.TextureInfo;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.ClientUtil;
import team.chisel.client.render.texture.MetadataSectionChisel;

public class ModelChisel implements IModelChisel {

    private final ModelBlock modelinfo;
    private final IModel parentmodel;

    private final Map<String, String[]> textureLists;
    
    private Collection<ResourceLocation> textureDependencies;
    
    private transient byte layers;

    private Map<String, IChiselTexture<?>> textures = new HashMap<>();
    private boolean hasVanillaTextures;
    
    public ModelChisel(ModelBlock modelinfo, IModel parent, Map<String, String[]> textureLists) {
        this.modelinfo = modelinfo;
        this.parentmodel = parent;
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
        textureDependencies = new HashSet<>();
        Map<ResourceLocation, String[]> resolvedTextureLists = new HashMap<>();
        if (modelinfo != null) {
            for (Entry<String, String[]> e : textureLists.entrySet()) {
                if (modelinfo.isTexturePresent(e.getKey())) {
                    resolvedTextureLists.put(new ResourceLocation(modelinfo.textures.get(e.getKey())), e.getValue());
                } else {
                    resolvedTextureLists.put(new ResourceLocation(e.getKey()), e.getValue());
                }
            }
        }
        for (ResourceLocation rl : parentmodel.getTextures()) {
            if (resolvedTextureLists.containsKey(rl)) {
                for (String s : resolvedTextureLists.get(rl)) {
                    textureDependencies.add(new ResourceLocation(s));
                }
            } else {
                textureDependencies.add(rl);
            }
        }
        return getTextures();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        IBakedModel parent = parentmodel.bake(state, format, rl -> {
            TextureAtlasSprite sprite = bakedTextureGetter.apply(rl);
            MetadataSectionChisel chiselmeta = null;
            try {
                chiselmeta = ClientUtil.getMetadata(sprite);
            } catch (IOException e) {}
            if (chiselmeta != null) {
                final MetadataSectionChisel meta = chiselmeta;
                textures.computeIfAbsent(sprite.getIconName(), s -> {
                    // TODO VERY TEMPORARY
                    IChiselTexture<?> tex = meta.getType().makeTexture(new TextureInfo(
                            Arrays.stream(ObjectArrays.concat(new ResourceLocation(sprite.getIconName()), meta.getAdditionalTextures())).map(TextureSpriteCallback::new).toArray(TextureSpriteCallback[]::new), 
                            Optional.of(meta.getExtraData()), 
                            meta.getLayer(),
                            false
                    ));
                    layers |= 1 << tex.getLayer().ordinal();
                    return tex;
                });
            } else {
                hasVanillaTextures = true;
            }
            return sprite;
        });
        return new ModelChiselBlock(this, parent);
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
    public IChiselTexture<?> getTexture(String iconName) {
        return textures.get(iconName);
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
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return (hasVanillaTextures && state.getBlock().getBlockLayer() == layer) || ((layers >> layer.ordinal()) & 1) == 1;
    }

    @Override
    public boolean ignoreStates() {
        return false;
    }
}
