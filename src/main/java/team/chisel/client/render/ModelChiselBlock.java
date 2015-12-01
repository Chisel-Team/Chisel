package team.chisel.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import team.chisel.api.block.ClientVariationData;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselTexture;
import team.chisel.api.render.RenderContextList;
import team.chisel.common.block.BlockCarvable;

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


    public List<BakedQuad> getFaceQuads(EnumFacing facing){
        List<BakedQuad> toReturn = new ArrayList<BakedQuad>();
        for (BakedQuad quad : this.quads){
            toReturn.add(quad);
        }
        return toReturn;
    }

    public List<BakedQuad> getGeneralQuads(){
        return this.quads;
    }

    public boolean isAmbientOcclusion(){
        return true;
    }

    public boolean isGui3d(){
        return true;
    }

    public boolean isBuiltInRenderer(){
        return false;
    }

    public TextureAtlasSprite getParticleTexture(){
        if (this.variationData == null){
            return null;
        }
        else {
            return this.variationData.defaultTexture
        }
    }

    public ItemCameraTransforms getItemCameraTransforms(){
        return ItemCameraTransforms.DEFAULT;
    }

    public IBakedModel handleBlockState(IBlockState stateIn){
        if (stateIn.getBlock() instanceof BlockCarvable && stateIn instanceof IExtendedBlockState){
            IExtendedBlockState state = (IExtendedBlockState) stateIn;
            BlockCarvable block = (BlockCarvable) state.getBlock();
            RenderContextList ctxList = state.getValue(BlockCarvable.CTX_LIST);
            List<BakedQuad> quadList = new ArrayList<BakedQuad>();
            ClientVariationData varData = (ClientVariationData) block.getBlockData().variations[state.getValue(block.metaProperty)];
            for (EnumFacing facing : EnumFacing.VALUES){
                IChiselTexture<? extends IBlockRenderContext> tex = varData.getTextureForSide(facing);
                for (IBlockRenderType<? extends IBlockRenderContext> type : tex.getBlockRenderTypes()){
                    quadList.addAll(tex.getSideQuads(facing, ctxList.getRenderContext()))
                }
                quadList.addAll(tex.getSideQuads(facing, ctxList.getRenderContext()))
            }
        }
        else {
            return this;
        }
    }
}
