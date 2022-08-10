package team.chisel.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import team.chisel.api.block.VariationData;

public class BlockCarvableBookshelf extends BlockCarvable {

    public BlockCarvableBookshelf(Properties properties, VariationData variation) {
        super(properties, variation);
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos) {
        return Blocks.BOOKSHELF.getEnchantPowerBonus(Blocks.BOOKSHELF.defaultBlockState(), world, pos);
    }
}
