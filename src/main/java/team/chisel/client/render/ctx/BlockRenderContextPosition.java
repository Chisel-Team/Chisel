package team.chisel.client.render.ctx;

import net.minecraft.util.math.BlockPos;
import team.chisel.api.render.IBlockRenderContext;

public class BlockRenderContextPosition implements IBlockRenderContext {

    private BlockPos position;

    public BlockRenderContextPosition(BlockPos pos) {
        this.position = pos;
    }

    public BlockRenderContextPosition(int x, int y, int z) {
        this(new BlockPos(x, y, z));
    }

    public BlockPos getPosition() {
        return position;
    }

    @Override
    public long getCompressedData() {
        return position.toLong();
    }
}
