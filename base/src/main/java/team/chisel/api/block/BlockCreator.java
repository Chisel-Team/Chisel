package team.chisel.api.block;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;

@ParametersAreNonnullByDefault
public interface BlockCreator<T extends Block & ICarvable> {

    T createBlock(Block.Properties properties, VariationData data);

}
