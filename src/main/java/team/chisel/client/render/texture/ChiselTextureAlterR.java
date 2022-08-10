package team.chisel.client.render.texture;

import net.minecraft.client.renderer.block.model.BakedQuad;
import org.jetbrains.annotations.Nullable;
import team.chisel.client.render.ctx.BlockRenderContextAlterR;
import team.chisel.client.render.type.BlockRenderTypeAlterR;
import team.chisel.ctm.api.texture.ISubmap;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.render.AbstractTexture;
import team.chisel.ctm.client.util.Quad;

import java.util.Collections;
import java.util.List;

public class ChiselTextureAlterR extends AbstractTexture<BlockRenderTypeAlterR> {
    public ChiselTextureAlterR(BlockRenderTypeAlterR type, TextureInfo info) {
        super(type, info);
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, @Nullable ITextureContext context, int quadGoal) {

        ISubmap outputQuad;
        int num = context == null ? 0 : ((BlockRenderContextAlterR) context).getTexture();

        outputQuad = switch (num) {
            case 0 -> Quad.TOP_LEFT;
            case 1 -> Quad.TOP_RIGHT;
            case 2 -> Quad.BOTTOM_LEFT;
            case 3 -> Quad.BOTTOM_RIGHT;
            default -> throw new IllegalStateException("Unexpected value: " + num);
        };

        return Collections.singletonList(makeQuad(quad, context).transformUVs(sprites[0], outputQuad).rebake());
    }
}
