package team.chisel.api.carving;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;

@ParametersAreNonnullByDefault
public interface ICarvingVariation {

    ICarvingGroup getGroup();
    
    Item getItem();
    
    Block getBlock();
}
