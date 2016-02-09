package team.chisel.client.render.texture;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumWorldBlockLayer;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.type.BlockRenderTypeV;

/**
 * Texture for V texture types
 */
public class ChiselTextureV extends AbstractChiselTexture<BlockRenderTypeV> {

    public ChiselTextureV(BlockRenderTypeV type, EnumWorldBlockLayer layer, TextureSpriteCallback... sprites){
        super(type, layer, sprites);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, IBlockRenderContext context, int quadGoal) {
        return Lists.newArrayList(quad);
        /*
        BlockPos pos = context == null ? new BlockPos(0, 0, 0) : ((BlockRenderContextPosition)context).getPosition();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int xSize = getType().getXSize();
        int ySize = getType().getYSize();

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

        float intervalU = 16f / xSize;
        float intervalV = 16f / ySize;

        // throw new RuntimeException(index % variationSize+" and "+index/variationSize);
        float minU = intervalU * tx;
        float minV = intervalV * ty;
        if (quadGoal != 4) {
            List<BakedQuad> list = new ArrayList<BakedQuad>();
            list.add(QuadHelper.makeUVFaceQuad(side, sprites[0].getSprite(), new float[] { minU, minV, minU + intervalU, minV + intervalV }));
            return list;
        } else {
            //Chisel.debug("V texture complying with quad goal of 4");
            //Chisel.debug(new float[] { minU, minV, minU + intervalU, minV + intervalV });
            return QuadHelper.makeFourQuads(side, sprites[0].getSprite(), new float[] { minU, minV, minU + intervalU, minV + intervalV });
        }
        */
    }
}
