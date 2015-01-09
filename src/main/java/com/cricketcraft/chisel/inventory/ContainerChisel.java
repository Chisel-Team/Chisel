package com.cricketcraft.chisel.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.cricketcraft.chisel.carving.Carving;
import com.cricketcraft.chisel.item.chisel.ItemChisel;
import com.cricketcraft.chisel.utils.General;

public class ContainerChisel extends Container {

	public final InventoryChiselSelection inventory;
	public InventoryPlayer playerInventory;
	int currentIndex;
	public ItemStack chisel;
	public boolean finished;
	public Carving carving;

	public ContainerChisel(InventoryPlayer inventoryplayer, InventoryChiselSelection inv) {
		inventory = inv;
		playerInventory = inventoryplayer;
		currentIndex = playerInventory.currentItem;
		inv.container = this;

		int[] leftOffsets = { 8, 26, 134, 152, 44, 116 };
		int[] topOffsets = { 8, 26, 44, 62 };

		for (int i = 0; i < 2; i++) {
			for (int y = 0; y < 4; y++) {
				for (int x = 0; x < 2; x++) {
					addSlotToContainer(new SlotChiselSelection(this, inventory, inventory, x + i * 8 + y * 2, leftOffsets[x + i * 2], topOffsets[y]));
				}
			}
		}

		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 2; x++) {
				addSlotToContainer(new SlotChiselSelection(this, inventory, inventory, 16 + x + y * 2, leftOffsets[4 + x], topOffsets[3 - y]));
			}
		}

		addSlotToContainer(new SlotChiselInput(this, inventory, InventoryChiselSelection.normalSlots, 80, 35));

		for (int k = 0; k < 3; k++) {
			for (int j1 = 0; j1 < 9; j1++) {
				addSlotToContainer(new Slot(inventoryplayer, j1 + k * 9 + 9, 8 + j1 * 18, 102 + k * 18 - 18));
			}
		}

		for (int l = 0; l < 9; l++) {
			addSlotToContainer(l == currentIndex ? new SlotChiselPlayer(this, inventoryplayer, l, 8 + l * 18, 160 - 18) : new Slot(inventoryplayer, l, 8 + l * 18, 160 - 18));
		}

		chisel = inventoryplayer.getCurrentItem();
		if (chisel.stackTagCompound != null) {
			ItemStack stack = ItemStack.loadItemStackFromNBT(chisel.stackTagCompound.getCompoundTag("chiselTarget"));
			inventory.setInventorySlotContents(InventoryChiselSelection.normalSlots, stack);
		}

		Item item = General.getItem(chisel);
		carving = item instanceof ItemChisel ? ItemChisel.carving : Carving.chisel;

		inventory.updateItems();
	}

	@Override
	public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer) {
		if (par3 == 2 && par2 == currentIndex)
			return null;

		return super.slotClick(par1, par2, par3, par4EntityPlayer);
	}

	@Override
	public void onContainerClosed(EntityPlayer entityplayer) {
		inventory.clearItems();
		super.onContainerClosed(entityplayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return inventory.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer entity, int slotIdx) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotIdx);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotIdx > 24) {
				if (!this.mergeItemStack(itemstack1, 24, 25, false)) {
					return null;
				}
			} else {
				if (slotIdx < 24 && itemstack1 != null) {
					entity.inventory.setItemStack(itemstack1.copy());
					slot.onPickupFromSlot(entity, itemstack1);
					itemstack1 = entity.inventory.getItemStack();
					entity.inventory.setItemStack(null);
				}

				if (!this.mergeItemStack(itemstack1, 52, 61, false)) {
					if (!this.mergeItemStack(itemstack1, 25, 52, false)) {
						return null;
					}
				}
			}
			slot.onSlotChange(itemstack1, itemstack);

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}
			if (slotIdx >= 24) {
				slot.onPickupFromSlot(entity, itemstack1);
			}
			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
				return null;
			}
		}
		return itemstack;
	}

	public void onChiselSlotChanged() {
		ItemStack stack = playerInventory.mainInventory[currentIndex];
		if (!stack.isItemEqual(chisel))
			finished = true;

		if (finished)
			return;

		if (chisel.stackTagCompound == null)
			chisel.stackTagCompound = new NBTTagCompound();

		NBTTagCompound tag = new NBTTagCompound();
		if (inventory.getStackInSpecialSlot() != null)
			inventory.getStackInSpecialSlot().writeToNBT(tag);

		chisel.stackTagCompound.setTag("chiselTarget", tag);
		playerInventory.mainInventory[currentIndex] = chisel;
	}
}
