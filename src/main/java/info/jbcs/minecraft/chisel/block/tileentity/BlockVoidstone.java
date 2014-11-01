package info.jbcs.minecraft.chisel.block.tileentity;

import info.jbcs.minecraft.chisel.client.GeneralChiselClient;
import info.jbcs.minecraft.chisel.utils.General;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockVoidstone extends Block {

    public static IIcon iconParticle;

    protected BlockVoidstone() {
        super(Material.rock);
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        if(General.rand.nextInt(4) == 0 && world.isRemote)
            GeneralChiselClient.spawnVoidstoneFX(world, this, x, y, z);
    }
}
