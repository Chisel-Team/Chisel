package team.chisel.client.render.texture;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.QuadHelper;
import team.chisel.client.render.type.BlockRenderTypeNormal;

/**
 * Chisel texture for a normal texture
 */
public class ChiselTextureNormal extends AbstractChiselTexture {

    public ChiselTextureNormal(BlockRenderTypeNormal type, EnumWorldBlockLayer layer, TextureSpriteCallback... sprites){
        super(type, layer, sprites);
    }

    @Override
    public List<BakedQuad> getSideQuads(EnumFacing side, IBlockRenderContext context, int target){
        if (target == 4){
            //Chisel.debug("Normal texture complying with quad goal of 4");
            return QuadHelper.makeFourQuads(side, sprites[0].getSprite(), new float[]{0, 0, 16, 16});
        }
        else {
            return Arrays.asList(QuadHelper.makeNormalFaceQuad(side, sprites[0].getSprite()));
        }
    }


}
