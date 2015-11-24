package team.chisel.client.render;

import team.chisel.Chisel;
import team.chisel.api.render.RenderType;
import team.chisel.client.render.ctm.ModelCTM;
import team.chisel.common.block.BlockCarvable;
import team.chisel.common.block.ItemChiselBlock;
import team.chisel.common.variation.PropertyVariation;
import team.chisel.common.variation.Variation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
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
public class ModelNonCTM implements ISmartBlockModel, ISmartItemModel {

    private List<BakedQuad> quads;

    private static final FaceBakery bakery = new FaceBakery();

    private BlockResources resources;

    public static final ModelCTM.QuadPos quadPos = new ModelCTM.QuadPos(new Vector3f(0, 0, 0), new Vector3f(16, 16, 16));


    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        PropertyVariation VARIATION = ((BlockCarvable) state.getBlock()).getType().getPropertyVariation();
        IBlockResources r = ((BlockCarvable) state.getBlock()).getSubBlock((Variation) state.getValue(VARIATION)).getResources();
        resources = (BlockResources) r;
        //BlockPos pos = ((IExtendedBlockState)state).getValue(BlockCarvable.BLOCK_POS);
        this.quads = generateQuads((BlockResources) r, state);
        return this;
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        //Chisel.logger.info("Handling itemstack for "+GameRegistry.findUniqueIdentifierFor(stack.getItem()));
        if (stack.getItem() instanceof ItemChiselBlock) {
            ItemChiselBlock itemBlock = (ItemChiselBlock) stack.getItem();
            if (itemBlock.getBlock() instanceof BlockCarvable) {
                BlockCarvable block = (BlockCarvable) itemBlock.getBlock();
                //Chisel.logger.info("index for "+GameRegistry.findUniqueIdentifierFor(block).toString()+" is "+block.getIndex());
                this.quads = generateQuads((BlockResources) block.allSubBlocks()[stack.getMetadata()].getResources(), null);
            } else {
                Chisel.debug("Not BlockCarvable?");
            }
        } else {
            Chisel.debug("Not Chisel Block Item?");
        }
        return this;
    }

    private List<BakedQuad> generateQuads(BlockResources r, IBlockState state) {
        RenderType type;
        type = r.getType();
        List<BakedQuad> toReturn = new ArrayList<BakedQuad>();

        for (EnumFacing f : EnumFacing.values()) {
            TextureAtlasSprite t = r.getDefaultTexture();
            if (f == EnumFacing.UP) {
                if (r.top != null) {
                    t = r.top;
                }
            } else if (f == EnumFacing.DOWN) {
                if (r.bottom != null) {
                    t = r.bottom;
                }
            } else if (r.side != null) {
                t = r.side;
            }
            boolean full = ((f == EnumFacing.UP || f == EnumFacing.DOWN) && (r.getType() == RenderType.CTMH || r.getType() == RenderType.CTMV));

            toReturn.add(makeQuad(f, t, type, full));
        }
        return toReturn;
    }

    public static BakedQuad makeQuad(EnumFacing f, TextureAtlasSprite sprite, RenderType type, boolean full) {
        int num = 16;
        if (type == RenderType.CTMH || type == RenderType.CTMV || type == RenderType.V4 || type == RenderType.R4)
            num = 8;
        else if (type == RenderType.V9 || type == RenderType.R9) num = 16 / 3;
        else if (type == RenderType.R16) num = 4;
        if (full) num = 16;
        return bakery.makeBakedQuad(quadPos.from, quadPos.to, new BlockPartFace(f, -1, sprite.getIconName(), new BlockFaceUV(new float[]{0, 0, num, num}, 0)),
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
        //throw new RuntimeException("Giving side quad");
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
        if (resources.getDefaultTexture() == null || resources.getDefaultTexture().getIconName().equals("missingno")) {
            if (resources.top == null) {
                return resources.side;
            }
            return resources.top;
        }
        return resources.getDefaultTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }
}
