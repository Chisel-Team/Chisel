package info.jbcs.minecraft.chisel.block;


import info.jbcs.minecraft.chisel.client.GeneralChiselClient;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockLavastone extends BlockMarbleTexturedOre
{
    public BlockLavastone(Material mat, String baseIcon)
    {
        super(mat, baseIcon);
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        if(random.nextInt(8) == 0)
            GeneralChiselClient.spawnLavastoneFX(world, this, x, y, z);
    }

}
