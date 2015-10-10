package team.chisel.client.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import team.chisel.Chisel;
import team.chisel.ctmlib.Drawing;
import team.chisel.utils.RoofingUtil;

/**
 * Rendering Roofing blocks
 */
public class RenderRoofing implements ISimpleBlockRenderingHandler {


    public static int renderID;

    public RenderRoofing(){
        renderID = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer){
        Drawing.drawBlock(block, metadata, renderer);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer){
        //north, south, west, east
        boolean[] sidedSloping = RoofingUtil.getSidesSloping(world, x, y, z, block);
        //south west, south east, north east, north west
        boolean[] cornersSloping = RoofingUtil.getCornersSloping(world, x, y, z, block);
        //boolean[] cullDirs = getCullDirs(world, x, y, z, block);
        Tessellator tess = Tessellator.instance;
        tess.addTranslation(x, y, z);
        if (world.getBlock(x, y, z).shouldSideBeRendered(world, x, y, z, 0)){
            renderer.renderFaceYNeg(block, 0, 0, 0, block.getIcon(0, world.getBlockMetadata(x, y, z)));
        }
//        if (world.getBlock(x, y, z).shouldSideBeRendered(world, x, y, z, 2)){
//            //renderer.renderFaceZNeg(block, 0, 0, 0, block.getIcon(2, world.getBlockMetadata(x, y, z)));
//        }
//        if (world.getBlock(x, y, z).shouldSideBeRendered(world, x, y, z, 3)){
//            renderer.renderFaceZNeg(block, 0, 0, 0, block.getIcon(3, world.getBlockMetadata(x, y, z)));
//        }
//        if (world.getBlock(x, y, z).shouldSideBeRendered(world, x, y, z, 4)){
//            renderer.renderFaceZNeg(block, 0, 0, 0, block.getIcon(4, world.getBlockMetadata(x, y, z)));
//        }
//        if (world.getBlock(x, y, z).shouldSideBeRendered(world, x, y, z, 5)){
//            renderer.renderFaceZNeg(block, 0, 0, 0, block.getIcon(5, world.getBlockMetadata(x, y, z)));
//        }

        if (sidedSloping[0] || cornersSloping[2] || cornersSloping[3]){
            renderSlopedPlane(0, tess, block.getIcon(1, world.getBlockMetadata(x, y, z)), cornersSloping);
        }
        if (sidedSloping[1] || cornersSloping[0] || cornersSloping[1]){
            renderSlopedPlane(1, tess, block.getIcon(1, world.getBlockMetadata(x, y, z)), cornersSloping);
        }
        if (sidedSloping[2] || cornersSloping[0] || cornersSloping[3]){
            renderSlopedPlane(2, tess, block.getIcon(1, world.getBlockMetadata(x, y, z)), cornersSloping);
        }
        if (sidedSloping[3] || cornersSloping[1] || cornersSloping[2]){
            renderSlopedPlane(3, tess, block.getIcon(1, world.getBlockMetadata(x, y, z)), cornersSloping);
        }
        tess.addTranslation(-x, -y, -z);
        return true;
    }


//    /**
//     * Format is [north, south, west, east]
//     */
//    private boolean[] getCullDirs(IBlockAccess world, int x, int y, int z, Block block){
//        boolean north = world.getBlock(x, y, z-1) == block;
//        boolean south = world.getBlock(x, y, z+1) == block;
//        boolean west = world.getBlock(x-1, y, z) == block;
//        boolean east = world.getBlock(x+1, y, z) == block;
//        return new boolean[]{north, south, west, east};
//    }

    /**
     * top side is north, south, west, east
     * corner is format south west, south east, north east, north west
     */
    private void renderSlopedPlane(int topSide, Tessellator tess, IIcon icon, boolean[] corners){
        float minU = icon.getInterpolatedU(0);
        float minV = icon.getInterpolatedV(0);
        float maxU = icon.getInterpolatedU(16);
        float maxV = icon.getInterpolatedV(16);
        int rotation = 0;
        if (topSide == 1){
            rotation = 2;
        }
        else if (topSide == 2){
            rotation = 3;
        }
        else if (topSide == 3){
            rotation = 1;
        }
        float[][] uvs = new float[][] {{minU, minV}, {maxU, minV}, {maxU, maxV}, {minU, maxV}};
        if (rotation != 0){
            uvs = getRotatedUVS(uvs, rotation);
        }
        Chisel.logger.info("UVS are "+uvs);
        if (corners[0] || corners[1] || corners[2] || corners[3]) {
            tess.addVertexWithUV(0, corners[0] ? sideElevations[topSide][3] : 0, 1, uvs[3][0], uvs[3][1]); //south west
            tess.addVertexWithUV(1, corners[1] ? sideElevations[topSide][2] : 0, 1, uvs[2][0], uvs[2][1]); //south east
            tess.addVertexWithUV(1, corners[2] ? sideElevations[topSide][1] : 0, 0, uvs[1][0], uvs[1][1]); //north east
            tess.addVertexWithUV(0, corners[3] ? sideElevations[topSide][0] : 0, 0, uvs[0][0], uvs[0][1]); //north west
        }
        else {
            tess.addVertexWithUV(0, sideElevations[topSide][3], 1, uvs[3][0], uvs[3][1]); //south west
            tess.addVertexWithUV(1, sideElevations[topSide][2], 1, uvs[2][0], uvs[2][1]); //south east
            tess.addVertexWithUV(1, sideElevations[topSide][1], 0, uvs[1][0], uvs[1][1]); //north east
            tess.addVertexWithUV(0, sideElevations[topSide][0], 0, uvs[0][0], uvs[0][1]); //north west
        }

//        tess.addVertexWithUV(0, sideElevations[topSide][0], 0, uvs[0][0], uvs[0][1]); //north west
//        tess.addVertexWithUV(1, sideElevations[topSide][1], 0, uvs[1][0], uvs[1][1]); //north east
//        tess.addVertexWithUV(1, sideElevations[topSide][2], 1, uvs[2][0], uvs[2][1]); //south east
//        tess.addVertexWithUV(0, sideElevations[topSide][3], 1, uvs[3][0], uvs[3][1]); //south west
        Chisel.logger.info("Rendering sloped plane");
    }

//    /**
//     * top corner is south west, south east, north east, north west
//     */
//    private void renderSlopedCorner(int topSide, Tessellator tess, IIcon icon){
//        float minU = icon.getInterpolatedU(0);
//        float minV = icon.getInterpolatedV(0);
//        float maxU = icon.getInterpolatedU(16);
//        float maxV = icon.getInterpolatedV(16);
//
//        int rotation = 0;
//        if (topSide == 1){
//            rotation = 2;
//        }
//        else if (topSide == 2){
//            rotation = 3;
//        }
//        else if (topSide == 3){
//            rotation = 1;
//        }
//        float[][] uvs = new float[][] {{minU, minV}, {maxU, minV}, {maxU, maxV}, {minU, maxV}};
//        if (rotation != 0){
//            uvs = getRotatedUVS(uvs, rotation);
//        }
//        Chisel.logger.info("UVS are "+uvs);
//        tess.addVertexWithUV(0, sideElevations[topSide][3], 1, uvs[3][0], uvs[3][1]); //south west
//        tess.addVertexWithUV(1, sideElevations[topSide][2], 1, uvs[2][0], uvs[2][1]); //south east
//        tess.addVertexWithUV(1, sideElevations[topSide][1], 0, uvs[1][0], uvs[1][1]); //north east
//        tess.addVertexWithUV(0, sideElevations[topSide][0], 0, uvs[0][0], uvs[0][1]); //north west
//
//    }

    /**
     * uvs is always in the format north west, north east, south east, south west
     * Rotate the uvs array right by one
     */
    private float[][] getRotatedUVS(float[][] uvs){
        float[] north_west = uvs[0];
        float[] north_east = uvs[1];
        float[] south_east = uvs[2];
        float[] south_west = uvs[3];
        uvs[0] = south_west;
        uvs[1] = north_west;
        uvs[2] = north_east;
        uvs[3] = south_east;
        return uvs;
    }

    /**
     * uvs is always in the format north west, north east, south east, south west
     * Rotate the uvs array by the specified amount
     */
    private float[][] getRotatedUVS(float[][] uvs, int amount){
        for (int i = 0 ; i < amount ; i++){
            uvs = getRotatedUVS(uvs);
        }
        return uvs;
    }

    /**
     * Sides are in order: north, south, west, east
     */
    private int[][] sideElevations = {{1, 1, 0, 0}, {0, 0, 1, 1}, {1, 0, 0, 1}, {0, 1, 1, 0}};

    /**
     * Corner uv's going counter clockwise starting at north west corner
     * north east south west
     */
    private float[][] sideUV = {
            {0, 0}, //North West
            {0, 16}, //North East
            {16, 16}, //South East
            {16, 0}}; //South West

    @Override
    public boolean shouldRender3DInInventory(int modelId){
        return true;
    }

    @Override
    public int getRenderId(){
        return renderID;
    }
}
