package team.chisel.client.render.texture;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumWorldBlockLayer;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.Quad;
import team.chisel.client.render.ctm.CTM;
import team.chisel.client.render.ctx.CTMBlockRenderContext;
import team.chisel.client.render.type.BlockRenderTypeCTM;

public class ChiselTextureCTM extends AbstractChiselTexture<BlockRenderTypeCTM> {

    public ChiselTextureCTM(BlockRenderTypeCTM type, EnumWorldBlockLayer layer, TextureSpriteCallback[] sprites) {
        super(type, layer, sprites);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad bq, IBlockRenderContext context, int quadGoal) {
        Quad quad = Quad.from(bq, DefaultVertexFormats.ITEM);
        if (context == null) {
            return Collections.singletonList(quad.transformUVs(sprites[0].getSprite()).rebake());
        }
        
        Quad[] quads = quad.subdivide(4);
        
        int[] ctm = ((CTMBlockRenderContext)context).getCTM(bq.getFace()).getSubmapIndices();
        
        for (int i = 0; i < quads.length; i++) {
            Quad q = quads[i];
            if (q != null) {
                int ctmid = q.getUvs().normalize().getQuadrant();
                quads[i] = q.transformUVs(sprites[ctm[ctmid] > 15 ? 0 : 1].getSprite(), CTM.uvs[ctm[ctmid]].normalize());
            }
        }
        return Arrays.stream(quads).filter(q -> q != null).map(q -> q.rebake()).collect(Collectors.toList());
    }
}
