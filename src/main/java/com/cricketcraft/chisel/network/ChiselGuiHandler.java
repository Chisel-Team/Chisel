package com.cricketcraft.chisel.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.cricketcraft.chisel.block.tileentity.TileEntityAutoChisel;
import com.cricketcraft.chisel.block.tileentity.TileEntityPresent;
import com.cricketcraft.chisel.client.gui.GuiAutoChisel;
import com.cricketcraft.chisel.client.gui.GuiChisel;
import com.cricketcraft.chisel.client.gui.GuiPresent;
import com.cricketcraft.chisel.inventory.ContainerAutoChisel;
import com.cricketcraft.chisel.inventory.ContainerChisel;
import com.cricketcraft.chisel.inventory.ContainerPresent;
import com.cricketcraft.chisel.inventory.InventoryChiselSelection;

import cpw.mods.fml.common.network.IGuiHandler;

public class ChiselGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case 0:
			return new ContainerChisel(player.inventory, new InventoryChiselSelection(null));
		case 1:
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity instanceof TileEntityAutoChisel)
				return new ContainerAutoChisel(player.inventory, (TileEntityAutoChisel) tileentity);
		case 2:
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if (tileEntity instanceof TileEntityPresent)
				return new ContainerPresent(player.inventory, (TileEntityPresent) tileEntity);
		default:
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case 0:
			return new GuiChisel(player.inventory, new InventoryChiselSelection(null));
		case 1:
			TileEntity tileentity = world.getTileEntity(x, y, z);
			if (tileentity instanceof TileEntityAutoChisel)
				return new GuiAutoChisel(player.inventory, (TileEntityAutoChisel) tileentity);
		case 2:
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if (tileEntity instanceof TileEntityPresent)
				return new GuiPresent(player.inventory, (TileEntityPresent) tileEntity);
		default:
			return null;
		}
	}
}
