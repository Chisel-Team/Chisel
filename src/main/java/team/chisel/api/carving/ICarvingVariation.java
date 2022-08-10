package team.chisel.api.carving;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface ICarvingVariation {

    ICarvingGroup getGroup();
    
    Item getItem();
    
    Block getBlock();
}
