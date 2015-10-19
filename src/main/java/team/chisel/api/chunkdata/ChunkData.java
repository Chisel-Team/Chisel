package team.chisel.api.chunkdata;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ChunkData {

	private static IChunkDataRegistry offsetRegistry;

	public static final String OFFSET_DATA_KEY = "offsettool";

	private static final IOffsetData DUMMY = new IOffsetData() {

		@Override
		public int getOffsetX() {
			return 0;

		}

		@Override
		public int getOffsetY() {
			return 0;
		}

		@Override
		public int getOffsetZ() {
			return 0;
		}
	};

	public static void setOffsetRegistry(IChunkDataRegistry registry) {
		if (offsetRegistry == null) {
			offsetRegistry = registry;
		} else {
			throw new IllegalStateException();
		}
	}

	public static IOffsetData getOffsetForChunk(World world, int x, int z) {
		return getOffsetForChunk(world.getChunkFromBlockCoords(x, z));
	}

	public static IOffsetData getOffsetForChunk(Chunk chunk) {
		return getOffsetForChunk(chunk.worldObj.provider.dimensionId, chunk.getChunkCoordIntPair());
	}

	public static IOffsetData getOffsetForChunk(int dimID, ChunkCoordIntPair chunk) {
		IChunkData<? extends IOffsetData> data = offsetRegistry.<IChunkData<? extends IOffsetData>> getData(OFFSET_DATA_KEY);
		return data == null ? DUMMY : data.getDataForChunk(dimID, chunk);
	}
}
