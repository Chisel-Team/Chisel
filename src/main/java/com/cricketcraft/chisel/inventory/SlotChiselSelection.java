package com.cricketcraft.chisel.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.cricketcraft.chisel.utils.General;

public class SlotChiselSelection extends Slot {

	public SlotChiselSelection(ContainerChisel container, InventoryChiselSelection inv, IInventory iinventory, int i, int j, int k) {
		super(iinventory, i, j, k);

		this.container = container;
		selInventory = inv;
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return false;
	}

	@Override
	public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
		if (container.finished)
			return false;

		return par1EntityPlayer.inventory.getItemStack() == null;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack itemstack) {
		ItemStack stack = player.inventory.getItemStack();
		ItemStack crafted = selInventory.inventory[InventoryChiselSelection.normalSlots];

		if (stack == null) {
			if (crafted != null && crafted.stackSize > 0)
				crafted.stackSize--;
			if (crafted.stackSize == 0)
				crafted = null;

			selInventory.setInventorySlotContents(InventoryChiselSelection.normalSlots, crafted);
		} else {
			putStack(new ItemStack(itemstack.getItem(), itemstack.stackSize, itemstack.getItemDamage()));

			player.inventory.setItemStack(null);

			if (selInventory.inventory[InventoryChiselSelection.normalSlots] == null)
				return;

			player.inventory.setItemStack(new ItemStack(itemstack.getItem(), selInventory.inventory[InventoryChiselSelection.normalSlots].stackSize, itemstack.getItemDamage()));
			selInventory.setInventorySlotContents(InventoryChiselSelection.normalSlots, null);
		}

		selInventory.updateItems();

		String sound = container.carving.getVariationSound(itemstack.getItem(), itemstack.getItemDamage());
		player.worldObj.playSoundAtEntity(player, sound, 0.3f + 0.7f * General.rand.nextFloat(), 0.6f + 0.4f * General.rand.nextFloat());

		/*
		 * ItemStack stack=player.inventory.getItemStack();
		 * 
		 * putStack(new ItemStack(itemstack.itemID, itemstack.stackSize,
		 * itemstack.getItemDamage()));
		 * 
		 * super.onPickupFromSlot(player,itemstack);
		 * 
		 * player.inventory.setItemStack(null);
		 * 
		 * if(selInventory.items[InventoryChiselSelection.normalSlots]==null)
		 * return;
		 * 
		 * player.inventory.setItemStack(new ItemStack(itemstack.itemID,
		 * selInventory.items[InventoryChiselSelection.normalSlots].stackSize,
		 * itemstack.getItemDamage()));
		 * selInventory.setInventorySlotContents(InventoryChiselSelection
		 * .normalSlots,null);
		 * 
		 * selInventory.updateItems();
		 * 
		 * String sound=Carving.chisel.getVariationSound(itemstack.itemID,
		 * itemstack.getItemDamage()); player.worldObj.playSoundAtEntity(player,
		 * sound, 0.3f + 0.7f * General.rand.nextFloat(), 0.6f + 0.4f *
		 * General.rand.nextFloat());
		 */
	}

	ContainerChisel container;
	InventoryChiselSelection selInventory;
}
