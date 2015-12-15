package team.chisel.client.render.texture;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import team.chisel.Chisel;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.QuadHelper;
import team.chisel.client.render.type.BlockRenderTypeNormal;

import java.util.Arrays;
import java.util.List;

/**
 * Chisel texture for a normal texture
 */
public class ChiselTextureNormal extends AbstractChiselTexture {

    public ChiselTextureNormal(BlockRenderTypeNormal type, TextureSpriteCallback[] sprites){
        super(type, sprites);
    }

    @Override
    public List<BakedQuad> getSideQuads(EnumFacing side, IBlockRenderContext context, int target){
        if (target == 4){
            Chisel.debug("Normal texture complying with quad goal of 4");
            return QuadHelper.makeFourQuads(side, sprites[0].getSprite(), new float[]{0, 0, 16, 16});
        }
        else {
            return Arrays.asList(QuadHelper.makeNormalFaceQuad(side, sprites[0].getSprite()));
        }
    }


}
