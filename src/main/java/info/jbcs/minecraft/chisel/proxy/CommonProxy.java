package info.jbcs.minecraft.chisel.proxy;

import cpw.mods.fml.common.registry.GameRegistry;
import info.jbcs.minecraft.chisel.block.tileentity.TileEntityAutoChisel;
import info.jbcs.minecraft.chisel.block.tileentity.TileEntityPresent;

public class CommonProxy
{
	public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityAutoChisel.class, "autoChisel");
        GameRegistry.registerTileEntity(TileEntityPresent.class, "tile.present.present");
    }

    public void preInit()
    {
    }

    public void init()
    {
    }
}
