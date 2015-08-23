package team.chisel.block;

import com.cricketcraft.chisel.api.carving.CarvableHelper;

import net.minecraft.block.Block;

public interface IStairsCreator {

	public BlockCarvableStairs create(Block block, int meta, CarvableHelper helper);
}
