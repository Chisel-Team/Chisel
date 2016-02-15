package team.chisel.client.render.texture;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumWorldBlockLayer;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.Quad;
import team.chisel.client.render.ctm.CTM;
import team.chisel.client.render.ctm.ISubmap;
import team.chisel.client.render.ctx.CTMBlockRenderContext;
import team.chisel.client.render.type.BlockRenderTypeCTMH;
import team.chisel.common.util.Dir;

public class ChiselTextureCTMH extends AbstractChiselTexture<BlockRenderTypeCTMH> {

    public ChiselTextureCTMH(BlockRenderTypeCTMH type, EnumWorldBlockLayer layer, TextureSpriteCallback... sprites) {
        super(type, layer, sprites);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, IBlockRenderContext context, int quadGoal) {
        Quad q = Quad.from(quad, DefaultVertexFormats.ITEM);
        if (quad.getFace().getAxis().isVertical()) {
            q = q.transformUVs(sprites[0].getSprite());
        } else {
            CTM ctm = context == null ? null : ((CTMBlockRenderContext) context).getCTM(quad.getFace());
            ISubmap submap = getQuad(ctm);
            q = q.transformUVs(sprites[1].getSprite(), submap);
        }
        return Collections.singletonList(q.rebake());
    }

    private ISubmap getQuad(CTM ctm) {
        if (ctm == null || !ctm.connectedOr(Dir.LEFT, Dir.RIGHT)) {
            return Quad.TOP_LEFT;
        } else if (ctm.connectedAnd(Dir.LEFT, Dir.RIGHT)) {
            return Quad.TOP_RIGHT;
        } else if (ctm.connected(Dir.LEFT)) {
            return Quad.BOTTOM_RIGHT;
        } else {
            return Quad.BOTTOM_LEFT;
        }
    }
}
