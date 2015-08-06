package com.cricketcraft.chisel.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerPresent extends Container {

	private IInventory lower;
	private int rows;

	public ContainerPresent(IInventory player, IInventory chest) {
		lower = chest;
		rows = chest.getSizeInventory() / 9;
		chest.openInventory();
		int a = (rows - 4) * 18;

		for (int d = 0; d < rows; d++) {
			for (int e = 0; e < 9; e++) {
				addSlotToContainer(new Slot(chest, e + d * 9, 8 + e * 18, 18 + d * 18));
			}
		}

		for (int d = 0; d < 3; d++) {
			for (int e = 0; e < 9; e++) {
				addSlotToContainer(new Slot(player, e + d * 9 + 9, 8 + e * 18, 103 + d * 18 + a));
			}
		}

		for (int d = 0; d < 9; d++) {
			addSlotToContainer(new Slot(player, d, 8 + d * 18, 161 + a));
		}
	}

	public IInventory getLowerPresentInventory() {
		return lower;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {

		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(p_82846_2_);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (p_82846_2_ < rows * 9) {
				if (!this.mergeItemStack(itemstack1, rows * 9, inventorySlots.size(), true)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, rows * 9, false)) {
				return null;
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return true;
	}
}
