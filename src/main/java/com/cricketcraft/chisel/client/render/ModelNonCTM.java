package com.cricketcraft.chisel.client.render;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.client.render.ctm.ModelCTM;
import com.cricketcraft.chisel.common.block.BlockCarvable;
import com.cricketcraft.chisel.common.block.ItemChiselBlock;
import com.cricketcraft.chisel.common.util.SubBlockUtil;
import com.cricketcraft.chisel.common.variation.Variation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

/**
 * Model for regular non ctm blocks
 *
 * @author minecreatr
 */
public class ModelNonCTM implements ISmartBlockModel, ISmartItemModel{

    private List<BakedQuad> quads;

    private static final FaceBakery bakery = new FaceBakery();

    public static final ModelCTM.QuadPos pos = new ModelCTM.QuadPos(new Vector3f(0, 0, 0), new Vector3f(16, 16, 16));

    private TextureAtlasSprite texture;

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        IBlockResources r = ((BlockCarvable)state.getBlock()).getSubBlock((Variation)state.getValue(BlockCarvable.VARIATION)).getResources();
        this.quads=generateQuads(r);
        return this;
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack){
        if (stack.getItem() instanceof ItemChiselBlock){
            ItemChiselBlock itemBlock = (ItemChiselBlock)stack.getItem();
            if (itemBlock.getBlock() instanceof  BlockCarvable){
                BlockCarvable block = (BlockCarvable)itemBlock.getBlock();
                Variation[] vars = block.getType().getVariants();
                this.quads=generateQuads(block.allSubBlocks()[stack.getMetadata()].getResources());
            }
        }
        else {
            Chisel.logger.info("Not Chisel Block Item?");
        }
        return this;
    }

    private List<BakedQuad> generateQuads(IBlockResources r){
        List<BakedQuad> toReturn = new ArrayList<BakedQuad>();

        for (EnumFacing f : EnumFacing.values()){
            toReturn.add(makeQuad(f, r.getDefaultTexture()));
        }
        return toReturn;
    }

    private static BakedQuad makeQuad(EnumFacing f, TextureAtlasSprite sprite){
        return bakery.makeBakedQuad(pos.from, pos.to, new BlockPartFace(f, -1, sprite.getIconName(), new BlockFaceUV(new float[]{0, 0, 16, 16}, 0)),
                sprite, f, ModelRotation.X0_Y0, new BlockPartRotation(new Vector3f(1, 0, 0), f.getAxis(), 0, false), false, false);
    }

    public List getFaceQuads(EnumFacing face) {
        List<BakedQuad> toReturn = new ArrayList<BakedQuad>();
        for (BakedQuad quad : quads) {
            if (quad==null){
                continue;
            }
            if (quad.getFace() == face) {
                toReturn.add(quad);
            }
        }
        //throw new RuntimeException("Giving side quad");
        return toReturn;
    }

    public List getGeneralQuads() {
        return this.quads;
    }

    public boolean isAmbientOcclusion() {
        return true;
    }

    public boolean isGui3d() {
        return true;
    }

    public boolean isBuiltInRenderer() {
        return false;
    }

    public TextureAtlasSprite getTexture() {
        return texture;
    }

    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }
}
