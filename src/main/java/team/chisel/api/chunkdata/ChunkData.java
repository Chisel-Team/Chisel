package team.chisel.api.chunkdata;

import javax.annotation.Nonnull;

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

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

    public static IOffsetData getOffsetForChunk(Level world, @Nonnull BlockPos pos) {
        return getOffsetForChunk(world.getChunkAt(pos));
    }

    public static IOffsetData getOffsetForChunk(LevelChunk chunk) {
        return getOffsetForChunk(chunk.getLevel().dimension(), chunk.getPos());
    }

    public static IOffsetData getOffsetForChunk(ResourceKey<Level> dimID, ChunkPos chunk) {
        IChunkData<? extends IOffsetData> data = offsetRegistry.<IChunkData<? extends IOffsetData>> getData(OFFSET_DATA_KEY);
        return data == null ? DUMMY : data.getDataForChunk(dimID, chunk);
    }
}
