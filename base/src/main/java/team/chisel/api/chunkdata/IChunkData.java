package team.chisel.api.chunkdata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;

import javax.annotation.Nonnull;

public interface IChunkData<T> {

    ListTag writeToNBT();

    void writeToNBT(@Nonnull ChunkAccess chunk, @Nonnull CompoundTag tag);

    Iterable<ChunkPos> readFromNBT(@Nonnull ListTag tag);

    void readFromNBT(@Nonnull ChunkAccess chunk, @Nonnull CompoundTag tag);

    boolean requiresClientSync();

    T getDataForChunk(ResourceKey<Level> dimID, @Nonnull ChunkPos chunk);
}
