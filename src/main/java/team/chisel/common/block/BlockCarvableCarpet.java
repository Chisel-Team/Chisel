package team.chisel.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import team.chisel.api.block.VariationData;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BlockCarvableCarpet extends BlockCarvable {
    private static final AxisAlignedBB CARPET_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);

    /* Carpet */

    public BlockCarvableCarpet(Material material, int index, int max, VariationData... variations) {
        super(material, index, max, variations);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CARPET_AABB;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        this.checkForDrop(worldIn, pos, state);
    }

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
        if(!this.canBlockStay(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        } else {
            return true;
        }
    }

    private boolean canBlockStay(World worldIn, BlockPos pos) {
        return !worldIn.isAirBlock(pos.down());
    }
}
