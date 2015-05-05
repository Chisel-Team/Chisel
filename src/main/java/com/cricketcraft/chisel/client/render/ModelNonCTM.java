package com.cricketcraft.chisel.client.render;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.client.render.ctm.ModelCTM;
import com.cricketcraft.chisel.common.block.BlockCarvable;
import com.cricketcraft.chisel.common.block.ItemChiselBlock;
import com.cricketcraft.chisel.common.util.SubBlockUtil;
import com.cricketcraft.chisel.common.variation.PropertyVariation;
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
        PropertyVariation VARIATION = ((BlockCarvable)state.getBlock()).getType().getPropertyVariation();
        IBlockResources r = ((BlockCarvable)state.getBlock()).getSubBlock((Variation)state.getValue(VARIATION)).getResources();
        texture=r.getDefaultTexture();
        this.quads=generateQuads((BlockResources)r);
        return this;
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack){
        if (stack.getItem() instanceof ItemChiselBlock){
            ItemChiselBlock itemBlock = (ItemChiselBlock)stack.getItem();
            if (itemBlock.getBlock() instanceof  BlockCarvable){
                BlockCarvable block = (BlockCarvable)itemBlock.getBlock();
                Variation[] vars = block.getType().getVariants();
                this.quads=generateQuads((BlockResources)block.allSubBlocks()[stack.getMetadata()].getResources());
            }
        }
        else {
            Chisel.logger.info("Not Chisel Block Item?");
        }
        return this;
    }

    private List<BakedQuad> generateQuads(BlockResources r){
        List<BakedQuad> toReturn = new ArrayList<BakedQuad>();

        for (EnumFacing f : EnumFacing.values()){
            TextureAtlasSprite t = r.getDefaultTexture();
            if (f==EnumFacing.UP){
                if (r.top!=null) {
                    t = r.top;
                }
            }
            else if (f==EnumFacing.DOWN){
                if (r.bottom!=null) {
                    t = r.bottom;
                }
            }
            else if (r.side!=null){
                t=r.side;
            }
            toReturn.add(makeQuad(f, t));
        }
        return toReturn;
    }

    public static BakedQuad makeQuad(EnumFacing f, TextureAtlasSprite sprite){
        //Chisel.logger.info("Making "+sprite.getIconName()+" for facing "+f.getName());
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
