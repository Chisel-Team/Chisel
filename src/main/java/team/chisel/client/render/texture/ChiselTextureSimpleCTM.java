package team.chisel.client.render.texture;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import net.minecraft.client.renderer.block.model.BakedQuad;
import team.chisel.client.render.type.BlockRenderTypeSimpleCTM;
import team.chisel.ctm.api.texture.ISubmap;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.ctx.TextureContextCTM;
import team.chisel.ctm.client.texture.render.AbstractTexture;
import team.chisel.ctm.client.texture.render.TextureCTM;
import team.chisel.ctm.client.util.CTMLogic;
import team.chisel.ctm.client.util.Dir;
import team.chisel.ctm.client.util.Quad;

public class ChiselTextureSimpleCTM extends TextureCTM<BlockRenderTypeSimpleCTM> {

    public ChiselTextureSimpleCTM(BlockRenderTypeSimpleCTM type, TextureInfo info) {
        super(type, info);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, ITextureContext context, int quadGoal) {
        Quad q = makeQuad(quad, context);

        if (context == null) {
            return Collections.singletonList(q.transformUVs(sprites[0], Quad.TOP_LEFT).rebake());
        }

        return Collections.singletonList(q.transformUVs(sprites[0], getQuad(((TextureContextCTM) context).getCTM(quad.getFace()))).rebake());
    }

    private ISubmap getQuad(CTMLogic ctm) {
        if (ctm == null) {
            return Quad.TOP_LEFT;
        }

        if (!ctm.connectedOr(Dir.TOP, Dir.RIGHT, Dir.BOTTOM, Dir.LEFT)) {
            return Quad.TOP_LEFT;
        } else if (ctm.connectedAnd(Dir.TOP, Dir.TOP_RIGHT, Dir.RIGHT, Dir.BOTTOM_RIGHT, Dir.BOTTOM, Dir.BOTTOM_LEFT, Dir.LEFT, Dir.TOP_LEFT)) {
            return Quad.BOTTOM_RIGHT;
        } else if (ctm.connectedAnd(Dir.TOP, Dir.RIGHT, Dir.BOTTOM, Dir.LEFT)) {
            return Quad.TOP_LEFT;
        } else if (ctm.connectedAnd(Dir.LEFT, Dir.RIGHT)) {
            return Quad.TOP_RIGHT;
        } else if (ctm.connectedAnd(Dir.TOP, Dir.BOTTOM)) {
            return Quad.BOTTOM_LEFT;
        } else {
            return Quad.TOP_LEFT;
        }
    }
    
    @Override
    public Optional<Boolean> connectInside() {
        return Optional.of(true);
    }
}
