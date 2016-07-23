package team.chisel.client.render.texture;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureInfo;
import team.chisel.client.render.Quad;
import team.chisel.client.render.ctm.ISubmap;
import team.chisel.client.render.ctx.BlockRenderContextPosition;
import team.chisel.client.render.type.BlockRenderTypeAlterR;

import static team.chisel.client.ClientUtil.rand;

public class ChiselTextureAlterR extends AbstractChiselTexture<BlockRenderTypeAlterR> {

    public ChiselTextureAlterR(BlockRenderTypeAlterR type, TextureInfo info) {
        super(type, info);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, IBlockRenderContext context, int quadGoal) {
        Quad q = Quad.from(quad).setFullbright(fullbright);
        ISubmap outputQuad;
        int num = 0;

        BlockPos pos = context == null ? new BlockPos(0, 0, 0) : ((BlockRenderContextPosition) context).getPosition();
        rand.setSeed(MathHelper.getPositionRandom(pos));
        rand.nextBoolean();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        num += rand.nextInt(2)*2;
        boolean type = true;

        // If even, switch boolean
        // If we have a set of multiple coords that are [x, y, z]
        // [5, 8, 9], [0, 0, 1], [9, 8, 8]...

        if(x % 2 == 0)
        {
            type = !type;
        }
        // Odd Even Odd
        // True False True

        if(y % 2 == 0)
        {
            type = !type;
        }
        // Even Even Even
        // False True False

        if(z % 2 == 0)
        {
            type = !type;
        }
        // Odd Odd Even
        // False True True

        num += type ? 0 : 1;

        switch(num){
            default:
            case 0:
                outputQuad = Quad.TOP_LEFT;
                break;
            case 1:
                outputQuad = Quad.TOP_RIGHT;
                break;
            case 2:
                outputQuad = Quad.BOTTOM_LEFT;
                break;
            case 3:
                outputQuad = Quad.BOTTOM_RIGHT;
                break;
        }

        return Collections.singletonList(Quad.from(quad).transformUVs(sprites[0].getSprite(), outputQuad).setFullbright(fullbright).rebake());
    }
}
