package com.cricketcraft.chisel.client.render.ctm;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.client.render.CTMBlockResources;
import net.minecraft.block.BlockSlab;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.vecmath.Vector3f;

/**
 * Makes faces for CTM
 *
 * @author minecreatr
 */
public class CTMFaceBakery extends FaceBakery{

    public static final CTMFaceBakery instance = new CTMFaceBakery();

    public static final ModelCTM.QuadPos[] possibleQuads = new ModelCTM.QuadPos[]{
            new ModelCTM.QuadPos(new Vector3f(8, 0, 0), new Vector3f(16, 16, 8)), // South West Pillar quad 1 from top
            new ModelCTM.QuadPos(new Vector3f(8, 0, 8), new Vector3f(16, 16, 16)), // South East Pillar quad 2 from top
            new ModelCTM.QuadPos(new Vector3f(0, 0, 8), new Vector3f(8, 16, 16)), // North East Pillar quad 3 from top
            new ModelCTM.QuadPos(new Vector3f(0, 0, 0), new Vector3f(8, 16, 8)), // North West Pillar quad 4 from top

            new ModelCTM.QuadPos(new Vector3f(0, 0, 0), new Vector3f(16, 8, 8)), // North to South Down West quad 1 from South face
            new ModelCTM.QuadPos(new Vector3f(0, 0, 8), new Vector3f(16, 8, 16)), // North to South Down East quad 2 from South face
            new ModelCTM.QuadPos(new Vector3f(0, 8, 8), new Vector3f(16, 16, 16)), // North to South Up East quad 3 from South face
            new ModelCTM.QuadPos(new Vector3f(0, 8, 0), new Vector3f(16, 16, 8)), // North to South Up West quad 4 from South face

            new ModelCTM.QuadPos(new Vector3f(0, 0, 0), new Vector3f(8, 8, 16)), // West to East Down North quad 1 from East face
            new ModelCTM.QuadPos(new Vector3f(8, 0, 0), new Vector3f(16, 8, 16)), // West to East Down South quad 2 from East face
            new ModelCTM.QuadPos(new Vector3f(8, 8, 0), new Vector3f(16, 16, 16)), // West to East Up South quad 3 from East face
            new ModelCTM.QuadPos(new Vector3f(0, 8, 0), new Vector3f(8, 16, 16)) // West to East Up North quad 4 from East face

    };

    /**
     * Gets the id for the correct quad position
     * @param f The Direction it is facing
     * @param r The Region
     *
     *          From Up       Sides
     *            N             U
     *       W  |4|3|  E    N |4|3| S
     *          |1|2|         |1|2|
     *            S             D
     * @return The QuadPos
     */
    public static ModelCTM.QuadPos getCorrectQuadPos(EnumFacing f, int r){
        int num;
        boolean nonMain = false;
        if (f==EnumFacing.UP||f==EnumFacing.DOWN){
            num = 0;
        }
        else if (f==EnumFacing.NORTH||f==EnumFacing.SOUTH){
            num=4;
        }
        else if (f==EnumFacing.EAST||f==EnumFacing.WEST){
            num=8;
        }
        else {
            num=0;
        }
        if (f==EnumFacing.DOWN||f==EnumFacing.NORTH||f==EnumFacing.WEST){
            nonMain=true;
        }
        if (r==1&&nonMain){
            num+=1;
        }
        else if (r==2){
            if (!nonMain)num+=1;else num+=0;
        }
        else if (r==3){
            if (!nonMain)num+=2;else num+=3;
        }
        else if (r==4){
            if (!nonMain)num+=3;else num+=2;
        }
        return possibleQuads[num];
    }

    /**
     * Makes a ctm face
     * @param side The Side
     * @param resources The Resources to use
     * @param quads The Quad "ids"
     * @return The CTM Face
     */
    public CTMFace makeCtmFace(EnumFacing side, CTMBlockResources resources, int[] quads){
        Chisel.logger.info("Making CTM face");
        return new CTMFace(makeQuadFor(side, resources, quads[0], 1),
                           makeQuadFor(side, resources, quads[1], 2),
                           makeQuadFor(side, resources, quads[2], 3),
                           makeQuadFor(side, resources, quads[3], 4)
                );
    }

    private BakedQuad makeQuadFor(EnumFacing side, CTMBlockResources resources, int quad, int quadSection){
        ModelCTM.QuadPos pos = getCorrectQuadPos(side, quadSection);
        TextureAtlasSprite s;
        if (CTM.isDefaultTexture(quad)){
            s=resources.texture;
        }
        else {
            s=resources.ctmTexture;
        }
        return makeBakedQuad(pos.from, pos.to, new BlockPartFace(side, 0, s.getIconName(), new BlockFaceUV(CTM.uvs[quad], 0)),
                s, side, ModelRotation.X0_Y0, new BlockPartRotation(new Vector3f(1, 0, 0), side.getAxis(), 0, false), false, false);
    }
}
