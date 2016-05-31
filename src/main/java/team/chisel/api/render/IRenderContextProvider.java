package team.chisel.api.render;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IRenderContextProvider {

    /**
     * Gets the block render context for this block
     *
     * @param world
     *            The world block access
     * @param pos
     *            The block position
     * @return The block render context
     */
    IBlockRenderContext getBlockRenderContext(IBlockAccess world, BlockPos pos);

    /**
     * Gets the render context from this compressed data. This int is just the data for this context
     */
    IBlockRenderContext getContextFromData(long data);
}
