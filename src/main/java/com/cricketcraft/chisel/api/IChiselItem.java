package com.cricketcraft.chisel.api;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Implement this on items which can be used to chisel blocks.
 */
public interface IChiselItem extends IChiselMode {

	/**
	 * Called when a block is chiseled using this item. This method is responsible for damaging and deleting the chisel item.
	 * 
	 * @param stack
	 *            The {@link ItemStack} being used
	 * @return True if this chisel should be damaged. False otherwise.
	 */
	void onChisel(World world, IInventory inv, int slot, ItemStack chisel, ItemStack target);

}
