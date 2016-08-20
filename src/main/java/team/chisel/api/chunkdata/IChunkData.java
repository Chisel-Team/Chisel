package team.chisel.api.chunkdata;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

public interface IChunkData<T> {

    void writeToNBT(@Nonnull Chunk chunk, @Nonnull NBTTagCompound tag);

    void readFromNBT(@Nonnull Chunk chunk, @Nonnull NBTTagCompound tag);

    boolean requiresClientSync();

    T getDataForChunk(int dimID, @Nonnull ChunkPos chunk);
}
