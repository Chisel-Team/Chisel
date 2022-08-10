package team.chisel.api.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;

public interface BlockProvider<T extends Block & ICarvable> extends BlockCreator<T> {

    BlockItem createBlockItem(@Nonnull T block, @Nonnull Item.Properties properties);
}
