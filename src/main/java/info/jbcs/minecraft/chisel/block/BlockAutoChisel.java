package info.jbcs.minecraft.chisel.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAutoChisel extends BlockContainer
{
    public BlockAutoChisel()
    {
        super(Material.rock);
        setBlockName("autoChisel");
        setHardness(1F);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return null;
    }
}
