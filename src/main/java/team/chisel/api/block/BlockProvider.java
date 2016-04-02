package team.chisel.api.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public interface BlockProvider<T extends Block & ICarvable> extends BlockCreator<T> {

    Class<T> getBlockClass();

    default ItemBlock createItemBlock(T block) {
        return (ItemBlock) new ItemBlock(block).setRegistryName(block.getRegistryName());
    }
}
