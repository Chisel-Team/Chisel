package team.chisel.client.render.texture;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureSpriteCallback;
import team.chisel.client.render.Quad;
import team.chisel.client.render.ctm.ISubmap;
import team.chisel.client.render.ctm.Submap;
import team.chisel.client.render.ctx.BlockRenderContextPosition;
import team.chisel.client.render.type.BlockRenderTypeV;

/**
 * Texture for V texture types
 */
public class ChiselTextureV extends AbstractChiselTexture<BlockRenderTypeV> {

    public ChiselTextureV(BlockRenderTypeV type, BlockRenderLayer layer, TextureSpriteCallback... sprites){
        super(type, layer, sprites);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, IBlockRenderContext context, int quadGoal) {
        EnumFacing side = quad.getFace();
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
        
        ISubmap submap = new Submap(intervalU, intervalV, minU, minV);
        
        Quad q = Quad.from(quad, DefaultVertexFormats.ITEM);
        if (quadGoal != 4) {
            return Collections.singletonList(q.transformUVs(sprites[0].getSprite(), submap).rebake());
        } else {
            //Chisel.debug("V texture complying with quad goal of 4");
            //Chisel.debug(new float[] { minU, minV, minU + intervalU, minV + intervalV });
            
            Quad[] quads = q.subdivide(4);
            
            for (int i = 0; i < quads.length; i++) {
                if (quads[i] != null) {
                    quads[i] = quads[i].transformUVs(sprites[0].getSprite(), submap);
                }
            }
            return Arrays.stream(quads).filter(Objects::nonNull).map(Quad::rebake).collect(Collectors.toList());
        }
    }
}
