package team.chisel.common.block;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import team.chisel.api.block.VariationData;

@ParametersAreNonnullByDefault
public class BlockCarvableCarpet extends BlockCarvable {
    private static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 1, 0.0625, 1);

    /* Carpet */

    public BlockCarvableCarpet(Block.Properties properties, VariationData variation) {
        super(properties, variation);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return super.isValidPosition(state, worldIn, pos) && this.canBlockStay(worldIn, pos);
    }

    private boolean canBlockStay(IWorldReader worldIn, BlockPos pos) {
        return !worldIn.isAirBlock(pos.down());
    }
}
