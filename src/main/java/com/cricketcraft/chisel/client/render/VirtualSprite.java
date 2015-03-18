//package com.cricketcraft.chisel.client.render;
//
//import net.minecraft.client.renderer.block.model.BakedQuad;
//import net.minecraft.client.renderer.block.model.BlockFaceUV;
//import net.minecraft.client.renderer.block.model.BlockPartFace;
//import net.minecraft.client.renderer.block.model.BlockPartRotation;
//import net.minecraft.client.renderer.block.model.FaceBakery;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.client.resources.model.ModelRotation;
//import net.minecraft.util.EnumFacing;
//
//import javax.vecmath.Vector3f;
//
///**
// * Represents a virtual sprite
// *
// * @author minecreatr
// */
//public class VirtualSprite {
//
//    private static final Vector3f FROM = new Vector3f(0, 0, 0);
//    private static final Vector3f TO = new Vector3f(16, 16, 16);
//
//    public static final FaceBakery bakery = new FaceBakery();
//
//    private TextureAtlasSprite parent;
//    public int minU;
//    public int minV;
//    public int maxU;
//    public int maxV;
//
//    public VirtualSprite(TextureAtlasSprite parent, int minU, int minV, int maxU, int maxV){
//        this.parent=parent;
//        this.minU=minU;
//        this.minV=minV;
//        this.maxU=maxU;
//        this.maxV=maxV;
//    }
//
//    public BlockFaceUV getBlockFaceUV(int rotation){
//        return new BlockFaceUV(new float[]{minU, minV, maxU, maxV}, rotation);
//    }
//
//    public BakedQuad getQuad(EnumFacing f, int rotation){
//        return bakery.makeBakedQuad(FROM, TO, new BlockPartFace(f, 0,parent.getIconName(),
//                        getBlockFaceUV(rotation)),
//                parent, f, ModelRotation.X0_Y0, new BlockPartRotation(new Vector3f(1, 0, 0), f.getAxis(), 0, false), false, false);
//    }
//}
