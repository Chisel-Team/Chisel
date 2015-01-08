package com.cricketcraft.chisel.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockLightstoneCarvable extends BlockCarvable {

	public BlockLightstoneCarvable() {
		super(Material.glass);
	}

	@Override
	public int quantityDropped(Random random) {
		return Blocks.glowstone.quantityDropped(random);
	}

	@Override
	public Item getItemDropped(int i, Random random, int a) {
		return Items.glowstone_dust;
	}

	@Override
	public int damageDropped(int i) {
		return 0;
	}
}
