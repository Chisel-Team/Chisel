package team.chisel.api.block;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import team.chisel.common.init.ChiselTabs;

public interface BlockProvider<T extends Block & ICarvable> extends BlockCreator<T> {

    Class<T> getBlockClass();

    default BlockItem createBlockItem(@Nonnull T block, @Nonnull Item.Properties properties) {
        return (BlockItem) new BlockItem(block, properties.group(ChiselTabs.tab));
    }
}
