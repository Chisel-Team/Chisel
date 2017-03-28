package team.chisel.client.render.ctx;

import javax.annotation.Nonnull;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import team.chisel.Chisel;
import team.chisel.api.chunkdata.ChunkData;
import team.chisel.api.render.IBlockRenderContext;

public class BlockRenderContextPosition implements IBlockRenderContext {

    protected @Nonnull BlockPos position;

    public BlockRenderContextPosition(@Nonnull BlockPos pos) {
        this.position = pos;
    }

    public BlockRenderContextPosition(int x, int y, int z) {
        this(new BlockPos(x, y, z));
    }
    
    public BlockRenderContextPosition applyOffset() {
        this.position = position.add(ChunkData.getOffsetForChunk(Chisel.proxy.getClientWorld().provider.getDimension(), new ChunkPos(position)).getOffset());
        return this;
    }

    public @Nonnull BlockPos getPosition() {
        return position;
    }

    @Override
    public long getCompressedData() {
        return 0L; // Position data is not useful for serialization (and in fact breaks caching as each location is a new key)
    }
}
