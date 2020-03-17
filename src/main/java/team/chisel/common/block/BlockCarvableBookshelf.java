package team.chisel.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import team.chisel.api.block.VariationData;

public class BlockCarvableBookshelf extends BlockCarvable {

    public BlockCarvableBookshelf(Properties properties, VariationData variation) {
        super(properties, variation);
    }
    
    @Override
    public float getEnchantPowerBonus(BlockState state, IWorldReader world, BlockPos pos) {
        return Blocks.BOOKSHELF.getEnchantPowerBonus(Blocks.BOOKSHELF.getDefaultState(), world, pos);
    }
}
