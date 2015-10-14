package team.chisel.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import team.chisel.block.tileentity.TileEntityAutoChisel;
import team.chisel.block.tileentity.TileEntityPresent;
import team.chisel.client.gui.GuiAutoChisel;
import team.chisel.client.gui.GuiChisel;
import team.chisel.client.gui.GuiPresent;
import team.chisel.inventory.ContainerAutoChisel;
import team.chisel.inventory.ContainerChisel;
import team.chisel.inventory.ContainerPresent;
import team.chisel.inventory.InventoryChiselSelection;
import cpw.mods.fml.common.network.IGuiHandler;

public class ChiselGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case 0:
			return new ContainerChisel(player.inventory, new InventoryChiselSelection(player.getCurrentEquippedItem()));
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
			return new GuiChisel(player.inventory, new InventoryChiselSelection(player.getCurrentEquippedItem()));
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
