package team.chisel.api.chunkdata;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class ChunkData {
    public static final String OFFSET_DATA_KEY = "offsettool";
    private static final IOffsetData DUMMY = new IOffsetData() {
        private final BlockPos ZERO = new BlockPos(0, 0, 0);

        @Override
        public @NotNull BlockPos getOffset() {
            return ZERO;
        }

    };
    private static IChunkDataRegistry offsetRegistry;

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
        IChunkData<? extends IOffsetData> data = offsetRegistry.getData(OFFSET_DATA_KEY);
        return data.getDataForChunk(dimID, chunk);
    }
}
