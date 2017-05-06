package team.chisel.client.render;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.ObjectArrays;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.IModelChisel;
import team.chisel.api.render.RenderContextList;
import team.chisel.api.render.TextureInfo;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.ChiselExtendedState;
import team.chisel.client.ClientUtil;
import team.chisel.client.render.texture.MetadataSectionChisel;

@ParametersAreNonnullByDefault
public class ModelChiselBlock extends AbstractChiselBakedModel {
    
    public ModelChiselBlock(IModelChisel model) {
        super(model);
    }

    private static final EnumFacing[] FACINGS = ObjectArrays.concat(EnumFacing.VALUES, (EnumFacing) null);

    @Override
    protected AbstractChiselBakedModel createModel(@Nullable IBlockState state, IModelChisel model, @Nullable RenderContextList ctx) {
        AbstractChiselBakedModel ret = new ModelChiselBlock(model);
        for (BlockRenderLayer layer : LAYERS) {
            for (EnumFacing facing : FACINGS) {
                List<BakedQuad> parentQuads = model.getModel(null).getQuads(state, facing, 0);
                List<BakedQuad> quads;
                if (facing != null) {
                    ret.faceQuads.put(layer, facing, quads = new ArrayList<>());
                } else {
                    quads = ret.genQuads.get(layer);
                }
                for (BakedQuad q : parentQuads) {
                    TextureAtlasSprite sprite = q.getSprite();
                    if (sprite == null) {
                        continue;
                    }

                    MetadataSectionChisel chiselmeta = null;
                    try {
                        chiselmeta = ClientUtil.getMetadata(sprite);
                    } catch (IOException e) {}
                    
                    if (!(state instanceof ChiselExtendedState) || chiselmeta == null) {
                        quads.add(q);
                    } else {
                        IBlockRenderType type = chiselmeta.getType();
                        ChiselExtendedState extstate = (ChiselExtendedState) state;
                        // TODO VERY TEMPORARY
                        IChiselTexture<?> tex = type.makeTexture(new TextureInfo(
                                                    Arrays.stream(ObjectArrays.concat(new ResourceLocation(sprite.getIconName()), chiselmeta.getAdditionalTextures())).map(TextureSpriteCallback::new).toArray(TextureSpriteCallback[]::new), 
                                                    Optional.empty(), 
                                                    chiselmeta.getLayer(), 
                                                    false
                                                ));
                        IBlockRenderContext brc = type.getBlockRenderContext(extstate.getClean(), extstate.getWorld(), extstate.getPos(), tex);
                        quads.addAll(tex.transformQuad(q, brc, type.getQuadsPerSide()));
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public @Nonnull TextureAtlasSprite getParticleTexture() {
        return getModel().getModel(null).getParticleTexture();
    }
}
