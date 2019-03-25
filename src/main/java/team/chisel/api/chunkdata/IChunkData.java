package team.chisel.api.chunkdata;

import java.util.Collections;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

public interface IChunkData<T> {
    
    default NBTTagList writeToNBT() {
        // No impl, backwards compat. TODO make non-default
        return new NBTTagList();
    }

    void writeToNBT(@Nonnull Chunk chunk, @Nonnull NBTTagCompound tag);
    
    default Iterable<ChunkPos> readFromNBT(@Nonnull NBTTagList tag) {
        // No impl, backwards compat. TODO make non-default
        return Collections.emptyList();
    }

    void readFromNBT(@Nonnull Chunk chunk, @Nonnull NBTTagCompound tag);

    boolean requiresClientSync();

    T getDataForChunk(int dimID, @Nonnull ChunkPos chunk);
}
