package team.chisel.client.render.ctx;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import team.chisel.Chisel;
import team.chisel.api.chunkdata.ChunkData;

public class BlockRenderContextOffset extends BlockRenderContextPosition {

    public BlockRenderContextOffset(BlockPos pos) {
        super(pos.add(ChunkData.getOffsetForChunk(Chisel.proxy.getClientWorld().provider.getDimension(), new ChunkPos(pos)).getOffset()));
    }
}
