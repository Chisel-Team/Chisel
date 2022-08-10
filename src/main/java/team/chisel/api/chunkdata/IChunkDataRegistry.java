package team.chisel.api.chunkdata;


public interface IChunkDataRegistry {

    void registerChunkData(String key, IChunkData<?> cd);

    <T extends IChunkData<?>> T getData(String key);

}
