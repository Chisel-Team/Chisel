package team.chisel.api.chunkdata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;

public interface IChunkData<T> {

	void writeToNBT(Chunk chunk, NBTTagCompound tag);

	void readFromNBT(Chunk chunk, NBTTagCompound tag);

	boolean requiresClientSync();
	
	T getDataForChunk(ChunkCoordIntPair chunk);

}