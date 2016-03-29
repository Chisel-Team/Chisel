package team.chisel.client.render.texture;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.BlockRenderLayer;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.Quad;
import team.chisel.client.render.ctm.CTM;
import team.chisel.client.render.ctm.ISubmap;
import team.chisel.client.render.type.BlockRenderTypeSimpleCTM;
import team.chisel.common.util.Dir;

import java.util.List;

public class ChiselTextureSimpleCTM  extends AbstractChiselTexture<BlockRenderTypeSimpleCTM>
{
    public ChiselTextureSimpleCTM(BlockRenderTypeSimpleCTM type, BlockRenderLayer layer, TextureSpriteCallback... sprites) {
        super(type, layer, sprites);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, IBlockRenderContext context, int quadGoal) {
        return null;
    }

    private ISubmap getQuad(CTM ctm) {
        if (ctm == null || !ctm.connectedOr(Dir.TOP, Dir.RIGHT, Dir.BOTTOM, Dir.LEFT))
        {
            return Quad.TOP_LEFT;
        }
        else if (ctm.connectedAnd(Dir.TOP, Dir.RIGHT, Dir.BOTTOM, Dir.LEFT))
        {
            return Quad.TOP_LEFT;
        }
        else if (ctm.connectedAnd(Dir.LEFT, Dir.RIGHT))
        {
            return Quad.TOP_RIGHT;
        }
        else if (ctm.connectedAnd(Dir.TOP, Dir.BOTTOM))
        {
            return Quad.BOTTOM_LEFT;
        }
        else
        {
            return Quad.BOTTOM_RIGHT;
        }
    }
}
