package com.cricketcraft.chisel.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class ItemCarvablePumpkin extends ItemCarvable {

	public ItemCarvablePumpkin(Block block) {
		super(block);
	}

	@Override
	public boolean isValidArmor(ItemStack itemStack, int armorType, Entity entity) {
		return true;
	}
}
