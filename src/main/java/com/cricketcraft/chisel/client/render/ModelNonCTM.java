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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;

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

    private BlockResources resources;

    public static final ModelCTM.QuadPos quadPos = new ModelCTM.QuadPos(new Vector3f(0, 0, 0), new Vector3f(16, 16, 16));


    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        PropertyVariation VARIATION = ((BlockCarvable)state.getBlock()).getType().getPropertyVariation();
        IBlockResources r = ((BlockCarvable)state.getBlock()).getSubBlock((Variation)state.getValue(VARIATION)).getResources();
        resources=(BlockResources)r;
        BlockPos pos = ((IExtendedBlockState)state).getValue(BlockCarvable.BLOCK_POS);
        if (pos==null){
            throw new RuntimeException("Pos is null?");
        }
        this.quads=generateQuads((BlockResources)r, pos);
        return this;
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack){
        if (stack.getItem() instanceof ItemChiselBlock){
            ItemChiselBlock itemBlock = (ItemChiselBlock)stack.getItem();
            if (itemBlock.getBlock() instanceof  BlockCarvable){
                BlockCarvable block = (BlockCarvable)itemBlock.getBlock();
                Variation[] vars = block.getType().getVariants();
                this.quads = generateQuads((BlockResources)block.allSubBlocks()[stack.getMetadata()].getResources(), null);
            }
        }
        else {
            Chisel.logger.info("Not Chisel Block Item?");
        }
        return this;
    }

    private List<BakedQuad> generateQuads(BlockResources r, BlockPos pos){
        int type = IBlockResources.NORMAL;
        if (r instanceof CTMBlockResources){
            CTMBlockResources ctm = (CTMBlockResources)r;
            type = ctm.type;
        }
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
            toReturn.add(makeQuad(f, t, type, pos));
        }
        return toReturn;
    }

    public static BakedQuad makeQuad(EnumFacing f, TextureAtlasSprite sprite, int type, BlockPos pos){
        if (pos==null) {
            int num = 16;
            if (type == IBlockResources.CTMH || type == IBlockResources.CTMV || type == IBlockResources.V4 || type == IBlockResources.R4)
                num = 8;
            else if (type == IBlockResources.V9 || type == IBlockResources.R9) num = 16 / 3;
            else if (type == IBlockResources.R16) num = 4;
            return bakery.makeBakedQuad(quadPos.from, quadPos.to, new BlockPartFace(f, -1, sprite.getIconName(), new BlockFaceUV(new float[]{0, 0, num, num}, 0)),
                    sprite, f, ModelRotation.X0_Y0, new BlockPartRotation(new Vector3f(1, 0, 0), f.getAxis(), 0, false), false, false);
        }
        int variationSize = (type == IBlockResources.V9||type == IBlockResources.R9) ? 3 : 2;
        variationSize = (type == IBlockResources.R16) ? 4: variationSize;
        Chisel.logger.info("Rendering stuff, variation size is "+variationSize);
        int xModulus = pos.getX() % variationSize;
        int zModulus =pos.getZ() % variationSize;
        int textureX = (xModulus < 0) ? (xModulus + variationSize) : xModulus;
        int textureZ = (zModulus < 0) ? (zModulus + variationSize) : zModulus;
        int textureY = (variationSize - (pos.getY() % variationSize) - 1);

        if (f == EnumFacing.WEST|| f == EnumFacing.SOUTH) {
            //For WEST, SOUTH reverse the indexes for both X and Z
            textureX = (variationSize - textureX - 1);
            textureZ = (variationSize - textureZ - 1);
        } /*else if (side == 0) {
            //For DOWN, reverse the indexes for only Z
            textureZ = (variationSize - textureZ - 1);
        	}*/

        int index;
        if (f == EnumFacing.DOWN || f == EnumFacing.UP) {
            // DOWN || UP
            index = textureX + textureZ * variationSize;
        } else if (f == EnumFacing.NORTH || f == EnumFacing.SOUTH) {
            // NORTH || SOUTH
            index = textureX + textureY * variationSize;
        } else {
            // WEST || EAST
            index = textureZ + textureY * variationSize;
        }
        float minU = (index%variationSize)*(16/variationSize);
        float minV = (index/variationSize)*(16/variationSize);

        float maxU = ((index%variationSize)+1)*(16/variationSize);
        float maxV = ((index%variationSize)+1)*(16/variationSize);


        float uDif = maxU-minU;
        float vDif = maxV-minV;
        float tot = uDif+vDif;

        if (tot>16){
            if (uDif>8){
                maxU-=8;
            }
            if (vDif>8){
                maxV-=8;
            }
        }
        else if (tot<16){
            if (uDif<8){
                maxU+=8;
            }
            if (vDif<8){
                maxV+=8;
            }
        }
        

        return bakery.makeBakedQuad(quadPos.from, quadPos.to, new BlockPartFace(f, -1, sprite.getIconName(), new BlockFaceUV(new float[]{minU, minV, maxU, maxV}, 0)),
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
        if (resources.getDefaultTexture()==null||resources.getDefaultTexture().getIconName().equals("missingno")){
            if (resources.top==null){
                return resources.side;
            }
            return resources.top;
        }
        return resources.getDefaultTexture();
    }

    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }
}
