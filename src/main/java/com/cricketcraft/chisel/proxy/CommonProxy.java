package com.cricketcraft.chisel.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.cricketcraft.chisel.block.tileentity.TileEntityAutoChisel;
import com.cricketcraft.chisel.block.tileentity.TileEntityCarvableBeacon;
import com.cricketcraft.chisel.block.tileentity.TileEntityPresent;

import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityAutoChisel.class, "autoChisel");
		GameRegistry.registerTileEntity(TileEntityPresent.class, "tile.chisel.present");
		GameRegistry.registerTileEntity(TileEntityCarvableBeacon.class, "tile.chisel.beacon");
	}

	public void preInit() {
	}

	public void init() {
	}

	public EntityPlayer getClientPlayer() {
		return null;
	}

	public World getClientWorld() {
		return null;
	}
}
