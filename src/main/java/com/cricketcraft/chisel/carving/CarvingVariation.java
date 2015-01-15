package com.cricketcraft.chisel.carving;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;

public class CarvingVariation implements ICarvingVariation {

	private int order;
	private Block block;
	private int meta;
	private int damage;

	public CarvingVariation(Block block, int metadata, int ord) {
		this.order = ord;
		this.block = block;
		this.meta = metadata;
		this.damage = metadata;
	}

	@Override
	public Block getBlock() {
		return block;
	}

	@Override
	public int getBlockMeta() {
		return meta;
	}

	@Override
	public int getItemMeta() {
		return damage;
	}

	@Override
	public int getOrder() {
		return order;
	}

	@Override
	public int compareTo(ICarvingVariation o) {
		return CarvingUtils.compare(this, o);
	}
}
