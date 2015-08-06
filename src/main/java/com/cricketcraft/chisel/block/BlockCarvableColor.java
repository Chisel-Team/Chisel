package com.cricketcraft.chisel.block;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCarvableColor extends BlockCarvable {

	public BlockCarvableColor() {
		this(Material.rock);
	}

	public BlockCarvableColor(Material m) {
		super(m);
	}

	@Override
	public boolean recolourBlock(World world, int x, int y, int z, ForgeDirection side, int color) {
		int newColor = 15 - color;
		if (world.getBlockMetadata(x, y, z) != newColor) {
			world.setBlockMetadataWithNotify(x, y, z, newColor, 3);
			return true;
		}
		return false;
	}
}
