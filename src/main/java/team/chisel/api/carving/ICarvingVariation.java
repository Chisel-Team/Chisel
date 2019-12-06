package team.chisel.api.carving;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

@ParametersAreNonnullByDefault
public interface ICarvingVariation {

    ICarvingGroup getGroup();
    
    Item getItem();
    
    Block getBlock();
}
