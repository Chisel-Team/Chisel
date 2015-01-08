package com.cricketcraft.chisel.proxy;

import com.cricketcraft.chisel.block.tileentity.TileEntityAutoChisel;
import com.cricketcraft.chisel.block.tileentity.TileEntityPresent;

import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityAutoChisel.class, "autoChisel");
		GameRegistry.registerTileEntity(TileEntityPresent.class, "tile.present.present");
	}

	public void preInit() {
	}

	public void init() {
	}
}
