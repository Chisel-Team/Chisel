package com.cricketcraft.chisel.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarvableBookshelf extends BlockCarvable {

	public BlockCarvableBookshelf() {
		super(Material.wood);
		setHarvestLevel("axe", 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		if (side < 2)
			return Blocks.planks.getBlockTextureFromSide(side);
		return super.getIcon(side, metadata);
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		if (side < 2)
			return Blocks.planks.getBlockTextureFromSide(side);
		return super.getIcon(world, x, y, z, side);
	}

	@Override
	public int quantityDropped(Random random) {
		return 3;
	}

	@Override
	public Item getItemDropped(int ammount, Random random, int meta) {
		return Items.book;
	}

    @Override
	public int damageDropped(int damage) {
		return 0;
	}

	@Override
	public int getDamageValue(World world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z);
	}

	@Override
	public float getEnchantPowerBonus(World world, int x, int y, int z) {
		return 1;
	}
}
