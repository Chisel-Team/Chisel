package team.chisel.api.render;

/**
 * Interface for a block's render context
 */
public interface IBlockRenderContext {

    /**
     * Gets the compressed data, will only use bits up to the given compressed data length
     */
    long getCompressedData();

}
