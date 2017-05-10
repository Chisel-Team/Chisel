package team.chisel.client.render;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import lombok.*;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

import gnu.trove.set.TLongSet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import team.chisel.Chisel;
import team.chisel.api.render.IModelChisel;
import team.chisel.api.render.RenderContextList;
import team.chisel.client.ChiselExtendedState;
import team.chisel.common.asm.ChiselCoreMethods;
import team.chisel.common.util.ProfileUtil;

@RequiredArgsConstructor
public abstract class AbstractChiselBakedModel implements IPerspectiveAwareModel {

    private static Cache<Pair<Item, Integer>, AbstractChiselBakedModel> itemcache = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.SECONDS).<Pair<Item, Integer>, AbstractChiselBakedModel>build();
    private static Cache<State, AbstractChiselBakedModel> modelcache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).maximumSize(0).<State, AbstractChiselBakedModel>build();

    public static void invalidateCaches()
    {
        itemcache.invalidateAll();
        modelcache.invalidateAll();
    }
    
    @ParametersAreNonnullByDefault
    private class Overrides extends ItemOverrideList {
        
        public Overrides() {
            super(Lists.newArrayList());
        }

        @SuppressWarnings("null")
        @Override
        @SneakyThrows
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            return itemcache.get(Pair.of(stack.getItem(), stack.getItemDamage()), () -> createModel(block.getDefaultState(), model, null));
        }
    }
    
    @Value
    @AllArgsConstructor
    private static class State {
        IBlockState cleanState;
        TLongSet serializedContext;
    }
    
    @Getter
    private final @Nonnull IModelChisel model;
    @Getter
    private final @Nonnull IBakedModel parent;
    private final @Nonnull Overrides overrides = new Overrides();

    protected final ListMultimap<BlockRenderLayer, BakedQuad> genQuads = MultimapBuilder.enumKeys(BlockRenderLayer.class).arrayListValues().build();
    protected final Table<BlockRenderLayer, EnumFacing, List<BakedQuad>> faceQuads = Tables.newCustomTable(Maps.newEnumMap(BlockRenderLayer.class), () -> Maps.newEnumMap(EnumFacing.class));

    @Override
    @SneakyThrows
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {        
        if (ChiselCoreMethods.renderingDamageModel.get()) {
            return parent.getQuads(state, side, rand);
        }
        
        ProfileUtil.start("chisel_models");
        
        AbstractChiselBakedModel baked = this;
        BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();

        if (Chisel.proxy.getClientWorld() != null && state instanceof ChiselExtendedState) {
            ProfileUtil.start("state_creation");
            ChiselExtendedState ext = (ChiselExtendedState) state;
            RenderContextList ctxList = ext.getContextList(ext.getClean(), model);

            TLongSet serialized = ctxList.serialized();
            ProfileUtil.end();

            ProfileUtil.start("model_creation");
            baked = modelcache.get(new State(ext.getClean(), serialized), () -> createModel(state, model, ctxList));
        } else if (state != null)  {
            ProfileUtil.start("model_creation");
            baked = modelcache.get(new State(state, null), () -> createModel(state, model, null));
        }

        ProfileUtil.endAndStart("quad_lookup");
        List<BakedQuad> ret;
        if (side != null && layer != null) {
            ret = baked.faceQuads.get(layer, side);
        } else if (side != null) {
            ret = baked.faceQuads.column(side).values().stream().flatMap(List::stream).collect(Collectors.toList());
        } else if (layer != null) {
            ret = baked.genQuads.get(layer);
        } else {
            ret = Lists.newArrayList(baked.genQuads.values());
        }
        ProfileUtil.end();
        ProfileUtil.end();
        return ret;
    }

    @Override
    public @Nonnull ItemOverrideList getOverrides() {
        return overrides;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return parent.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return parent.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public @Nonnull TextureAtlasSprite getParticleTexture() {
        return this.model.getDefaultFace().getParticle();
    }

    @Override
    public @Nonnull ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    private static @Nonnull TRSRTransformation get(float tx, float ty, float tz, float ax, float ay, float az, float s) {
        return new TRSRTransformation(
            new Vector3f(tx / 16, ty / 16, tz / 16),
            TRSRTransformation.quatFromXYZDegrees(new Vector3f(ax, ay, az)),
            new Vector3f(s, s, s),
            null);
    }
        
    public static final Map<TransformType, TRSRTransformation> TRANSFORMS = ImmutableMap.<TransformType, TRSRTransformation>builder()
            .put(TransformType.GUI,                         get(0, 0, 0, 30, 45, 0, 0.625f))
            .put(TransformType.THIRD_PERSON_RIGHT_HAND,     get(0, 2.5f, 0, 75, 45, 0, 0.375f))
            .put(TransformType.THIRD_PERSON_LEFT_HAND,      get(0, 2.5f, 0, 75, 45, 0, 0.375f))
            .put(TransformType.FIRST_PERSON_RIGHT_HAND,     get(0, 0, 0, 0, 45, 0, 0.4f))
            .put(TransformType.FIRST_PERSON_LEFT_HAND,      get(0, 0, 0, 0, 225, 0, 0.4f))
            .put(TransformType.GROUND,                      get(0, 2, 0, 0, 0, 0, 0.25f))
            .put(TransformType.FIXED,                       get(0, 0, 0, 0, 0, 0, 0.5f))
            .build();
    
    public static final TRSRTransformation DEFAULT_TRANSFORM = get(0, 0, 0, 0, 0, 0, 1);

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(this, TRANSFORMS.getOrDefault(cameraTransformType, DEFAULT_TRANSFORM).getMatrix());
    }
    
    protected static final BlockRenderLayer[] LAYERS = BlockRenderLayer.values();
    
    protected abstract AbstractChiselBakedModel createModel(IBlockState state, @Nonnull IModelChisel model, RenderContextList ctx);

}
