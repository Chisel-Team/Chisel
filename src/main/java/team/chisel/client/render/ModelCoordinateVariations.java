package team.chisel.client.render;

import team.chisel.client.render.ctm.ModelCTM;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.util.SubBlockUtil;
import team.chisel.common.variation.PropertyVariation;
import team.chisel.common.variation.Variation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.client.resources.model.WeightedBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Model for coordinate variations
 *
 * @author minecreatr
 */
public class ModelCoordinateVariations extends WeightedBakedModel {

    private String variation;

    private ModelCoordinateVariations(List l, String v) {
        super(l);
        this.variation = v;
    }

    public static ModelCoordinateVariations newModel(String v) {
        Builder b = new Builder();
        b.add(new SmartSubtype(0), 1);
        return new ModelCoordinateVariations(b.build().models, v);
    }

    @Override
    public IBakedModel getAlternativeModel(long seed) {
        return new SmartSubtype(seed);
    }

    protected static class SmartSubtype implements ISmartBlockModel {


        private static final FaceBakery bakery = new FaceBakery();

        public static final ModelCTM.QuadPos quadPos = new ModelCTM.QuadPos(new Vector3f(0, 0, 0), new Vector3f(16, 16, 16));

        private long seed;

        private List<BakedQuad> quads;

        private TextureAtlasSprite particle;

        public SmartSubtype(long seed) {
            this.seed = seed;
        }

        @Override
        public IBakedModel handleBlockState(IBlockState state) {
            PropertyVariation VARIATION = null;
            if (state.getBlock() instanceof BlockCarvable) {
                VARIATION = ((BlockCarvable) state.getBlock()).VARIATION;
            }
            IBlockResources r = SubBlockUtil.getResources(state.getBlock(), (Variation) state.getValue(VARIATION));
            List<BakedQuad> newQuads = generateQuads(state, r);
            this.quads = newQuads;
            this.particle = r.getDefaultTexture();
            return this;
        }

        private List<BakedQuad> generateQuads(IBlockState state, IBlockResources r) {
            if (BlockResources.isV(r.getType())) {
                return makeVQuads(state, r);
            } else {
                return makeRQuads(state, r);
            }

        }

        private List<BakedQuad> makeVQuads(IBlockState s, IBlockResources r) {
            IExtendedBlockState state = (IExtendedBlockState) s;
            int variationSize = BlockResources.getVariationWidth(r.getType());

            int xModules = (Integer) state.getValue(BlockCarvable.XMODULES);
            int yModules = (Integer) state.getValue(BlockCarvable.YMODULES);
            int zModules = (Integer) state.getValue(BlockCarvable.ZMODULES);
            //This ensures that blocks placed near 0,0 or it's axis' do not misbehave
            int textureX = (xModules < 0) ? (xModules + variationSize) : xModules;
            int textureZ = (zModules < 0) ? (zModules + variationSize) : zModules;
            //Always invert the y index
            int textureY = (variationSize - yModules - 1);

            int interval = 16 / variationSize;

            List<BakedQuad> toReturn = new ArrayList<BakedQuad>();

            for (EnumFacing side : EnumFacing.values()) {
                int index;
                if (side == EnumFacing.DOWN || side == EnumFacing.UP) {
                    // DOWN || UP
                    index = textureX + textureZ * variationSize;
                } else if (side == EnumFacing.NORTH || side == EnumFacing.SOUTH) {
                    // NORTH || SOUTH
                    index = textureX + textureY * variationSize;
                } else {
                    // WEST || EAST
                    index = textureZ + textureY * variationSize;
                }
                //throw new RuntimeException(index % variationSize+" and "+index/variationSize);
                int minU = interval * (index % variationSize);
                int minV = interval * (index / variationSize);

                //Chisel.logger.info("Using u/v pairs "+minU+" "+minV+" "+maxU+" "+maxV);
                toReturn.add(makeQuad(side, r.getDefaultTexture(), new float[]{minU, minV, minU + interval, minV + interval}));
            }
            return toReturn;
        }

        private List<BakedQuad> makeRQuads(IBlockState s, IBlockResources r) {
            int bound = 4;
            int wid = 2;
            if (r.getType() == IBlockResources.R9) {
                bound = 9;
                wid = 3;
            } else if (r.getType() == IBlockResources.R16) {
                bound = 16;
                wid = 4;
            }
            Random random = new Random(seed);
            int num = random.nextInt(bound) + 1;
            float interval = 16 / wid;
            int unitsAcross = num % wid;
            int unitsDown = num / wid;
            if (unitsAcross == 0) {
                unitsAcross++;
            }
            if (unitsDown == 0) {
                unitsDown++;
            }
            float maxU = unitsAcross * interval;
            float maxV = unitsDown * interval;
            //Chisel.logger.info("maxU: "+maxU+" maxV: "+maxV);
            List<BakedQuad> toReturn = new ArrayList<BakedQuad>();
            for (EnumFacing f : EnumFacing.values()) {
                toReturn.add(makeQuad(f, r.getDefaultTexture(), new float[]{maxU - interval, maxV - interval, maxU, maxV}));
            }
            return toReturn;
        }

        public static BakedQuad makeQuad(EnumFacing f, TextureAtlasSprite sprite, float[] uvs) {
            return bakery.makeBakedQuad(quadPos.from, quadPos.to, new BlockPartFace(f, -1, sprite.getIconName(), new BlockFaceUV(uvs, 0)),
                    sprite, f, ModelRotation.X0_Y0, new BlockPartRotation(new Vector3f(1, 0, 0), f.getAxis(), 0, false), false, false);
        }

        @Override
        public List getFaceQuads(EnumFacing face) {
            List<BakedQuad> toReturn = new ArrayList<BakedQuad>();
            for (BakedQuad quad : quads) {
                if (quad == null) {
                    continue;
                }
                if (quad.getFace() == face) {
                    toReturn.add(quad);
                }
            }
            return toReturn;
        }

        @Override
        public List getGeneralQuads() {
            return this.quads;
        }

        @Override
        public boolean isAmbientOcclusion() {
            return true;
        }

        @Override
        public boolean isGui3d() {
            return true;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getTexture() {
            return particle;
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return ItemCameraTransforms.DEFAULT;
        }
    }

}
