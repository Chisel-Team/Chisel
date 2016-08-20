package team.chisel.client.render.ctx;

import javax.annotation.Nonnull;

import net.minecraft.util.math.BlockPos;
import team.chisel.api.render.IBlockRenderContext;

public class BlockRenderContextPosition implements IBlockRenderContext {

    private @Nonnull BlockPos position;

    public BlockRenderContextPosition(@Nonnull BlockPos pos) {
        this.position = pos;
    }

    public BlockRenderContextPosition(int x, int y, int z) {
        this(new BlockPos(x, y, z));
    }

    public @Nonnull BlockPos getPosition() {
        return position;
    }

    @Override
    public long getCompressedData() {
        return getPosition().toLong();
    }
}
