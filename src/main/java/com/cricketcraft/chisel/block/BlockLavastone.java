package com.cricketcraft.chisel.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import com.cricketcraft.chisel.client.GeneralChiselClient;

public class BlockLavastone extends BlockMultiLayer {

	public BlockLavastone(Material mat, String baseIcon) {
		super(mat, baseIcon);
		setLightLevel(1F);
	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (random.nextInt(8) == 0 && world.isRemote)
			GeneralChiselClient.spawnLavastoneFX(world, this, x, y, z);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return true;
	}

}
