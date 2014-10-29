package info.jbcs.minecraft.chisel.proxy;

import info.jbcs.minecraft.chisel.block.tileentity.TileEntityAutoChisel;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy
{
	public void registerTileEntities() {
		registerTileEntity(TileEntityAutoChisel.class, "autoChisel");
	}

	private void registerTileEntity(Class<? extends TileEntity> cls, String baseName) {
		GameRegistry.registerTileEntity(cls, "tile.autoChisel." + baseName);
	}
    public void preInit()
    {
    }

    public void init()
    {
    }
}
