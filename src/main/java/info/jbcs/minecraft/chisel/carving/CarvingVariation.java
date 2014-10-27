package info.jbcs.minecraft.chisel.carving;

import net.minecraft.block.Block;

public class CarvingVariation implements Comparable
{
    public CarvingVariation(Block block, int metadata, int ord)
    {
        this.order = ord;
        this.block = block;
        this.meta = metadata;
        this.damage = metadata;
    }

    @Override
    public int compareTo(Object a)
    {
        return order - ((CarvingVariation) a).order;
    }

    public int order;
    public Block block;
    public int meta;
    public int damage;
}
