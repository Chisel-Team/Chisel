package team.chisel.api.block;

import javax.annotation.Nonnull;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public interface BlockProvider<T extends Block & ICarvable> extends BlockCreator<T> {

    BlockItem createBlockItem(@Nonnull T block, @Nonnull Item.Properties properties);
}
