package info.jbcs.minecraft.chisel.block;

import info.jbcs.minecraft.chisel.api.ICarvable;
import info.jbcs.minecraft.chisel.carving.CarvableHelper;
import info.jbcs.minecraft.chisel.carving.CarvableVariation;
import info.jbcs.minecraft.chisel.client.GeneralChiselClient;
import info.jbcs.minecraft.chisel.init.ModTabs;
import info.jbcs.minecraft.chisel.utils.General;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockVoidstone extends Block implements ICarvable{

    public static IIcon iconParticle, topBottom, pillarSide;
    public static CarvableHelper carvableHelper;

    public BlockVoidstone() {
        super(Material.rock);
        carvableHelper = new CarvableHelper();
        setHardness(5.0F);
        setResistance(10.0F);
        setCreativeTab(ModTabs.tabChiselBlocks);
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        if(General.rand.nextInt(4) == 0 && world.isRemote)
            GeneralChiselClient.spawnVoidstoneFX(world, this, x, y, z);
    }

    @Override
    public IIcon getIcon(int side, int meta){
        if(side == 0 || side == 1){
            return topBottom;
        } else {
            return pillarSide;
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister icon){
        iconParticle = icon.registerIcon("Chisel:voidstone/particles/star");
        topBottom = icon.registerIcon("Chisel:voidstone/pillar-top");
        pillarSide = icon.registerIcon("Chisel:voidstone/pillar-side");
    }

    @Override
    public CarvableVariation getVariation(int metadata) {
        return carvableHelper.getVariation(metadata);
    }
}
