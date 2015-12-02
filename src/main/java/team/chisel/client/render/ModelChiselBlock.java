package team.chisel.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import team.chisel.api.block.ClientVariationData;
import team.chisel.api.render.ChiselFace;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.RenderContextList;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.block.ItemChiselBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for all chisel blocks
 */
public class ModelChiselBlock implements ISmartBlockModel, ISmartItemModel {

    private List<BakedQuad> quads;

    private ClientVariationData variationData;

    public ModelChiselBlock(List<BakedQuad> quads, ClientVariationData data){
        this.quads = quads;
        this.variationData = data;
    }

    public ModelChiselBlock(){
        this(new ArrayList<BakedQuad>(), null);
    }


    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing facing){
        List<BakedQuad> toReturn = new ArrayList<BakedQuad>();
        for (BakedQuad quad : this.quads){
            toReturn.add(quad);
        }
        return toReturn;
    }

    @Override
    public List<BakedQuad> getGeneralQuads(){
        return this.quads;
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
        if (stateIn.getBlock() instanceof BlockCarvable && stateIn instanceof IExtendedBlockState){
            IExtendedBlockState state = (IExtendedBlockState) stateIn;
            BlockCarvable block = (BlockCarvable) state.getBlock();
            RenderContextList ctxList = state.getValue(BlockCarvable.CTX_LIST);
            List<BakedQuad> quadList = new ArrayList<BakedQuad>();
            ClientVariationData varData = (ClientVariationData) block.getBlockData().variations[MathHelper.clamp_int(state.getValue(block.metaProperty),
                    0, block.getBlockData().variations.length)];
            for (EnumFacing facing : EnumFacing.VALUES){
                ChiselFace face = varData.getFaceForSide(facing);
                for (IChiselTexture tex : face.getTextureList()){
                    quadList.addAll(tex.getSideQuads(facing, ctxList.getRenderContext(tex.getBlockRenderType())));
                }
            }
            return new ModelChiselBlock(quadList, varData);
        }
        else {
            return this;
        }
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        if (stack.getItem() instanceof ItemChiselBlock){
            BlockCarvable block = (BlockCarvable) ((ItemChiselBlock)stack.getItem()).getBlock();
            ClientVariationData varData = (ClientVariationData) block.getBlockData().getVariation(stack.getItemDamage());
            List<BakedQuad> quads = new ArrayList<BakedQuad>();
            for (EnumFacing facing : EnumFacing.VALUES){
                quads.add(QuadHelper.makeNormalFaceQuad(facing, varData.getFaceForSide(facing).getParticle()));
            }
            return new ModelChiselBlock(quads, varData);
        }
        else {
            return this;
        }

    }
}
