package team.chisel.block;

import team.chisel.api.carving.CarvableHelper;
import net.minecraft.block.Block;

public interface IStairsCreator {

	public BlockCarvableStairs create(Block block, int meta, CarvableHelper helper);
}
