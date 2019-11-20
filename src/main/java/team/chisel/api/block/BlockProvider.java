package team.chisel.api.block;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import team.chisel.Chisel;
import team.chisel.common.init.ChiselTabs;

public interface BlockProvider<T extends Block & ICarvable> extends BlockCreator<T> {

    Class<T> getBlockClass();

    default BlockItem createBlockItem(@Nonnull T block) {
        return (BlockItem) new BlockItem(block, new Item.Properties().group(ChiselTabs.tab)).setRegistryName(block.getRegistryName());
    }
}
