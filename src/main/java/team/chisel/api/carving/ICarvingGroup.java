package team.chisel.api.carving;

import java.util.Collection;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;

@ParametersAreNonnullByDefault
public interface ICarvingGroup {

    Collection<Block> getBlocks();

    Collection<Item> getItems();
	
	SoundEvent getSound();
}
