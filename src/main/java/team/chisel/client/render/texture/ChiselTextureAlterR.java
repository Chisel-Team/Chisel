package team.chisel.client.render.texture;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureInfo;
import team.chisel.client.render.Quad;
import team.chisel.client.render.ctm.ISubmap;
import team.chisel.client.render.ctx.BlockRenderContextAlterR;
import team.chisel.client.render.type.BlockRenderTypeAlterR;

public class ChiselTextureAlterR extends AbstractChiselTexture<BlockRenderTypeAlterR> {

    public ChiselTextureAlterR(BlockRenderTypeAlterR type, TextureInfo info) {
        super(type, info);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, IBlockRenderContext context, int quadGoal) {
        
        ISubmap outputQuad;
        int num = context == null ? 0 : ((BlockRenderContextAlterR)context).getTexture();
        
        switch(num){
            default:
            case 0:
                outputQuad = Quad.TOP_LEFT;
                break;
            case 1:
                outputQuad = Quad.TOP_RIGHT;
                break;
            case 2:
                outputQuad = Quad.BOTTOM_LEFT;
                break;
            case 3:
                outputQuad = Quad.BOTTOM_RIGHT;
                break;
        }

        return Collections.singletonList(makeQuad(quad).transformUVs(sprites[0].getSprite(), outputQuad).rebake());
    }
}
