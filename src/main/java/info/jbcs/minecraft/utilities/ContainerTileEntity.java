package info.jbcs.minecraft.utilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerTileEntity<T extends TileEntity & IInventory> extends Container {
	public final IInventory playerInventory;
	public final T entity;
	public int playerSlotsCount;
	
	public ContainerTileEntity(IInventory playerInv, T tileEntity,int startX,int startY) {
		playerInventory = playerInv;
		entity = tileEntity;

		for (int k = 0; k < 3; k++) {
			for (int j1 = 0; j1 < 9; j1++) {
				addSlotToContainer(new Slot(playerInv, j1 + k * 9 + 9, startX + j1 * 18, startY + k * 18));
			}
		}

		for (int l = 0; l < 9; l++) {
			addSlotToContainer(new Slot(playerInv, l, startX + l * 18, startY + 142-84));
		}
		
		playerSlotsCount=inventorySlots.size();
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return entity.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer entityplayer, int i) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(i);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (i < playerSlotsCount) {
				if (!mergeItemStack(itemstack1, playerSlotsCount, inventorySlots.size(), true)) {
					return null;
				}
			} else {
				if (!mergeItemStack(itemstack1, 0, playerSlotsCount, false)) {
					return null;
				}
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}
}
