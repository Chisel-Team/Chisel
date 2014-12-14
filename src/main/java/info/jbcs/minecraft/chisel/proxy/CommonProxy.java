package info.jbcs.minecraft.chisel.proxy;

import info.jbcs.minecraft.chisel.block.tileentity.TileEntityAutoChisel;
import info.jbcs.minecraft.chisel.block.tileentity.TileEntityPresent;
import cpw.mods.fml.common.registry.GameRegistry;

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
