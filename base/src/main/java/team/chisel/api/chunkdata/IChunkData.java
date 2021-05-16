package team.chisel.api.chunkdata;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;

public interface IChunkData<T> {
    
    ListNBT writeToNBT();

    void writeToNBT(@Nonnull IChunk chunk, @Nonnull CompoundNBT tag);
    
    Iterable<ChunkPos> readFromNBT(@Nonnull ListNBT tag);

    void readFromNBT(@Nonnull IChunk chunk, @Nonnull CompoundNBT tag);

    boolean requiresClientSync();

    T getDataForChunk(RegistryKey<World> dimID, @Nonnull ChunkPos chunk);
}
