package com.cricketcraft.chisel.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.cricketcraft.chisel.block.tileentity.TileEntityAutoChisel;

public class ContainerAutoChisel extends Container {

	public static class SlotAutoChisel extends Slot {

		public SlotAutoChisel(TileEntityAutoChisel inv, int id, int x, int y) {
			super(inv, id, x, y);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return inventory.isItemValidForSlot(slotNumber, stack);
		}
	}

	public static TileEntityAutoChisel autoChisel;
	public static EntityPlayer player;

	public ContainerAutoChisel(InventoryPlayer player, TileEntityAutoChisel tileEntityAutoChisel) {

		ContainerAutoChisel.player = player.player;
		autoChisel = tileEntityAutoChisel;

		addSlotToContainer(new SlotAutoChisel(tileEntityAutoChisel, 0, 53, 15));
		addSlotToContainer(new SlotAutoChisel(tileEntityAutoChisel, 1, 78, 51));
		addSlotToContainer(new SlotAutoChisel(tileEntityAutoChisel, 2, 103, 15));
		addSlotToContainer(new SlotAutoChisel(tileEntityAutoChisel, 3, 8, 62));
		addSlotToContainer(new SlotAutoChisel(tileEntityAutoChisel, 4, 151, 11));
		addSlotToContainer(new SlotAutoChisel(tileEntityAutoChisel, 5, 151, 31));
		addSlotToContainer(new SlotAutoChisel(tileEntityAutoChisel, 6, 151, 51));

		bindPlayerInventory(player);
	}

	protected void bindPlayerInventory(InventoryPlayer invPlayer) {
		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 9; column++) {
				addSlotToContainer(new Slot(invPlayer, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
			}
		}

		for (int slotNumber = 0; slotNumber < 9; slotNumber++) {
			addSlotToContainer(new Slot(invPlayer, slotNumber, 8 + slotNumber * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return autoChisel.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
		ItemStack itemStack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotNumber);
		System.out.println(slotNumber);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();

			// if we are in the auto chisel
			if (slotNumber <= 6) {
				if (!this.mergeItemStack(itemStack1, 33, 42, false)) {
					return null;
				}
				// otherwise just put it in one of the machine slots
			} else if (!this.mergeItemStack(itemStack1, 0, 7, false)) {
				return null;
			}

			if (itemStack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			if (itemStack1.stackSize == itemStack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, itemStack1);
		}

		return itemStack;
	}
}
