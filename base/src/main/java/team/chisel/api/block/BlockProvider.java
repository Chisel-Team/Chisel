package team.chisel.api.block;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public interface BlockProvider<T extends Block & ICarvable> extends BlockCreator<T> {

    BlockItem createBlockItem(@Nonnull T block, @Nonnull Item.Properties properties);
}
