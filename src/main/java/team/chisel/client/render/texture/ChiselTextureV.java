package team.chisel.client.render.texture;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.QuadHelper;
import team.chisel.client.render.ctx.ModuleBlockRenderContext;
import team.chisel.client.render.type.BlockRenderTypeV;

import java.util.ArrayList;
import java.util.List;

/**
 * Texture for V texture types
 */
public class ChiselTextureV extends AbstractChiselTexture {

    public ChiselTextureV(BlockRenderTypeV type, TextureSpriteCallback[] sprites){
        super(type, sprites);
    }

    @Override
    public List<BakedQuad> getSideQuads(EnumFacing side, IBlockRenderContext contextIn){

        ModuleBlockRenderContext context = (ModuleBlockRenderContext) contextIn;

        int xModules = context.getXModules();
        int yModules = context.getYModules();
        int zModules = context.getZModules();
        int variationSize = context.getVariationSize();
        //This ensures that blocks placed near 0,0 or it's axis' do not misbehave
        int textureX = (xModules < 0) ? (xModules + variationSize) : xModules;
        int textureZ = (zModules < 0) ? (zModules + variationSize) : zModules;
        //Always invert the y index
        int textureY = (variationSize - yModules - 1);

        int interval = 16 / variationSize;

        int index;
        if (side == EnumFacing.DOWN || side == EnumFacing.UP) {
            // DOWN || UP
            index = textureX + textureZ * variationSize;
        } else if (side == EnumFacing.NORTH || side == EnumFacing.SOUTH) {
            // NORTH || SOUTH
            index = textureX + textureY * variationSize;
        } else {
            // WEST || EAST
            index = textureZ + textureY * variationSize;
        }
        //throw new RuntimeException(index % variationSize+" and "+index/variationSize);
        int minU = interval * (index % variationSize);
        int minV = interval * (index / variationSize);

        List<BakedQuad> list = new ArrayList<BakedQuad>();
        list.add(QuadHelper.makeUVFaceQuad(side, sprites[0].getSprite(), new float[]{minU, minV, minU + interval, minV + interval}));
        return list;
    }
}
