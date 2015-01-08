package com.cricketcraft.chisel.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.cricketcraft.chisel.block.tileentity.TileEntityAutoChisel;
import com.cricketcraft.chisel.block.tileentity.TileEntityAutoChisel.Upgrade;
import com.cricketcraft.chisel.init.ChiselItems;

public class ContainerAutoChisel extends Container {

	public static class SlotUpgrade extends Slot {

		public Upgrade upgradeType;

		public SlotUpgrade(TileEntityAutoChisel inv, int id, int x, int y, Upgrade upgrade) {
			super(inv, id, x, y);
			this.upgradeType = upgrade;
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return stack.getItem() == ChiselItems.upgrade && stack.getItemDamage() == upgradeType.ordinal();
		}
	}

	public static TileEntityAutoChisel autoChisel;
	public static EntityPlayer player;

	public ContainerAutoChisel(InventoryPlayer player, TileEntityAutoChisel tileEntityAutoChisel) {

		ContainerAutoChisel.player = player.player;
		autoChisel = tileEntityAutoChisel;

		addSlotToContainer(new Slot(tileEntityAutoChisel, 0, 53, 15));
		addSlotToContainer(new Slot(tileEntityAutoChisel, 1, 78, 51));
		addSlotToContainer(new Slot(tileEntityAutoChisel, 2, 103, 15));
		addSlotToContainer(new SlotUpgrade(tileEntityAutoChisel, 3, 151, 11, Upgrade.SPEED));
		addSlotToContainer(new SlotUpgrade(tileEntityAutoChisel, 4, 151, 31, Upgrade.AUTOMATION));
		addSlotToContainer(new SlotUpgrade(tileEntityAutoChisel, 5, 151, 51, Upgrade.STACK));

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
			if (slotNumber <= 5) {
				// merge hotbar
				if (!this.mergeItemStack(itemStack1, 33, 42, false)) {
					// merge rest of inventory
					if (!this.mergeItemStack(itemStack1, 6, 33, false)) {
						return null;
					}
				}
			} else {
				// if this is an upgrade, check the upgrade slots
				if (itemStack1.getItem() == ChiselItems.upgrade) {
					if (!this.mergeItemStack(itemStack1, 3, 6, false)) {
						return null;
					}
				} else {
					// otherwise just put it in one of the 3 other slots
					if (!this.mergeItemStack(itemStack1, 0, 3, false)) {
						return null;
					}
				}
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
