package com.chiselmod.chisel.client.render.ctm;

import com.chiselmod.chisel.client.render.CTMBlockResources;
import com.chiselmod.chisel.client.render.IBlockResources;
import com.chiselmod.chisel.common.block.BlockCarvable;
import com.chiselmod.chisel.common.block.subblocks.ICTMSubBlock;
import com.chiselmod.chisel.common.block.subblocks.ISubBlock;
import com.chiselmod.chisel.common.util.SubBlockUtil;
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
import net.minecraftforge.client.model.ISmartBlockModel;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Block Model for Connected textures
 * Deals with the Connected texture rendering basicly
 *
 * @author minecreatr
 */
public class ModelCTM implements ISmartBlockModel{

    private static final Vector3f FROM = new Vector3f(0, 0, 0);
    private static final Vector3f TO = new Vector3f(16, 16, 16);

    private List<BakedQuad> quads;

    public static final FaceBakery bakery = new FaceBakery();

    private TextureAtlasSprite particle;


    public ModelCTM(IBlockState state){
        this.quads=generateQuads(state);
    }

    public List getFaceQuads(EnumFacing face){
        List<BakedQuad> toReturn = new ArrayList<BakedQuad>();
        for (BakedQuad quad : quads){
            if (quad.getFace()==face){
                toReturn.add(quad);
            }
        }
        return toReturn;
    }

    public List getGeneralQuads(){
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

    public TextureAtlasSprite getTexture(){
        return particle;
    }

    public ItemCameraTransforms getItemCameraTransforms(){
        return ItemCameraTransforms.DEFAULT;
    }

    public IBakedModel handleBlockState(IBlockState state){
        List<BakedQuad> newQuads = generateQuads(state);
        this.quads=newQuads;
        this.particle = SubBlockUtil.getResources(state.getBlock(), (Integer)state.getValue(BlockCarvable.VARIATION)).noConnect;
        return this;
    }

    private List<BakedQuad> generateQuads(IBlockState state){
        List<BakedQuad> newQuads = new ArrayList<BakedQuad>();
        int variation = (Integer)state.getValue(BlockCarvable.VARIATION);
        if (state.getBlock() instanceof BlockCarvable){
            ISubBlock subBlock = ((BlockCarvable) state.getBlock()).getSubBlock(variation);
            if (subBlock instanceof ICTMSubBlock){
                ICTMSubBlock ctmSubBlock = (ICTMSubBlock)subBlock;
                for (EnumFacing f : EnumFacing.values()) {
                    newQuads.add(getQuad(f, state, ctmSubBlock.getResources()));
                }
            }
        }
        return newQuads;
    }

    private BakedQuad getQuad(EnumFacing side, IBlockState state, CTMBlockResources resources){
        boolean up = (Boolean)state.getValue(BlockCarvable.CONNECTED_UP);
        boolean down = (Boolean)state.getValue(BlockCarvable.CONNECTED_DOWN);
        boolean north = (Boolean)state.getValue(BlockCarvable.CONNECTED_NORTH);
        boolean south = (Boolean)state.getValue(BlockCarvable.CONNECTED_SOUTH);
        boolean west = (Boolean)state.getValue(BlockCarvable.CONNECTED_WEST);
        boolean east = (Boolean)state.getValue(BlockCarvable.CONNECTED_EAST);

        boolean isY = (side==EnumFacing.UP||side==EnumFacing.DOWN);
        boolean isX = (side==EnumFacing.WEST||side==EnumFacing.EAST);
        boolean isZ = (side==EnumFacing.NORTH||side==EnumFacing.SOUTH);
        int connections = 0;
        if (up&&!isY)connections++;
        if (down&&!isY)connections++;
        if (north&&!isZ)connections++;
        if (south&&!isZ)connections++;
        if (west&&!isX)connections++;
        if (east&&!isX)connections++;

        if (connections==0){
            return getDefaultQuad(side, state);
        }
        else if (connections==4){
            return makeQuad(side, resources.allConnect, 0);
        }

        if (side==EnumFacing.UP||side==EnumFacing.DOWN){
            if (up&&side==EnumFacing.UP){
                return getDefaultQuad(side, state);
            }
            else if (down&&side==EnumFacing.DOWN){
                return getDefaultQuad(side, state);
            }
            TextureAtlasSprite sprite = getTexture();
            int rotation = 0;
            if (connections==1){
                sprite = resources.oneConnect;
                if (north){
                    rotation=0;
                }
                else if (south){
                    rotation=180;
                }
                else if (east){
                    rotation=90;
                }
                else if (west){
                    rotation=270;
                }
            }
            else if (connections==2){
                if (north&&south){
                    sprite=resources.twoConnectPillar;
                    rotation=0;
                }
                else if (east&&west){
                    sprite=resources.twoConnectPillar;
                    rotation=90;
                }
                else if (north&&east){
                    sprite=resources.twoConnectCorner;
                    rotation=0;
                }
                else if (east&&south){
                    sprite=resources.twoConnectCorner;
                    rotation=90;
                }
                else if (south&&west){
                    sprite=resources.twoConnectCorner;
                    rotation=180;
                }
                else if (west&&north){
                    sprite=resources.twoConnectCorner;
                    rotation=270;
                }
            }
            else if (connections==3){
                sprite=resources.threeConnect;
                if (west&&north&&east){
                    rotation=0;
                }
                else if (north&&east&&south){
                    rotation=90;
                }
                else if (east&&south&&west){
                    rotation=180;
                }
                else if (south&&west&&north){
                    rotation=270;
                }
            }
            else if (connections==4){
                return makeQuad(side, resources.allConnect, 0);
            }
            else {
                return getDefaultQuad(side, state);
            }
            return makeQuad(side, sprite, rotation);
        }
        //else if (side==EnumFacing.EAST||side==EnumFacing.WEST);
        return getDefaultQuad(side, state);
    }

    private BakedQuad getDefaultQuad(EnumFacing f, IBlockState state){
        int v = (Integer)state.getValue(BlockCarvable.VARIATION);
        TextureAtlasSprite d = getTexture();
        if (state.getBlock() instanceof BlockCarvable){
            ISubBlock block = ((BlockCarvable) state.getBlock()).getSubBlock(v);
            if (block instanceof ICTMSubBlock){
                d=((ICTMSubBlock) block).getResources().getDefaultTexture();
            }
        }
        return makeQuad(f, d, 0);
    }

    
    private BakedQuad makeQuad(EnumFacing f, TextureAtlasSprite sprite, int rotation){
        return bakery.makeBakedQuad(FROM, TO, new BlockPartFace(f, 0, sprite.getIconName(),
                        new BlockFaceUV(new float[]{0, 0, 16, 16}, rotation)),
                sprite, f, ModelRotation.X0_Y0, new BlockPartRotation(new Vector3f(1, 0, 0), f.getAxis(), 0, false), false, false);
    }
}
