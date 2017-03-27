package team.chisel.client.render.ctx;

import java.util.EnumMap;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.vecmath.Point2i;

import com.google.common.base.Preconditions;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import team.chisel.client.render.texture.ChiselTextureMap;

@ParametersAreNonnullByDefault
public abstract class BlockRenderContextGridTexture extends BlockRenderContextPosition {
    
    public static class Patterned extends BlockRenderContextGridTexture {

        public Patterned(BlockPos pos, ChiselTextureMap tex, boolean applyOffset) {
            super(pos, tex, applyOffset);
        }

        @Override
        protected Point2i calculateTextureCoord(BlockPos pos, int w, int h, EnumFacing side) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            int tx, ty;

            // Calculate submap x/y from x/y/z by ignoring the direction which the side is offset on
            // Negate the y coordinate when calculating non-vertical directions, otherwise it is reverse order
            if (side.getAxis().isVertical()) {
                // DOWN || UP
                tx = x % w;
                ty = (side.getFrontOffsetY() * z + 1) % h;
            } else if (side.getAxis() == Axis.Z) {
                // NORTH || SOUTH
                tx = x % w;
                ty = -y % h;
            } else {
                // WEST || EAST
                tx = (z + 1) % w;
                ty = -y % h;
            }

            // Reverse x order for north and east
            if (side == EnumFacing.NORTH || side == EnumFacing.EAST) {
                tx = (w - tx - 1) % w;
            }

            // Remainder can produce negative values, so wrap around
            if (tx < 0) {
                tx += w;
            }
            if (ty < 0) {
                ty += h;
            }
            
            return new Point2i(tx, ty);
        }
    }
    
    public static class Random extends BlockRenderContextGridTexture {
        
        private static final java.util.Random rand = new java.util.Random();
        
        public Random(BlockPos pos, ChiselTextureMap tex, boolean applyOffset) {
            super(pos, tex, applyOffset);
        }

        @Override
        protected Point2i calculateTextureCoord(BlockPos pos, int w, int h, EnumFacing side) {

            rand.setSeed(MathHelper.getPositionRandom(pos) + side.ordinal());
            rand.nextBoolean();

            int tx = rand.nextInt(w) + 1;
            int ty = rand.nextInt(h) + 1;
            
            return new Point2i(tx, ty);
        }
    }
    
    private final EnumMap<EnumFacing, Point2i> textureCoords = new EnumMap<>(EnumFacing.class);    
    private final long serialized;

    @SuppressWarnings("null")
    public BlockRenderContextGridTexture(BlockPos pos, ChiselTextureMap tex, boolean applyOffset) {
        super(pos);

        // Since we can only return a long, we must limit to 10 bits of data per face = 60 bits
        Preconditions.checkArgument(tex.getXSize() * tex.getYSize() < 1024, "V* Texture size too large for texture %s", tex.getParticle());
        
        if (applyOffset) {
            applyOffset();
        }
        
        long serialized = 0;
        for (@Nonnull EnumFacing side : EnumFacing.VALUES) {

            Point2i coords = calculateTextureCoord(position, tex.getXSize(), tex.getYSize(), side);
            textureCoords.put(side, coords);
            
            // Calculate a unique index for a submap (x + (y * x-size)), then shift it left by the max bit storage (10 bits = 1024 unique indices)
            serialized |= (coords.x + (coords.y * tex.getXSize())) << (10 * side.ordinal());
        }
        
        this.serialized = serialized;
    }
    
    protected abstract Point2i calculateTextureCoord(BlockPos pos, int w, int h, EnumFacing side);
    
    @SuppressWarnings("null")
    public Point2i getTextureCoords(EnumFacing side) {
        return textureCoords.get(side);
    }

    @Override
    public long getCompressedData() {
        return serialized;
    }
}
