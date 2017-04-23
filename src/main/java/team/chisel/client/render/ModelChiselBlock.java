package team.chisel.client.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;

import lombok.SneakyThrows;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.TextureInfo;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.ChiselExtendedState;
import team.chisel.client.ClientUtil;
import team.chisel.client.render.texture.MetadataSectionChisel;


public class ModelChiselBlock implements IPerspectiveAwareModel {
    
    @ParametersAreNonnullByDefault
    private class Overrides extends ItemOverrideList {
        
        public Overrides() {
            super(Lists.newArrayList());
        }

        @SuppressWarnings("null")
        @Override
        @SneakyThrows
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
            return model.getModel(null);
        }
    }
    
    private final ModelChisel model;

    public ModelChiselBlock(ModelChisel model) {
        this.model = model;
    }

    @Override
    public @Nonnull List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> parentQuads = model.getModel(null).getQuads(state, side, rand);
        List<BakedQuad> ret = new ArrayList<>();
        for (BakedQuad q : parentQuads) {
            TextureAtlasSprite sprite = this.model.spritecache.get(new ResourceLocation(q.getSprite().getIconName()));
            MetadataSectionChisel chiselmeta = ClientUtil.getResource(sprite).getMetadata(MetadataSectionChisel.SECTION_NAME);
            if (chiselmeta == null) {
                ret.add(q);
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
                IBlockRenderContext ctx = type.getBlockRenderContext(extstate.getClean(), extstate.getWorld(), extstate.getPos(), tex);
                ret.addAll(tex.transformQuad(q, ctx, type.getQuadsPerSide()));
            }
        }
        return ret;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public @Nonnull TextureAtlasSprite getParticleTexture() {
        return model.getModel(null).getParticleTexture();
    }

    @Override
    public @Nonnull ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public @Nonnull ItemOverrideList getOverrides() {
        return new Overrides();
    }

    @SuppressWarnings("deprecation")
    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
        return Pair.of(this, ModelChiselBlockOld.TRANSFORMS.getOrDefault(cameraTransformType, ModelChiselBlockOld.DEFAULT_TRANSFORM).getMatrix());
    }

}
