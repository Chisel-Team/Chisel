package team.chisel.client.render;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import team.chisel.client.render.ctm.CTMFaceBakery;

import javax.vecmath.Vector3f;
import java.util.List;

/**
 * Helper class for quad stuff
 */
public class QuadHelper {

    private static final CTMFaceBakery ctmBakery = new CTMFaceBakery();

    private static final FaceBakery bakery = new FaceBakery();

    private static final QuadPos quadPos = new QuadPos(new Vector3f(0, 0, 0), new Vector3f(16, 16, 16));


    /**
     * Make a normal quad for a face
     * @param f The Face
     * @param sprite The Sprite
     * @return The Quad
     */
    public static BakedQuad makeNormalFaceQuad(EnumFacing f, TextureAtlasSprite sprite){
        return makeUVFaceQuad(f, sprite, new float[]{0, 0, 16, 16});
    }

    /**
     * Make a face quad with the specified uvs
     * @param f The Face
     * @param sprite The Sprite
     * @param uvs The uvs
     * @return The Quad
     */
    public static BakedQuad makeUVFaceQuad(EnumFacing f, TextureAtlasSprite sprite, float[] uvs){
        return bakery.makeBakedQuad(quadPos.from, quadPos.to, new BlockPartFace(f, -1, sprite.getIconName(), new BlockFaceUV(uvs, 0)),
                sprite, f, ModelRotation.X0_Y0, new BlockPartRotation(new Vector3f(1, 0, 0), f.getAxis(), 0, false), false, false);
    }

    /**
     * Makes a ctm face
     *
     * @param side      The Side
     * @param sprites The Sprites
     * @param quads     The Quad "ids"
     * @return The CTM Face
     */
    public static List<BakedQuad> makeCtmFace(EnumFacing side, TextureAtlasSprite[] sprites, int[] quads) {
        return ctmBakery.makeCtmFace(side, sprites, quads);
    }
}
