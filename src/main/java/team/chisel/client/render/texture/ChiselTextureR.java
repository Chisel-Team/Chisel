package team.chisel.client.render.texture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.QuadHelper;
import team.chisel.client.render.ctx.BlockRenderContextSheet;

/**
 * Texture for R texture types
 */
public class ChiselTextureR extends AbstractChiselTexture {

    private static final Random rand = new Random();

    public ChiselTextureR(IBlockRenderType type, EnumWorldBlockLayer layer, TextureSpriteCallback[] sprites) {
        super(type, layer, sprites);
    }

    @Override
    public List<BakedQuad> getSideQuads(EnumFacing side, IBlockRenderContext context, int quadGoal) {

        BlockRenderContextSheet ctx = (BlockRenderContextSheet) context;
        BlockPos pos = ctx == null ? new BlockPos(0, 0, 0) : ctx.getPosition();
        rand.setSeed(MathHelper.getCoordinateRandom(pos.getX(), pos.getY(), pos.getZ()));
        rand.nextBoolean();

        // TODO this will NOT work. Need some kind of dummy context.
        int w = ctx == null ? 2 : ctx.getXSize();
        int h = ctx == null ? 2 : ctx.getYSize();
        float intervalX = 16 / w;
        float intervalY = 16 / h;

        int unitsAcross = rand.nextInt(w) + 1;
        int unitsDown = rand.nextInt(h) + 1;

        float maxU = unitsAcross * intervalX;
        float maxV = unitsDown * intervalY;
        List<BakedQuad> toReturn = new ArrayList<BakedQuad>();
        float[] uvs = new float[] { maxU - intervalX, maxV - intervalY, maxU, maxV };
        for (EnumFacing f : EnumFacing.values()) {
            toReturn.add(QuadHelper.makeUVFaceQuad(f, sprites[0].getSprite(), uvs));
        }
        return toReturn;
    }
}
