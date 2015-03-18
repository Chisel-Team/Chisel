package com.cricketcraft.chisel.client.render.ctm;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.client.render.CTMBlockResources;
import com.cricketcraft.chisel.common.block.BlockCarvable;
import com.cricketcraft.chisel.common.block.subblocks.ICTMSubBlock;
import com.cricketcraft.chisel.common.block.subblocks.ISubBlock;
import com.cricketcraft.chisel.common.util.SubBlockUtil;
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
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

/**
 * Block Model for Connected textures
 * Deals with the Connected texture rendering basicly
 *
 * @author minecreatr
 */
public class ModelCTM implements ISmartBlockModel {


    private List<BakedQuad> quads;

    public static final FaceBakery bakery = new FaceBakery();

    private TextureAtlasSprite particle;


    public ModelCTM(IBlockState state) {
        this.quads = generateQuads(state);
    }

    public List getFaceQuads(EnumFacing face) {
        List<BakedQuad> toReturn = new ArrayList<BakedQuad>();
        for (BakedQuad quad : quads) {
            if (quad.getFace() == face) {
                toReturn.add(quad);
            }
        }
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
        return particle;
    }

    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        Chisel.logger.info("Handling block state for "+state.toString());
        List<BakedQuad> newQuads = generateQuads(state);
        this.quads = newQuads;
        this.particle = SubBlockUtil.getResources(state.getBlock(), (Integer) state.getValue(BlockCarvable.VARIATION)).getDefaultTexture();
        return this;
    }

    private List<BakedQuad> generateQuads(IBlockState state) {
        List<BakedQuad> newQuads = new ArrayList<BakedQuad>();
        int variation = (Integer) state.getValue(BlockCarvable.VARIATION);
        if (state.getBlock() instanceof BlockCarvable) {
            ISubBlock subBlock = ((BlockCarvable) state.getBlock()).getSubBlock(variation);
            if (subBlock instanceof ICTMSubBlock) {
                ICTMSubBlock ctmSubBlock = (ICTMSubBlock) subBlock;
                for (EnumFacing f : EnumFacing.values()) {
                    CTMFaceBakery.instance.makeCtmFace(f, ctmSubBlock.getResources(), CTM.getSubmapIndices((IExtendedBlockState)state,f)).addToList(newQuads);
                }
            }
        }
        return newQuads;
    }





    /**
     * Two vectors to represent the position of a quad
     */
    public static class QuadPos{
        public Vector3f from;
        public Vector3f to;

        public QuadPos(Vector3f from, Vector3f to){
            this.from=from;
            this.to=to;
        }
    }
}
