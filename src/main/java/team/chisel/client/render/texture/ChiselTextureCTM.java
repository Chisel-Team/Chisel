package team.chisel.client.render.texture;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.QuadHelper;
import team.chisel.client.render.ctm.CTM;
import team.chisel.client.render.ctx.CTMBlockRenderContext;
import team.chisel.client.render.type.BlockRenderTypeCTM;

/**
 * Chisel Texture for CTM
 */
public class ChiselTextureCTM extends AbstractChiselTexture {

    public ChiselTextureCTM(BlockRenderTypeCTM type, EnumWorldBlockLayer layer, TextureSpriteCallback... sprites){
        super(type, layer, sprites);
    }

    @Override
    public List<BakedQuad> getSideQuads(EnumFacing side, IBlockRenderContext context, int target) {
        if (context == null){
            return Arrays.asList(QuadHelper.makeNormalFaceQuad(side, sprites[0].getSprite()));
        }
        else {
            return QuadHelper.makeCtmFace(side, this.sprites, CTM.getInstance().getSubmapIndices((CTMBlockRenderContext) context, side));
        }
    }
}
