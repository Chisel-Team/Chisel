package team.chisel.client.render.texture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumWorldBlockLayer;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.QuadHelper;
import team.chisel.client.render.ctx.BlockRenderContextSheet;
import team.chisel.client.render.type.BlockRenderTypeV;

/**
 * Texture for V texture types
 */
public class ChiselTextureV extends AbstractChiselTexture {

    public ChiselTextureV(BlockRenderTypeV type, EnumWorldBlockLayer layer, TextureSpriteCallback... sprites){
        super(type, layer, sprites);
    }

    @Override
    public List<BakedQuad> getSideQuads(EnumFacing side, IBlockRenderContext contextIn, int quadGoal) {
        if (contextIn == null){
            return Arrays.asList(QuadHelper.makeUVFaceQuad(side, sprites[0].getSprite(), new float[]{0, 0, 8, 8}));
        }

        BlockRenderContextSheet context = (BlockRenderContextSheet) contextIn;

        BlockPos pos = context.getPosition();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int xSize = context.getXSize();
        int ySize = context.getYSize();

        int tx, ty;

        // Calculate submap x/y from x/y/z by ignoring the direction which the side is offset on
        // Negate the y coordinate when calculating non-vertical directions, otherwise it is reverse order
        if (side.getAxis().isVertical()) {
            // DOWN || UP
            tx = x % xSize;
            ty = z % ySize;
        } else if (side.getAxis() == Axis.Z) {
            // NORTH || SOUTH
            tx = x % xSize;
            ty = -y % ySize;
        } else {
            // WEST || EAST
            tx = z % xSize;
            ty = -y % ySize;
        }

        // Reverse x order for north and east
        if (side == EnumFacing.NORTH || side == EnumFacing.EAST) {
            tx = (xSize - tx - 1) % xSize;
        }

        // Remainder can produce negative values, so wrap around
        if (tx < 0) {
            tx += xSize;
        }
        if (ty < 0) {
            ty += ySize;
        }

        int intervalU = 16 / xSize;
        int intervalV = 16 / ySize;

        // throw new RuntimeException(index % variationSize+" and "+index/variationSize);
        int minU = intervalU * tx;
        int minV = intervalV * ty;
        if (quadGoal != 4) {
            List<BakedQuad> list = new ArrayList<BakedQuad>();
            list.add(QuadHelper.makeUVFaceQuad(side, sprites[0].getSprite(), new float[] { minU, minV, minU + intervalU, minV + intervalV }));
            return list;
        } else {
            //Chisel.debug("V texture complying with quad goal of 4");
            //Chisel.debug(new float[] { minU, minV, minU + intervalU, minV + intervalV });
            return QuadHelper.makeFourQuads(side, sprites[0].getSprite(), new float[] { minU, minV, minU + intervalU, minV + intervalV });
        }
    }
}
