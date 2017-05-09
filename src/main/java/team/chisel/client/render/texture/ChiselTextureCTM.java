package team.chisel.client.render.texture;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import net.minecraft.client.renderer.block.model.BakedQuad;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureInfo;
import team.chisel.client.render.Quad;
import team.chisel.client.render.ctm.CTM;
import team.chisel.client.render.ctx.CTMBlockRenderContext;
import team.chisel.client.render.type.BlockRenderTypeCTM;

public class ChiselTextureCTM extends AbstractChiselTexture<BlockRenderTypeCTM> {

    public ChiselTextureCTM(BlockRenderTypeCTM type, TextureInfo info) {
        super(type, info);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad bq, IBlockRenderContext context, int quadGoal) {
        Quad quad = makeQuad(bq);
        if (context == null) {
            return Collections.singletonList(quad.transformUVs(sprites[0].getSprite()).rebake());
        }

        Quad[] quads = quad.subdivide(4);
        
        int[] ctm = ((CTMBlockRenderContext)context).getCTM(bq.getFace()).getSubmapIndices();
        
        for (int i = 0; i < quads.length; i++) {
            Quad q = quads[i];
            if (q != null) {
                int ctmid = q.getUvs().normalize().getQuadrant();
                quads[i] = q.grow().transformUVs(sprites[ctm[ctmid] > 15 ? 0 : 1].getSprite(), CTM.uvs[ctm[ctmid]].normalize());
            }
        }
        return Arrays.stream(quads).filter(Objects::nonNull).map(q -> q.rebake()).collect(Collectors.toList());
    }
}
