package team.chisel.client.render.ctm;

import team.chisel.common.block.BlockCarvable;
import team.chisel.common.block.subblocks.ICTMSubBlock;
import team.chisel.common.block.subblocks.ISubBlock;
import team.chisel.common.util.SubBlockUtil;
import team.chisel.common.variation.PropertyVariation;
import team.chisel.common.variation.Variation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

/**
 * Block Model for Connected textures
 * Deals with the Connected texture rendering basically
 *
 * @author minecreatr
 */
public class ModelCTM implements ISmartBlockModel {


    private List<BakedQuad> quads;

    private TextureAtlasSprite particle;


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

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        // /throw new RuntimeException(state.getValue(BlockCarvable.VARIATION).toString());
        PropertyVariation VARIATION = null;
        if (state.getBlock() instanceof BlockCarvable) {
            VARIATION = ((BlockCarvable) state.getBlock()).VARIATION;
        }
        List<BakedQuad> newQuads = generateQuads(state, VARIATION);
        this.quads = newQuads;
        this.particle = SubBlockUtil.getResources(state.getBlock(), (Variation) state.getValue(VARIATION)).getDefaultTexture();
        return this;
    }

    private List<BakedQuad> generateQuads(IBlockState state, PropertyVariation VARIATION) {
        List<BakedQuad> newQuads = new ArrayList<BakedQuad>();
        Variation variation = (Variation) state.getValue(VARIATION);
        if (state.getBlock() instanceof BlockCarvable) {
            ISubBlock subBlock = ((BlockCarvable) state.getBlock()).getSubBlock(variation);
            if (subBlock instanceof ICTMSubBlock) {
                ICTMSubBlock ctmSubBlock = (ICTMSubBlock) subBlock;
                for (EnumFacing f : EnumFacing.values()) {
                    CTMFaceBakery.instance.makeCtmFace(f, ctmSubBlock.getResources(), CTM.getSubmapIndices((IExtendedBlockState) state, f, ctmSubBlock.getResources().getType())
                    ).addToList(newQuads);
                }

            }
        }
        return newQuads;
    }

    public static boolean isTouchingSide(IBlockState inState, EnumFacing f) {
        if (inState == null) {
            return false;
        }
        if (!(inState instanceof IExtendedBlockState)) {
            return false;
        }
        IExtendedBlockState state = (IExtendedBlockState) inState;
        boolean up = (Boolean) state.getValue(BlockCarvable.CONNECTED_UP);
        boolean down = (Boolean) state.getValue(BlockCarvable.CONNECTED_DOWN);
        boolean north = (Boolean) state.getValue(BlockCarvable.CONNECTED_NORTH);
        boolean south = (Boolean) state.getValue(BlockCarvable.CONNECTED_SOUTH);
        boolean west = (Boolean) state.getValue(BlockCarvable.CONNECTED_WEST);
        boolean east = (Boolean) state.getValue(BlockCarvable.CONNECTED_EAST);
        if (up && f == EnumFacing.UP) {
            return true;
        }
        if (down && f == EnumFacing.DOWN) {
            return true;
        }
        if (north && f == EnumFacing.NORTH) {
            return true;
        }
        if (south && f == EnumFacing.SOUTH) {
            return true;
        }
        if (west && f == EnumFacing.WEST) {
            return true;
        }
        if (east && f == EnumFacing.EAST) {
            return true;
        }
        return false;
    }


    /**
     * Two vectors to represent the position of a quad
     */
    public static class QuadPos {
        public Vector3f from;
        public Vector3f to;

        public QuadPos(Vector3f from, Vector3f to) {
            this.from = from;
            this.to = to;
        }
    }
}
