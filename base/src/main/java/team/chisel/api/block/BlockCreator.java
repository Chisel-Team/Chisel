package team.chisel.api.block;

import net.minecraft.world.level.block.Block;

public interface BlockCreator<T extends Block & ICarvable> {

    T createBlock(Block.Properties properties, VariationData data);

}
