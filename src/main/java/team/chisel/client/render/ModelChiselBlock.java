package team.chisel.client.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import team.chisel.Chisel;
import team.chisel.api.render.IChiselFace;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.RenderContextList;
import team.chisel.client.BlockFaceData;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.block.ItemChiselBlock;

import com.google.common.collect.FluentIterable;

/**
 * Model for all chisel blocks
 */
@SuppressWarnings("deprecation")
public class ModelChiselBlock implements ISmartBlockModel, ISmartItemModel {

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
    public TextureAtlasSprite getParticleTexture(){
        if (this.variationData == null){
            return null;
        }
        else {
            return this.variationData.defaultFace.getParticle();
        }
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms(){
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public IBakedModel handleBlockState(IBlockState stateIn){
        Chisel.debug("Handling blockstate "+stateIn);
        if (stateIn.getBlock() instanceof BlockCarvable && stateIn instanceof IExtendedBlockState){
            IExtendedBlockState state = (IExtendedBlockState) stateIn;
            BlockCarvable block = (BlockCarvable) state.getBlock();
            RenderContextList ctxList = state.getValue(BlockCarvable.CTX_LIST);
            quads = new ArrayList<BakedQuad>();
            variationData = block.getBlockFaceData().getForMeta(MathHelper.clamp_int(state.getValue(block.metaProp),
                    0, block.getBlockData().variations.length));
            for (EnumFacing facing : EnumFacing.VALUES){
                IChiselFace face = variationData.getFaceForSide(facing);
                if (MinecraftForgeClient.getRenderLayer() != face.getLayer()){
                    Chisel.debug("Skipping Layer "+ MinecraftForgeClient.getRenderLayer()+" for block "+state);
                    continue;
                }
                int singleGreatestQuadGoal = 1;
                for (IChiselTexture tex : face.getTextureList()){
                    if (tex.getBlockRenderType().getQuadsPerSide() > singleGreatestQuadGoal){
                        singleGreatestQuadGoal = tex.getBlockRenderType().getQuadsPerSide();
                    }
                }
                for (IChiselTexture tex : face.getTextureList()){
                    quads.addAll(tex.getSideQuads(facing, ctxList.getRenderContext(tex.getBlockRenderType()), singleGreatestQuadGoal));
                }
            }
            return this;
        }
        else {
            return this;
        }
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        //Chisel.debug("Handling item model for "+stack);
        if (stack.getItem() instanceof ItemChiselBlock){
            BlockCarvable block = (BlockCarvable) ((ItemChiselBlock)stack.getItem()).getBlock();
            variationData = block.getBlockFaceData().getForMeta(stack.getItemDamage());
            quads = new ArrayList<BakedQuad>();
            for (EnumFacing facing : EnumFacing.VALUES){
                //quads.add(QuadHelper.makeNormalFaceQuad(facing, varData.getFaceForSide(facing).getParticle()));
                for (IChiselTexture tex : variationData.getFaceForSide(facing).getTextureList()){
                    quads.addAll(tex.getSideQuads(facing, null, 1));
                }
            }
            return new ModelChiselBlock(quads, variationData);
        }
        else {
            return this;
        }

    }
}
