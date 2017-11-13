//package team.chisel.client.render;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Deque;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//import javax.annotation.Nullable;
//import javax.annotation.ParametersAreNonnullByDefault;
//import javax.vecmath.Matrix4f;
//import javax.vecmath.Quat4f;
//import javax.vecmath.Vector3f;
//
//import org.apache.commons.lang3.tuple.Pair;
//
//import com.google.common.base.Function;
//import com.google.common.collect.Lists;
//
//import lombok.RequiredArgsConstructor;
//import lombok.val;
//import lombok.experimental.Delegate;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.client.renderer.block.model.BakedQuad;
//import net.minecraft.client.renderer.block.model.IBakedModel;
//import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
//import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
//import net.minecraft.client.renderer.block.model.ItemOverrideList;
//import net.minecraft.client.renderer.block.model.ModelResourceLocation;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.client.renderer.vertex.VertexFormat;
//import net.minecraft.client.resources.IResourceManager;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.world.World;
//import net.minecraftforge.client.model.ICustomModelLoader;
//import net.minecraftforge.client.model.IModel;
//import net.minecraftforge.client.model.IPerspectiveAwareModel;
//import net.minecraftforge.client.model.ModelLoader;
//import net.minecraftforge.client.model.ModelLoaderRegistry;
//import net.minecraftforge.common.model.IModelState;
//import net.minecraftforge.common.model.TRSRTransformation;
//import net.minecraftforge.fml.relauncher.ReflectionHelper;
//import team.chisel.common.util.NBTUtil;
//import team.chisel.ctm.client.model.ModelUtil;
//
//@ParametersAreNonnullByDefault
//@RequiredArgsConstructor
//public class ModelChiselItem implements IPerspectiveAwareModel {
//
//    private class Overrides extends ItemOverrideList {
//
//        public Overrides() {
//            super(Lists.newArrayList());
//        }
//
//        @Override
//        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
//            ItemStack target = NBTUtil.getChiselTarget(stack);
//            if (target == null) {
//                return originalModel;
//            }
//            Optional<ModelResourceLocation> mrl = Optional.ofNullable(ModelUtil.getMesh(target));
//            IModel rawModel = mrl.map(m -> ModelLoaderRegistry.getModelOrLogError(new ResourceLocation(m.getResourceDomain(), "item/" + m.getResourcePath()), "")).orElse(ModelLoaderRegistry.getMissingModel());
//            IBakedModel targetModel = rawModel.bake(rawModel.getDefaultState(), DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
//            return new ModelChiselItem(ModelChiselItem.this.parent, rawModel, targetModel.getOverrides().handleItemState(targetModel, target, world, entity), target);
//        }
//    }
//
//    private interface Exclusions {
//
//        List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand);
//
//        ItemOverrideList getOverrides();
//    }
//    
//    private static final TRSRTransformation targetTransform = new TRSRTransformation(
//            new Vector3f(), 
//            new Quat4f(), 
//            new Vector3f(0.25f, 0.25f, 0.25f), 
//            new Quat4f()
//        );
//
//    @Delegate(excludes = Exclusions.class)
//    private final IBakedModel parent;
//
//    private final @Nullable IModel targetRaw;
//    private final @Nullable IBakedModel target;
//    
//    private final @Nullable ItemStack targetStack;
//
//    // private final numMap<EnumFacing, List<BakedQuad>> faceQuads = new EnumMap<>(EnumFacing.class);
//    // private final List<BakedQuad> genQuads = new ArrayList<>();
//
//    @Override
//    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
//        List<BakedQuad> list = new ArrayList<>();
//        list.addAll(parent.getQuads(state, side, rand));
//        if (target != null) {
//            list.addAll(target.getQuads(state, side, rand));
//        }
//        return list;
//    }
//
//    @Override
//    public ItemOverrideList getOverrides() {
//        return new Overrides();
//    }
//
//    public enum Loader implements ICustomModelLoader {
//
//        INSTANCE;
//
//        private Set<ResourceLocation> locations = new HashSet<>();
//
//        private boolean loading = false;
//
//        public void addModel(Item item) {
//            ModelResourceLocation mrl = new ModelResourceLocation(item.getRegistryName(), "inventory");
//            locations.add(new ResourceLocation(mrl.getResourceDomain(), "models/item/" + mrl.getResourcePath()));
//            ModelLoader.setCustomModelResourceLocation(item, 0, mrl);
//        }
//
//        @Override
//        public void onResourceManagerReload(IResourceManager resourceManager) {}
//
//        @Override
//        public boolean accepts(@Nullable ResourceLocation modelLocation) {
//            return !loading && locations.contains(modelLocation);
//        }
//
//        @SuppressWarnings("unchecked")
//        @Override
//        public IModel loadModel(@Nullable ResourceLocation modelLocation) throws Exception {
//            loading = true;
//            Field _loadingModels = ReflectionHelper.findField(ModelLoaderRegistry.class, "loadingModels");
//            ResourceLocation popped = ((Deque<ResourceLocation>) _loadingModels.get(null)).removeLast();
//            try {
//                IModel parent = ModelLoaderRegistry.getModel(new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath().replace("models/", "")));
//                return new IModel() {
//
//                    @Override
//                    public Collection<ResourceLocation> getTextures() {
//                        return parent.getTextures();
//                    }
//
//                    @Override
//                    public Collection<ResourceLocation> getDependencies() {
//                        return parent.getDependencies();
//                    }
//
//                    @Override
//                    public IModelState getDefaultState() {
//                        return parent.getDefaultState();
//                    }
//
//                    @Override
//                    public IBakedModel bake(@Nullable IModelState state, @Nullable VertexFormat format, @Nullable Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
//                        return new ModelChiselItem(parent.bake(state, format, bakedTextureGetter), null, null, null);
//                    }
//                };
//            } finally {
//                ((Deque<ResourceLocation>) _loadingModels.get(null)).addLast(popped);
//                loading = false;
//            }
//        }
//    }
//
//    @Override
//    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
//        if (target != null && cameraTransformType == TransformType.GUI) {
//            ItemCameraTransforms ict = target.getItemCameraTransforms();
//            if (target instanceof IPerspectiveAwareModel) {
//                val targetPerspective = ((IPerspectiveAwareModel)target).handlePerspective(cameraTransformType);
//                IBakedModel newTarget = targetRaw.bake(new TRSRTransformation(targetPerspective.getRight()).compose(new TRSRTransformation(ict.gui)), DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
//                newTarget = newTarget instanceof IPerspectiveAwareModel ? ((IPerspectiveAwareModel)newTarget).handlePerspective(cameraTransformType).getLeft() : newTarget;
//                newTarget = newTarget.getOverrides().handleItemState(newTarget, targetStack, null, null);
//                return Pair.of(new ModelChiselItem(parent, targetRaw, newTarget, targetStack), null);
//            }
//            return Pair.of(this, null);
//        }
//        if (parent instanceof IPerspectiveAwareModel) {
//            val parentPerspective = ((IPerspectiveAwareModel) parent).handlePerspective(cameraTransformType);
//            return Pair.of(new ModelChiselItem(parentPerspective.getLeft(), null, null, null), parentPerspective.getRight());
//        }
//        return Pair.of(this, null);
//    }
//}
