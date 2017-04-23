package team.chisel.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * To be implemented on blocks that "hide" another block inside, so connected textures can still be accomplished.
 */
public interface IFacade {

    /**
     * Gets the blockstate this facade appears as.
     *
     * @param world
     *            {@link World}
     * @param pos
     *            The Blocks position
     * @param side
     *            The side being rendered, NOT the side being connected from.
     *            <p/>
     *            This value can be null if no side is specified. Please handle this appropriately.
     * @param neighbor
     *            The state to check against.
     * @return The blockstate which your block appears as.
     */
    @Nonnull
    IBlockState getFacade(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nullable EnumFacing side, @Nonnull IBlockState neighbor);

}
