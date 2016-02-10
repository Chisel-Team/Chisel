package team.chisel.client.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.vecmath.Matrix4f;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import team.chisel.Chisel;
import team.chisel.api.block.ICarvable;
import team.chisel.api.render.IChiselFace;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.RenderContextList;
import team.chisel.client.BlockFaceData;
import team.chisel.client.BlockFaceData.VariationFaceData;
import team.chisel.client.ClientUtil;
import team.chisel.common.block.BlockCarvable;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

/**
 * Model for all chisel blocks
 */
@SuppressWarnings("deprecation")
public class ModelChiselBlock implements ISmartBlockModel, ISmartItemModel, IPerspectiveAwareModel {

    private List<BakedQuad> quads;

    private BlockFaceData.VariationFaceData variationData;

    public ModelChiselBlock(List<BakedQuad> quads, BlockFaceData.VariationFaceData data){
        this.quads = quads;
        this.variationData = data;
    }

    public ModelChiselBlock(){
        this(new ArrayList<BakedQuad>(), null);
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing facing){
        return FluentIterable.from(quads).filter(quad -> quad.getFace() == facing).toList();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return Collections.emptyList(); // TODO this should be a separate list when we implement non-full blocks
    }

    @Override
    public boolean isAmbientOcclusion(){
        return true;
    }

    @Override
    public boolean isGui3d(){
        return true;
    }

    @Override
    public boolean isBuiltInRenderer(){
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        if (this.variationData == null) {
            return Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(TextureMap.LOCATION_MISSING_TEXTURE.toString());
        } else {
            return this.variationData.defaultFace.getParticle();
        }
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms(){
        return ItemCameraTransforms.DEFAULT;
    }

    // TODO implement model caching, returning a new model every time is a HUGE waste of memory and CPU
    
    @Override
    public IBakedModel handleBlockState(IBlockState stateIn){
//        Chisel.debug("Handling blockstate "+stateIn);
        if (stateIn.getBlock() instanceof ICarvable && stateIn instanceof IExtendedBlockState){
            IExtendedBlockState state = (IExtendedBlockState) stateIn;
            ICarvable block = (ICarvable) state.getBlock();
            RenderContextList ctxList = state.getValue(BlockCarvable.CTX_LIST);
            List<BakedQuad> quads = new ArrayList<BakedQuad>();
            VariationFaceData variationData = block.getBlockFaceData().getForMeta(MathHelper.clamp_int(block.getVariationIndex(state), 0, block.getVariations().length));
            for (EnumFacing facing : EnumFacing.VALUES){
                IChiselFace face = variationData.getFaceForSide(facing);
                if (MinecraftForgeClient.getRenderLayer() != face.getLayer()) {
                    Chisel.debug("Skipping Layer " + MinecraftForgeClient.getRenderLayer() + " for block " + state);
                    continue;
                }
                int quadGoal = Ordering.natural().max(FluentIterable.from(face.getTextureList()).transform(tex -> tex.getType().getQuadsPerSide()));
                List<BakedQuad> allQuads = Lists.newArrayList();
                IBakedModel model = ChiselModelRegistry.INSTANCE.getBaseModel();
                allQuads.addAll(model.getFaceQuads(facing));
                allQuads.addAll(FluentIterable.from(model.getGeneralQuads()).filter(q -> q.getFace() == facing).toList());
                for (BakedQuad q : allQuads) {
                    for (IChiselTexture<?> tex : face.getTextureList()) {
                        quads.addAll(tex.transformQuad(q, ctxList.getRenderContext(tex.getType()), quadGoal));
                    }
                }
            }
            return new ModelChiselBlock(quads, variationData);
        }
        else {
            return this;
        }
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
//        Chisel.debug("Handling item model for " + stack);
        BlockCarvable block = (BlockCarvable) ((ItemBlock) stack.getItem()).getBlock();
        VariationFaceData variationData = block.getBlockFaceData().getForMeta(stack.getItemDamage());
        List<BakedQuad> quads = new ArrayList<>();
        for (EnumFacing facing : EnumFacing.VALUES) {
            // quads.add(QuadHelper.makeNormalFaceQuad(facing, varData.getFaceForSide(facing).getParticle()));
            for (BakedQuad q : ChiselModelRegistry.INSTANCE.getBaseModel().getFaceQuads(facing)) {
                for (IChiselTexture<?> tex : variationData.getFaceForSide(facing).getTextureList()) {
                    quads.addAll(tex.transformQuad(q, null, 1));
                }
            }
        }
        return new ModelChiselBlock(quads, variationData);
    }

    @Override
    public VertexFormat getFormat() {
        return Attributes.DEFAULT_BAKED_FORMAT;
    }

    private Pair<IPerspectiveAwareModel, Matrix4f> thirdPersonTransform;
    
    @Override
    public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        if (cameraTransformType == TransformType.THIRD_PERSON) {
            if (thirdPersonTransform == null) {
                thirdPersonTransform = ImmutablePair.of(this, ClientUtil.DEFAULT_BLOCK_THIRD_PERSON_MATRIX);
            }
            return thirdPersonTransform;
        }
        return Pair.of(this, null);
    }
}
