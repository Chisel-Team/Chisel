package com.cricketcraft.chisel.block;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.block.tileentity.TileEntityDeChisel;
import com.cricketcraft.chisel.init.ChiselTabs;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDeChisel extends BlockContainer {
    public BlockDeChisel() {
        super(Material.iron);
        setCreativeTab(ChiselTabs.tabChisel);
        setHardness(2.0F);
        setResistance(10.0F);
        setBlockName("dechisel");
        setBlockTextureName(Chisel.MOD_ID + ":DeChisel");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityDeChisel();
    }
}
