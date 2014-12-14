package com.cricketcraft.minecraft.chisel.proxy;

import com.cricketcraft.minecraft.chisel.block.tileentity.TileEntityAutoChisel;
import com.cricketcraft.minecraft.chisel.block.tileentity.TileEntityPresent;

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
