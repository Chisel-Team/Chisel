package team.chisel.api.chunkdata;

import javax.annotation.Nonnull;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;

public class ChunkData {

    private static IChunkDataRegistry offsetRegistry;

    public static final String OFFSET_DATA_KEY = "offsettool";

    private static final IOffsetData DUMMY = new IOffsetData() {
        private final BlockPos ZERO = new BlockPos(0, 0, 0);
        
        @Override
        public BlockPos getOffset() {
            return ZERO;
        }

    };

    public static void setOffsetRegistry(IChunkDataRegistry registry) {
        if (offsetRegistry == null) {
            offsetRegistry = registry;
        } else {
            throw new IllegalStateException();
        }
    }

    public static IOffsetData getOffsetForChunk(World world, @Nonnull BlockPos pos) {
        return getOffsetForChunk(world.getChunkAt(pos));
    }

    public static IOffsetData getOffsetForChunk(Chunk chunk) {
        return getOffsetForChunk(chunk.getWorld().getDimension().getType(), chunk.getPos());
    }

    public static IOffsetData getOffsetForChunk(DimensionType dimID, ChunkPos chunk) {
        IChunkData<? extends IOffsetData> data = offsetRegistry.<IChunkData<? extends IOffsetData>> getData(OFFSET_DATA_KEY);
        return data == null ? DUMMY : data.getDataForChunk(dimID, chunk);
    }
}
