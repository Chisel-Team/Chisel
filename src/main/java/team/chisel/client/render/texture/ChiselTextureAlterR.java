package team.chisel.client.render.texture;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import team.chisel.client.render.ctx.BlockRenderContextAlterR;
import team.chisel.client.render.type.BlockRenderTypeAlterR;
import team.chisel.ctm.api.texture.ISubmap;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.render.AbstractTexture;
import team.chisel.ctm.client.util.Quad;

public class ChiselTextureAlterR extends AbstractTexture<BlockRenderTypeAlterR> {

    public ChiselTextureAlterR(BlockRenderTypeAlterR type, TextureInfo info) {
        super(type, info);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, ITextureContext context, int quadGoal) {
        
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

        return Collections.singletonList(makeQuad(quad, context).transformUVs(sprites[0], outputQuad).rebake());
    }
}
