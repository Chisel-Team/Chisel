package info.jbcs.minecraft.chisel.block;

import info.jbcs.minecraft.chisel.carving.CarvableHelper;
import net.minecraft.block.Block;

public interface BlockMarbleStairsMakerCreator
{
    public BlockMarbleStairs create(Block block, int meta, CarvableHelper helper);
}
