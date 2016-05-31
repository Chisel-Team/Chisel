package team.chisel.client.render.texture;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import team.chisel.api.render.IBlockRenderContext;
import team.chisel.api.render.TextureInfo;
import team.chisel.client.render.Quad;
import team.chisel.client.render.ctm.ISubmap;
import team.chisel.client.render.ctm.Submap;
import team.chisel.client.render.ctx.BlockRenderContextPosition;
import team.chisel.client.render.type.BlockRenderTypeV;

import com.google.gson.JsonObject;

/**
 * Texture for V texture types
 */
public class ChiselTextureV extends AbstractChiselTexture<BlockRenderTypeV> {

    private int xSize;

    private int ySize;

    public ChiselTextureV(BlockRenderTypeV type, TextureInfo info){
        super(type, info);

        if (!this.info.isPresent()){
            throw new RuntimeException("Texture type V must have texture height and width or size associated with it!");
        }
        else {
            JsonObject object = this.info.get();
            if (object.has("width") && object.has("height")){
                if (object.get("width").isJsonPrimitive() && object.get("width").getAsJsonPrimitive().isNumber()){
                    this.xSize = object.get("width").getAsInt();
                }
                else {
                    throw new RuntimeException("Width must be a number!");
                }
                if (object.get("height").isJsonPrimitive() && object.get("height").getAsJsonPrimitive().isNumber()){
                    this.xSize = object.get("height").getAsInt();
                }
                else {
                    throw new RuntimeException("Height must be a number!");
                }
            }
            else if (object.has("size")){
                if (object.get("size").isJsonPrimitive() && object.get("size").getAsJsonPrimitive().isNumber()){
                    this.xSize = object.get("size").getAsInt();
                    this.ySize = object.get("size").getAsInt();
                }
                else {
                    throw new RuntimeException("Size must be a number!");
                }
            }
            else {
                throw new RuntimeException("Texture type V must have texture height and width or size associated with it!");
            }
        }
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, IBlockRenderContext context, int quadGoal) {
        EnumFacing side = quad.getFace();
        BlockPos pos = context == null ? new BlockPos(0, 0, 0) : ((BlockRenderContextPosition)context).getPosition();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

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
        
        Quad q = Quad.from(quad).setFullbright(fullbright);
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
