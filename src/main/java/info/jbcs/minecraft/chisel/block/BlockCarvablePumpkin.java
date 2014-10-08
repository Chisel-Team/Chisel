package info.jbcs.minecraft.chisel.block;

import net.minecraft.world.World;

/**
 * Created by Pokefenn.
 * Licensed under MIT (If this is one of my Mods)
 */
public class BlockCarvablePumpkin extends BlockCarvable
{

    public BlockCarvablePumpkin()
    {
        super();
    }

    @Override
    public int onBlockPlaced(World world_, int x, int y, int z_, int p_149660_5_, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_)
    {
        return p_149660_9_;
    }
}
