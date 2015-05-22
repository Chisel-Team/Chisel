package com.cricketcraft.chisel.client.render;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.client.render.ctm.ModelCTM;
import com.cricketcraft.chisel.common.block.BlockCarvable;
import com.cricketcraft.chisel.common.block.subblocks.ISubBlock;
import com.cricketcraft.chisel.common.util.SubBlockUtil;
import com.cricketcraft.chisel.common.variation.PropertyVariation;
import com.cricketcraft.chisel.common.variation.Variation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.client.resources.model.WeightedBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
* Model for coordinate variations
*
* @author minecreatr
*/
public class ModelCoordinateVariations extends WeightedBakedModel{

    private String variation;

    private ModelCoordinateVariations(List l, String v){
        super(l);
        this.variation = v;
    }

    public static ModelCoordinateVariations newModel(String v){
        Builder b = new Builder();
        b.add(new SmartSubtype(0), 1);
        return new ModelCoordinateVariations(b.build().models, v);
    }

    @Override
    public IBakedModel getAlternativeModel(long seed){
        return new SmartSubtype(seed);
    }

    protected static class SmartSubtype implements ISmartBlockModel{


        private static final FaceBakery bakery = new FaceBakery();

        public static final ModelCTM.QuadPos quadPos = new ModelCTM.QuadPos(new Vector3f(0, 0, 0), new Vector3f(16, 16, 16));

        private long seed;

        private List<BakedQuad> quads;

        private TextureAtlasSprite particle;

        public SmartSubtype(long seed){
            this.seed = seed;
        }

        @Override
        public IBakedModel handleBlockState(IBlockState state) {
            PropertyVariation VARIATION = null;
            if (state.getBlock() instanceof BlockCarvable){
                VARIATION=((BlockCarvable) state.getBlock()).VARIATION;
            }
            IBlockResources r = SubBlockUtil.getResources(state.getBlock(), (Variation) state.getValue(VARIATION));
            List<BakedQuad> newQuads = generateQuads(state, r);
            this.quads = newQuads;
            this.particle = r.getDefaultTexture();
            return this;
        }

        private List<BakedQuad> generateQuads(IBlockState state, IBlockResources r) {
            int bound = 4;
            int wid = 2;
            if (r.getType()==IBlockResources.V9||r.getType()==IBlockResources.R9){
                bound = 9;
                wid = 3;
            }
            else if (r.getType()==IBlockResources.R16){
                bound = 16;
                wid = 4;
            }
            Random random = new Random(seed);
            int num = random.nextInt(bound) + 1;
            float interval = 16/wid;
            int unitsAcross = num%wid;
            int unitsDown = num/wid;
            if (unitsAcross==0){
                unitsAcross++;
            }
            if (unitsDown==0){
                unitsDown++;
            }
            float maxU = unitsAcross*interval;
            float maxV = unitsDown*interval;
            //Chisel.logger.info("maxU: "+maxU+" maxV: "+maxV);
            List<BakedQuad> toReturn = new ArrayList<BakedQuad>();
            for (EnumFacing f : EnumFacing.values()){
                toReturn.add(makeQuad(f, r.getDefaultTexture(), new float[]{maxU-interval, maxV-interval, maxU, maxV}));
            }
            return toReturn;
        }

        public static BakedQuad makeQuad(EnumFacing f, TextureAtlasSprite sprite, float[] uvs){
            return bakery.makeBakedQuad(quadPos.from, quadPos.to, new BlockPartFace(f, -1, sprite.getIconName(), new BlockFaceUV(uvs, 0)),
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
    }

}
