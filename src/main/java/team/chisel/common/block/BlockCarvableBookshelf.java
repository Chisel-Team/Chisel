package team.chisel.common.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.api.block.VariationData;


public class BlockCarvableBookshelf extends BlockCarvable {

    public BlockCarvableBookshelf(Material material, int index, int max, VariationData[] variations) {
        super(material, index, max, variations);
    }
    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return Blocks.bookshelf.getDrops(world, pos, state, fortune);
    }
}
