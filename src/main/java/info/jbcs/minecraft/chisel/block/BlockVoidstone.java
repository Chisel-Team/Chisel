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

public class BlockVoidstone extends Block implements ICarvable {
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
    public CarvableVariation getVariation(int metadata) {
        return carvableHelper.getVariation(metadata);
    }
}
