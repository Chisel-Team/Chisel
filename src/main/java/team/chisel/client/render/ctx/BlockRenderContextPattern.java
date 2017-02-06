package team.chisel.client.render.ctx;

import java.util.EnumMap;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.vecmath.Point2i;

import com.google.common.base.Preconditions;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import team.chisel.client.render.texture.ChiselTextureMap;

@ParametersAreNonnullByDefault
public class BlockRenderContextPattern extends BlockRenderContextPosition {
    
    private final EnumMap<EnumFacing, Point2i> textureCoords = new EnumMap<>(EnumFacing.class);
    private final long serialized;
    
    public BlockRenderContextPattern(BlockPos pos, ChiselTextureMap tex) {
        super(pos);

        // Since we can only return a long, we must limit to 10 bits of data per face = 60 bits
        Preconditions.checkArgument(tex.getXSize() * tex.getYSize() < 1024, "V* Texture size too large for texture %s", tex.getParticle());
        long serialized = 0;
        
        for (EnumFacing side : EnumFacing.VALUES) {

            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            int tx, ty;

            // Calculate submap x/y from x/y/z by ignoring the direction which the side is offset on
            // Negate the y coordinate when calculating non-vertical directions, otherwise it is reverse order
            if (side.getAxis().isVertical()) {
                // DOWN || UP
                tx = x % tex.getXSize();
                ty = (side.getFrontOffsetY() * z + 1) % tex.getYSize();
            } else if (side.getAxis() == Axis.Z) {
                // NORTH || SOUTH
                tx = x % tex.getXSize();
                ty = -y % tex.getYSize();
            } else {
                // WEST || EAST
                tx = (z + 1) % tex.getXSize();
                ty = -y % tex.getYSize();
            }

            // Reverse x order for north and east
            if (side == EnumFacing.NORTH || side == EnumFacing.EAST) {
                tx = (tex.getXSize() - tx - 1) % tex.getXSize();
            }

            // Remainder can produce negative values, so wrap around
            if (tx < 0) {
                tx += tex.getXSize();
            }
            if (ty < 0) {
                ty += tex.getYSize();
            }
            
            textureCoords.put(side, new Point2i(tx, ty));
            
            // Calculate a unique index for a submap (x + (y * x-size)), then shift it left by the max bit storage (10 bits = 1024 unique indices)
            serialized |= (tx + (ty * tex.getXSize())) << (10 * side.ordinal());
        }
        
        this.serialized = serialized;
    }
    
    public Point2i getTextureCoords(EnumFacing side) {
        return textureCoords.get(side);
    }

    @Override
    public long getCompressedData() {
        return serialized;
    }
}
