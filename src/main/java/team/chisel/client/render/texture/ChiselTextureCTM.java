package team.chisel.client.render.texture;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import team.chisel.client.render.QuadHelper;
import team.chisel.client.render.ctm.CTM;
import team.chisel.client.render.type.BlockRenderTypeCTM;
import team.chisel.client.render.ctx.CTMBlockRenderContext;

import java.util.List;

/**
 * Chisel Texture for CTM
 */
public class ChiselTextureCTM extends AbstractChiselTexture<CTMBlockRenderContext> {


    public ChiselTextureCTM(BlockRenderTypeCTM type, TextureAtlasSprite[] sprites){
        super(type, sprites);
    }

    @Override
    public List<BakedQuad> getSideQuads(EnumFacing side, CTMBlockRenderContext context) {
        return QuadHelper.makeCtmFace(side, this.sprites, CTM.getInstance().getSubmapIndices(context, side));
    }
}
