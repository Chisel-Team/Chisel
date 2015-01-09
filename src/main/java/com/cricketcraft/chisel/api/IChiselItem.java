package com.cricketcraft.chisel.api;

import net.minecraft.block.Block;
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

	/**
	 * Allows you to control if your item can chisel this block in the world.
	 * 
	 * @param world
	 *            World object
	 * @param x
	 *            X coord of the block being chiseled
	 * @param y
	 *            Y coord of the block being chiseled
	 * @param z
	 *            Z coord of the block being chiseled
	 * @param block
	 *            The {@link Block} being chiseled
	 * @param metadata
	 *            The blocks' metadata
	 * @return True if the chiseling should take place. False otherwise.
	 */
	boolean canChiselBlock(World world, int x, int y, int z, Block block, int metadata);
}
