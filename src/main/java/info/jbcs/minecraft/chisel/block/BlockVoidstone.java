package info.jbcs.minecraft.chisel.block;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.api.ICarvable;
import info.jbcs.minecraft.chisel.carving.CarvableHelper;
import info.jbcs.minecraft.chisel.carving.CarvableVariation;
import info.jbcs.minecraft.chisel.client.GeneralChiselClient;
import info.jbcs.minecraft.chisel.utils.General;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockVoidstone extends Block implements ICarvable{
    public CarvableHelper carverHelper;
    public static IIcon iconParticle;

    public BlockVoidstone() {
        super(Material.rock);
        carverHelper = new CarvableHelper();
        setHardness(5.0F);
        setResistance(10.0F);
    }

    @Override
    public void registerBlockIcons(IIconRegister icon){
        iconParticle = icon.registerIcon(Chisel.MOD_ID + ":voidstone/particles/star");
    }
    
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        if(General.rand.nextInt(4) == 0 && world.isRemote)
            GeneralChiselClient.spawnVoidstoneFX(world, this, x, y, z);
    }

    @Override
    public CarvableVariation getVariation(int metadata) {
        return carverHelper.getVariation(metadata);
    }
}
