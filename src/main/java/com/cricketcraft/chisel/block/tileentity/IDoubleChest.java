package com.cricketcraft.chisel.block.tileentity;

import net.minecraft.item.ItemStack;

public interface IDoubleChest {

	/**
	 * @return The size of this specific chest's inventory. Does not account for the connection.
	 */
	int getTrueSizeInventory();

	/**
	 * @param slot
	 *            The true slot index.
	 * @return The actual stack in this chest's slot. Does not account for the connection.
	 */
	ItemStack getTrueStackInSlot(int slot);

	/**
	 * Puts a stack in this chest's real slot at the given true index.
	 * 
	 * @param slot
	 *            The true slot index.
	 * @param stack
	 *            The ItemStack to place in the slot.
	 */
	void putStackInTrueSlot(int slot, ItemStack stack);

}
