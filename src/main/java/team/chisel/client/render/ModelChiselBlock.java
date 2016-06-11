package team.chisel.client.render;

import gnu.trove.set.TLongSet;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

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
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

import org.apache.commons.lang3.tuple.Pair;

import team.chisel.api.block.ICarvable;
import team.chisel.api.render.IChiselFace;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.RenderContextList;
import team.chisel.common.block.BlockCarvable;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

/**
 * Model for all chisel blocks
 */
public class ModelChiselBlock implements IPerspectiveAwareModel {
	
	private class Overrides extends ItemOverrideList {
		
		public Overrides() {
			super(Lists.newArrayList());
		}

	    @Override
	    public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
	        Block block = ((ItemBlock) stack.getItem()).getBlock();
	        return createModel(block.getDefaultState(), model, null);
	    }
	}

    private List<BakedQuad> face;
    private List<BakedQuad> general;

    private ModelChisel model;
    private Overrides overrides = new Overrides();
        
    private static Cache<Pair<IBlockState, TLongSet>, ModelChiselBlock> modelcache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).maximumSize(500).<Pair<IBlockState, TLongSet>, ModelChiselBlock>build();
    
    public ModelChiselBlock(List<BakedQuad> face, List<BakedQuad> general, ModelChisel model) {
        this.face = face;
        this.general = general;
        this.model = model;
    }

    public ModelChiselBlock(ModelChisel model) {
        this(Collections.emptyList(), Collections.emptyList(), model);
    }

    @Override
    @SneakyThrows
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        ModelChiselBlock baked;
        if (state != null && state.getBlock() instanceof ICarvable && state instanceof IExtendedBlockState) {
            IExtendedBlockState ext = (IExtendedBlockState) state;
            IBlockState clean = ext.getClean();
            RenderContextList ctxList = ext.getValue(BlockCarvable.CTX_LIST);
            if (ctxList == null) {
                return model.getModel(state).getQuads(state, side, rand);
            }
            TLongSet serialized = ctxList.serialize();
            baked = modelcache.get(Pair.of(clean, serialized), () -> createModel(ext, model, ctxList));
        } else {
            baked = this;
        }
        return side == null ? baked.general : FluentIterable.from(baked.face).filter(quad -> quad.getFace() == side).toList();
    }

    @Override
    public ItemOverrideList getOverrides() {
    	return overrides;
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
    public TextureAtlasSprite getParticleTexture() {
        return this.model.getDefaultFace().getParticle();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    private ModelChiselBlock createModel(IBlockState state, ModelChisel model, RenderContextList ctx) {
        List<BakedQuad> faceQuads = Lists.newArrayList();
        List<BakedQuad> generalQuads = Lists.newArrayList();
        for (EnumFacing facing : EnumFacing.VALUES) {
            IChiselFace face = model.getFace(facing);
            if (ctx != null && MinecraftForgeClient.getRenderLayer() != face.getLayer()) {
            //Chisel.debug("Skipping Layer " + MinecraftForgeClient.getRenderLayer() + " for block " + block);
                continue;
            }
            int quadGoal = ctx == null ? 1 : Ordering.natural().max(FluentIterable.from(face.getTextureList()).transform(tex -> tex.getType().getQuadsPerSide()));
            IBakedModel baked = model.getModel(state);
            List<BakedQuad> origFaceQuads = baked.getQuads(state, facing, 0);
            List<BakedQuad> origGeneralQuads = FluentIterable.from(baked.getQuads(state, null, 0)).filter(q -> q.getFace() == facing).toList();
            addAllQuads(origFaceQuads, face, ctx, quadGoal, faceQuads);
            addAllQuads(origGeneralQuads, face, ctx, quadGoal, generalQuads);
        }
        return new ModelChiselBlock(faceQuads, generalQuads, model);
    }

    private void addAllQuads(List<BakedQuad> from, IChiselFace face, @Nullable RenderContextList ctx, int quadGoal, List<BakedQuad> to) {
        for (BakedQuad q : from) {
            for (IChiselTexture<?> tex : face.getTextureList()) {
                to.addAll(tex.transformQuad(q, ctx == null ? null : ctx.getRenderContext(tex.getType()), quadGoal));
            }
        }
    }

    private static TRSRTransformation get(float tx, float ty, float tz, float ax, float ay, float az, float s) {
        return new TRSRTransformation(
            new Vector3f(tx / 16, ty / 16, tz / 16),
            TRSRTransformation.quatFromXYZDegrees(new Vector3f(ax, ay, az)),
            new Vector3f(s, s, s),
            null);
    }
        
    private static final Map<TransformType, TRSRTransformation> TRANSFORMS = ImmutableMap.<TransformType, TRSRTransformation>builder()
            .put(TransformType.GUI,                         get(0, 0, 0, 30, 45, 0, 0.625f))
            .put(TransformType.THIRD_PERSON_RIGHT_HAND,     get(0, 2.5f, 0, 75, 45, 0, 0.375f))
            .put(TransformType.THIRD_PERSON_LEFT_HAND,      get(0, 2.5f, 0, 75, 45, 0, 0.375f))
            .put(TransformType.FIRST_PERSON_RIGHT_HAND,     get(0, 0, 0, 0, 45, 0, 0.4f))
            .put(TransformType.FIRST_PERSON_LEFT_HAND,      get(0, 0, 0, 0, 225, 0, 0.4f))
            .put(TransformType.GROUND,                      get(0, 2, 0, 0, 0, 0, 0.25f))
            .build();
    
    private static final TRSRTransformation DEFAULT_TRANSFORM = get(0, 0, 0, 0, 0, 0, 1);

    @Override
    public Pair<? extends IPerspectiveAwareModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(this, TRANSFORMS.getOrDefault(cameraTransformType, DEFAULT_TRANSFORM).getMatrix());
    }
}
