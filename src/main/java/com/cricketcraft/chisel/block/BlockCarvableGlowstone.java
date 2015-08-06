package com.cricketcraft.chisel.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockCarvableGlowstone extends BlockCarvable {

	public BlockCarvableGlowstone() {
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

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
		Block glowstone = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		return new ItemStack(Item.getItemFromBlock(glowstone), 1, meta);
	}
}
