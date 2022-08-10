package team.chisel.api.chunkdata;

import net.minecraft.core.BlockPos;

import javax.annotation.Nonnull;

/**
 * An object which contains info about the offset data for a chunk.
 */
public interface IOffsetData {

    /**
     * Gets the offset for this data
     *
     * @return A BlockPos with coordinates ranging from 0-15
     */
    @Nonnull
    BlockPos getOffset();

}
