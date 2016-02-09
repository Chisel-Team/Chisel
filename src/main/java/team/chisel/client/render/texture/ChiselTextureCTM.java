package team.chisel.client.render.texture;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumWorldBlockLayer;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.Quad;
import team.chisel.client.render.type.BlockRenderTypeCTM;

public class ChiselTextureCTM extends AbstractChiselTexture<BlockRenderTypeCTM> {

    public ChiselTextureCTM(BlockRenderTypeCTM type, EnumWorldBlockLayer layer, TextureSpriteCallback[] sprites) {
        super(type, layer, sprites);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad bq, IBlockRenderContext context, int quadGoal) {
        Quad quad = Quad.from(bq, DefaultVertexFormats.ITEM);
        BakedQuad test = quad.rebake();
        Quad testQuad = Quad.from(test, DefaultVertexFormats.ITEM);

        quad = quad.transformUVs(sprites[0].getSprite());

        Quad[] quads = quad.subdivide(4);
        return Arrays.stream(quads).map(q -> q.rebake()).collect(Collectors.toList());
    }
}
