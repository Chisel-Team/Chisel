package team.chisel.client.render.texture;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
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
        if (target == 1){
            return Arrays.asList(QuadHelper.makeNormalFaceQuad(side, sprites[0].getSprite()));
        }
        else if (target == 4){

        }
    }


}
