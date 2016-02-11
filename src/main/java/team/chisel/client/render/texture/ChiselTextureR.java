package team.chisel.client.render.texture;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.Quad;
import team.chisel.client.render.ctm.ISubmap;
import team.chisel.client.render.ctm.Submap;
import team.chisel.client.render.ctx.BlockRenderContextPosition;
import team.chisel.client.render.type.BlockRenderTypeR;

/**
 * Texture for R texture types
 */
public class ChiselTextureR extends AbstractChiselTexture<BlockRenderTypeR> {

    private static final Random rand = new Random();

    public ChiselTextureR(BlockRenderTypeR type, EnumWorldBlockLayer layer, TextureSpriteCallback[] sprites) {
        super(type, layer, sprites);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, IBlockRenderContext context, int quadGoal) {
        EnumFacing side = quad.getFace();
        
        BlockPos pos = context == null ? new BlockPos(0, 0, 0) : ((BlockRenderContextPosition) context).getPosition();
        rand.setSeed(MathHelper.getPositionRandom(pos) + side.ordinal());
        rand.nextBoolean();

        int w = getType().getXSize();
        int h = getType().getYSize();
        float intervalX = 16f / w;
        float intervalY = 16f / h;

        int unitsAcross = rand.nextInt(w) + 1;
        int unitsDown = rand.nextInt(h) + 1;

        float maxU = unitsAcross * intervalX;
        float maxV = unitsDown * intervalY;
        ISubmap uvs = new Submap(intervalX, intervalY, maxU - intervalX, maxV - intervalY);

        return Collections.singletonList(Quad.from(quad, DefaultVertexFormats.ITEM).transformUVs(sprites[0].getSprite(), uvs).rebake());
    }
}
